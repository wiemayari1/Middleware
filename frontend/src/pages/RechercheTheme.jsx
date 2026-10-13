import { useState } from "react";

export default function RechercheTheme({ rechercher }) {
  const [theme, setTheme] = useState("");

  function envoyer(e) {
    e.preventDefault();
    rechercher(theme);
  }

  return (
      <>
        <section className="banniere">
          <div>
            <h1>Recherche par thème</h1>
            <p>Saisissez un thème pour consulter les livres associés.</p>
          </div>
        </section>

        <section className="contenu">
          <form className="carte-formulaire" onSubmit={envoyer}>
            <label htmlFor="theme">Choisir un thème</label>
            <div className="ligne-formulaire">
              <input
                  id="theme"
                  type="text"
                  value={theme}
                  onChange={(e) => setTheme(e.target.value)}
                  placeholder="Saisir un thème, par exemple JAVA"
              />
              <button type="submit" className="btn-pilule">
                Rechercher
              </button>
            </div>
          </form>
        </section>
      </>
  );
}