import { useEffect, useState } from "react";
import { listerLivres } from "../api.js";
import Couverture from "../composants/Couverture.jsx";
import Alerte from "../composants/Alerte.jsx";

export default function Catalogue() {
  const [resultat, setResultat] = useState(null);

  // Consulter : une demande LISTE est envoyée à l'ouverture de la page.
  useEffect(() => {
    let annule = false;
    listerLivres().then((r) => {
      if (!annule) setResultat(r);
    });
    return () => {
      annule = true;
    };
  }, []);

  const livres = resultat && resultat.statut === "OK" ? resultat.livres : null;

  return (
      <>
        <section className="banniere">
          <div>
            <h1>Catalogue des livres</h1>
            <p>Voici la liste des livres disponibles dans notre bibliothèque.</p>
          </div>
          {livres && (
              <span className="badge badge-clair">
            {livres.length} livre{livres.length > 1 ? "s" : ""}
          </span>
          )}
        </section>

        <section className="contenu">
          {!resultat && <p className="discret">Interrogation du serveur...</p>}
          <Alerte resultat={resultat} />

          {livres && (
              <div className="grille-livres">
                {livres.map((l) => (
                    <article className="carte-livre" key={l.id}>
                      <Couverture />
                      <h3>{l.titre}</h3>
                      <p className="id">ID : {l.id}</p>
                    </article>
                ))}
              </div>
          )}
        </section>
      </>
  );
}