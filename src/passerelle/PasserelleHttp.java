package passerelle;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * Passerelle HTTP -> TCP pour l'interface web.
 * C'est un client TCP supplementaire du serveur de la bibliotheque : elle ne possede
 * aucune copie du catalogue et ne fait aucune recherche. Elle traduit simplement
 * une requete HTTP en une ligne du protocole, puis la reponse en JSON.
 *
 * Usage : java passerelle.PasserelleHttp [portHttp] [hoteServeur] [portServeur]
 */
public class PasserelleHttp {
    private static String hoteTcp = "127.0.0.1";
    private static int portTcp = 5000;

    public static void main(String[] args) throws IOException {
        int portHttp = args.length > 0 ? Integer.parseInt(args[0]) : 8081;
        if (args.length > 1) hoteTcp = args[1];
        if (args.length > 2) portTcp = Integer.parseInt(args[2]);

        HttpServer http = HttpServer.create(new InetSocketAddress(portHttp), 0);
        http.createContext("/api/livres", ex -> traiter(ex, "LISTE"));
        http.createContext("/api/recherche", ex -> {
            String theme = parametre(ex.getRequestURI().getRawQuery(), "theme");
            if (theme == null || theme.indexOf('\n') >= 0 || theme.indexOf('\r') >= 0) {
                repondreRefus(ex, "RECHERCHE;" + (theme == null ? "" : "?"), "REQUETE_INVALIDE");
            } else {
                traiter(ex, "RECHERCHE;" + theme);
            }
        });
        http.start();
        System.out.println("Passerelle HTTP sur le port " + portHttp
                + " -> serveur TCP " + hoteTcp + ":" + portTcp);
    }

    private static void traiter(HttpExchange ex, String requete) throws IOException {
        ajouterEntetes(ex);
        if ("OPTIONS".equals(ex.getRequestMethod())) { ex.sendResponseHeaders(204, -1); ex.close(); return; }
        if (!"GET".equals(ex.getRequestMethod())) { ex.sendResponseHeaders(405, -1); ex.close(); return; }

        try {
            String reponse = envoyer(requete);
            envoyerJson(ex, 200, convertir(requete, reponse));
        } catch (IOException e) {
            String json = "{\"statut\":\"COMMUNICATION\",\"requete\":" + js(requete)
                    + ",\"message\":" + js(e.getMessage() == null ? "Erreur de communication" : e.getMessage()) + "}";
            envoyerJson(ex, 502, json);
        }
    }

    private static void repondreRefus(HttpExchange ex, String requete, String code) throws IOException {
        ajouterEntetes(ex);
        envoyerJson(ex, 200, "{\"statut\":\"ERREUR\",\"requete\":" + js(requete)
                + ",\"reponse\":" + js("ERR;" + code) + ",\"code\":" + js(code) + "}");
    }

    /** Une connexion = une requete et une reponse (convention du TP). */
    private static String envoyer(String requete) throws IOException {
        try (Socket s = new Socket()) {
            s.connect(new InetSocketAddress(hoteTcp, portTcp), 3000);
            s.setSoTimeout(5000);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8));
            out.write(requete + "\n");
            out.flush();
            String reponse = in.readLine();
            if (reponse == null) throw new IOException("Connexion fermee sans reponse");
            return reponse;
        } catch (IOException e) {
            throw new IOException("Serveur TCP injoignable ou en echec (" + hoteTcp + ":" + portTcp + ") : " + e.getMessage(), e);
        }
    }

    private static String convertir(String requete, String reponse) throws IOException {
        String[] p = reponse.split(";", -1);
        if (p[0].equals("ERR") && p.length >= 2) {
            return "{\"statut\":\"ERREUR\",\"requete\":" + js(requete) + ",\"reponse\":" + js(reponse)
                    + ",\"code\":" + js(p[1]) + "}";
        }
        if (p[0].equals("OK") && p.length >= 2) {
            StringBuilder sb = new StringBuilder("[");
            for (int i = 2; i < p.length; i++) {
                int k = p[i].indexOf(':');
                if (k < 0) throw new IOException("Reponse non conforme au protocole");
                if (i > 2) sb.append(',');
                sb.append("{\"id\":").append(js(p[i].substring(0, k)))
                  .append(",\"titre\":").append(js(p[i].substring(k + 1))).append('}');
            }
            sb.append(']');
            return "{\"statut\":\"OK\",\"requete\":" + js(requete) + ",\"reponse\":" + js(reponse)
                    + ",\"livres\":" + sb + "}";
        }
        throw new IOException("Reponse non conforme au protocole");
    }

    private static String parametre(String query, String nom) throws IOException {
        if (query == null) return null;
        for (String kv : query.split("&")) {
            int k = kv.indexOf('=');
            String cle = URLDecoder.decode(k < 0 ? kv : kv.substring(0, k), "UTF-8");
            if (cle.equals(nom)) return k < 0 ? "" : URLDecoder.decode(kv.substring(k + 1), "UTF-8");
        }
        return null;
    }

    private static void ajouterEntetes(HttpExchange ex) {
        ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        ex.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, OPTIONS");
    }

    private static void envoyerJson(HttpExchange ex, int code, String json) throws IOException {
        byte[] data = json.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        ex.sendResponseHeaders(code, data.length);
        try (OutputStream os = ex.getResponseBody()) { os.write(data); }
    }

    private static String js(String s) {
        StringBuilder sb = new StringBuilder("\"");
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"': sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                default:
                    if (c < 0x20) sb.append(String.format("\\u%04x", (int) c)); else sb.append(c);
            }
        }
        return sb.append('"').toString();
    }
}
