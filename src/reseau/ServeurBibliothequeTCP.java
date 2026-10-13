package reseau;

import metier.*;
import protocole.ProtocoleBibliotheque;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Usage : java ServeurBibliothequeTCP [port] [adresseLocaleEcoute]
 * Sans adresse : écoute sur toutes les interfaces (accessible depuis un autre poste).
 */
public class ServeurBibliothequeTCP {
    public static void main(String[] args) {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 5000;

        // Une seule bibliothèque et une seule instance de GestionBibliotheque
        GestionBibliotheque gestion = new GestionBibliotheque(Bibliotheque.catalogueInitial());
        ProtocoleBibliotheque protocole = new ProtocoleBibliotheque(gestion);

        try (ServerSocket ecoute = creerSocketEcoute(port, args)) {
            System.out.println("Serveur à l'écoute sur " + ecoute.getLocalSocketAddress());

            while (true) {                                   // clients successifs, sans thread
                try (Socket client = ecoute.accept()) {
                    traiterClient(client, protocole);
                } catch (IOException e) {
                    System.err.println("Erreur avec un client : " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Impossible de démarrer le serveur : " + e.getMessage());
        }
    }

    private static ServerSocket creerSocketEcoute(int port, String[] args) throws IOException {
        if (args.length > 1) {
            return new ServerSocket(port, 50, InetAddress.getByName(args[1]));
        }
        return new ServerSocket(port);   // adresse générique : toutes les interfaces
    }

    private static void traiterClient(Socket client, ProtocoleBibliotheque protocole) throws IOException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
        BufferedWriter out = new BufferedWriter(
                new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8));

        String requete = in.readLine();
        System.out.println("[" + client.getRemoteSocketAddress() + "] requête : " + requete);

        if (requete == null) {            // fin de flux : fin d'échange (≠ ligne vide)
            System.out.println("Fin de flux sans requête, connexion abandonnée.");
            return;
        }
        String reponse = protocole.traiter(requete);
        out.write(reponse + "\n");
        out.flush();
        System.out.println("[" + client.getRemoteSocketAddress() + "] réponse : " + reponse);
    }   // la socket est fermée par le try-with-resources de l'appelant
}
