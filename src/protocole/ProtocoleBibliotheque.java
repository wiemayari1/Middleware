package protocole;

import metier.*;
import java.util.List;

/**
 * Interprétation des messages : transforme une ligne de requête en appel
 * à GestionBibliotheque, puis construit la ligne de réponse.
 * N'ouvre aucune socket.
 */
public class ProtocoleBibliotheque {
    private static final String ERR_INCONNUE = "ERR;COMMANDE_INCONNUE";
    private static final String ERR_INVALIDE = "ERR;REQUETE_INVALIDE";

    private final GestionBibliotheque gestion;

    public ProtocoleBibliotheque(GestionBibliotheque gestion) {
        this.gestion = gestion;
    }

    public String traiter(String ligne) {
        if (ligne == null || ligne.trim().isEmpty()) {
            return ERR_INVALIDE;
        }
        // -1 : conserve les champs vides finaux ("RECHERCHE;" -> 2 champs)
        String[] champs = ligne.split(";", -1);
        for (int i = 0; i < champs.length; i++) champs[i] = champs[i].trim();

        String commande = champs[0];
        if (commande.isEmpty()) {
            return ERR_INVALIDE;
        }

        switch (commande) {
            case "LISTE":
                if (champs.length != 1) return ERR_INVALIDE;
                return formater(gestion.listerLivres());

            case "RECHERCHE":
                if (champs.length != 2) return ERR_INVALIDE;
                String theme = champs[1];
                if (theme.isEmpty() || contientCaractereReserve(theme)) return ERR_INVALIDE;
                try {
                    return formater(gestion.rechercherParTheme(theme));
                } catch (IllegalArgumentException e) {
                    return ERR_INVALIDE;
                }

            default:
                return ERR_INCONNUE;
        }
    }

    /** ';' ',' ':' et retours à la ligne sont réservés à la structure des messages. */
    private boolean contientCaractereReserve(String s) {
        return s.indexOf(';') >= 0 || s.indexOf(':') >= 0 || s.indexOf(',') >= 0
                || s.indexOf('\n') >= 0 || s.indexOf('\r') >= 0;
    }

    /** OK;nombre;id:titre;id:titre;... */
    private String formater(List<Livre> livres) {
        StringBuilder sb = new StringBuilder("OK;").append(livres.size());
        for (Livre l : livres) {
            sb.append(';').append(l.getId()).append(':').append(l.getTitre());
        }
        return sb.toString();
    }
}
