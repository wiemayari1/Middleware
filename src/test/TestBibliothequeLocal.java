package test;

import metier.*;
import java.util.List;

public class TestBibliothequeLocal {
    public static void main(String[] args) {
        GestionBibliotheque g = new GestionBibliotheque(Bibliotheque.catalogueInitial());

        afficher("Liste de tous les livres (attendu : 5 livres)", g.listerLivres());
        afficher("Recherche de \"JAVA\" (attendu : L1, L3)", g.rechercherParTheme("JAVA"));
        afficher("Recherche de \" java \" (attendu : L1, L3)", g.rechercherParTheme(" java "));
        afficher("Recherche de \"HISTOIRE\" (attendu : liste vide)", g.rechercherParTheme("HISTOIRE"));

        System.out.println("== Recherche avec un thème vide (attendu : exception)");
        try {
            g.rechercherParTheme("   ");
            System.out.println("ECHEC : aucune exception");
        } catch (IllegalArgumentException e) {
            System.out.println("OK : IllegalArgumentException - " + e.getMessage());
        }
    }

    private static void afficher(String titre, List<Livre> livres) {
        System.out.println("== " + titre);
        System.out.println("   -> " + livres.size() + " livre(s)");
        for (Livre l : livres) System.out.println("   " + l);
    }
}
