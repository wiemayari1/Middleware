package reseau;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Usage : java ClientBibliothequeTCP [adresseServeur] [port] [requete]
 * Sans requête, elle est demandée à l'utilisateur.
 */
public class ClientBibliothequeTCP {
    public static void main(String[] args) {
        String hote = args.length > 0 ? args[0] : "127.0.0.1";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 5000;

        String requete;
        if (args.length > 2) {
            requete = args[2];
        } else {
            System.out.print("Requête (LISTE | RECHERCHE;theme) : ");
            Scanner sc = new Scanner(System.in);
            requete = sc.hasNextLine() ? sc.nextLine() : "";
        }

        try (Socket socket = new Socket(hote, port);
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             BufferedWriter out = new BufferedWriter(
                     new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8))) {

            out.write(requete + "\n");
            out.flush();

            String reponse = in.readLine();
            if (reponse == null) {
                // Fermeture sans réponse : échec de communication, jamais "OK;0"
                System.err.println("Échec de communication : connexion fermée sans réponse.");
                return;
            }
            afficher(reponse);
        } catch (IOException e) {
            System.err.println("Échec de connexion à " + hote + ":" + port + " : " + e.getMessage());
        }
    }

    private static void afficher(String reponse) {
        System.out.println("Réponse brute : " + reponse);
        String[] p = reponse.split(";", -1);
        if (p[0].equals("ERR")) {
            System.out.println("Erreur du serveur : " + (p.length > 1 ? p[1] : "?"));
        } else if (p[0].equals("OK") && p.length >= 2) {
            System.out.println(p[1] + " livre(s) :");
            for (int i = 2; i < p.length; i++) {
                int k = p[i].indexOf(':');
                System.out.println("  " + p[i].substring(0, k) + " -> " + p[i].substring(k + 1));
            }
        } else {
            System.err.println("Réponse non conforme au protocole.");
        }
    }
}
