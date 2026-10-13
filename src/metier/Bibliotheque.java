package metier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Bibliotheque {
    private final String nom;
    private final List<Livre> livres = new ArrayList<>();

    public Bibliotheque(String nom) {
        this.nom = nom;
    }

    public void ajouterLivre(Livre livre) {
        livres.add(livre);
    }

    public String getNom() { return nom; }

    /** Vue non modifiable, dans l'ordre du catalogue. */
    public List<Livre> getLivres() {
        return Collections.unmodifiableList(livres);
    }

    /** Catalogue initial du TP. */
    public static Bibliotheque catalogueInitial() {
        Bibliotheque b = new Bibliotheque("Bibliothèque universitaire");
        b.ajouterLivre(new Livre("L1", "Programmation Java", Arrays.asList("JAVA", "POO")));
        b.ajouterLivre(new Livre("L2", "Réseaux informatiques", Arrays.asList("RESEAUX", "TCP")));
        b.ajouterLivre(new Livre("L3", "Systèmes distribués", Arrays.asList("JAVA", "DISTRIBUE")));
        b.ajouterLivre(new Livre("L4", "Bases de données", Arrays.asList("SQL", "DONNEES")));
        b.ajouterLivre(new Livre("L5", "Architecture logicielle", Arrays.asList("ARCHITECTURE", "POO")));
        return b;
    }
}
