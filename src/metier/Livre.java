package metier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Livre {
    private final String id;
    private final String titre;
    private final List<String> themes;

    public Livre(String id, String titre, List<String> themes) {
        this.id = id;
        this.titre = titre;
        this.themes = Collections.unmodifiableList(new ArrayList<>(themes));
    }

    public String getId() { return id; }
    public String getTitre() { return titre; }
    public List<String> getThemes() { return themes; }

    @Override
    public String toString() {
        return id + " - " + titre + " " + themes;
    }
}
