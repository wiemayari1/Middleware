package metier;

import java.util.ArrayList;
import java.util.List;

/** Classe métier : aucune socket, aucun message réseau. */
public class GestionBibliotheque {
    private final Bibliotheque bibliotheque;

    public GestionBibliotheque(Bibliotheque bibliotheque) {
        this.bibliotheque = bibliotheque;
    }

    public List<Livre> listerLivres() {
        return new ArrayList<>(bibliotheque.getLivres());
    }

    public List<Livre> rechercherParTheme(String theme) {
        if (theme == null || theme.trim().isEmpty()) {
            throw new IllegalArgumentException("Thème null ou vide");
        }
        String recherche = theme.trim();
        List<Livre> resultat = new ArrayList<>();
        for (Livre livre : bibliotheque.getLivres()) {
            for (String t : livre.getThemes()) {
                if (t.equalsIgnoreCase(recherche)) {   // thème entier, sans tenir compte de la casse
                    resultat.add(livre);
                    break;
                }
            }
        }
        return resultat;
    }
}
