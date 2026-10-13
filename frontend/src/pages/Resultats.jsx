import Icone from "../composants/Icones.jsx";
import Couverture from "../composants/Couverture.jsx";
import Alerte from "../composants/Alerte.jsx";

export default function Resultats({ theme, resultat, chargement, aller }) {
  const livres = resultat && resultat.statut === "OK" ? resultat.livres : null;

  return (
    <section className="contenu">
      <button type="button" className="retour" onClick={() => aller("recherche")}>
        <Icone nom="retour" taille={16} />
        Retour
      </button>

      <div className="titre-ligne">
        <h1>Résultats de la recherche par thème</h1>
        {livres && (
          <span className="badge">
            {livres.length} livre{livres.length > 1 ? "s" : ""} trouvé{livres.length > 1 ? "s" : ""}
          </span>
        )}
      </div>

      <span className="pastille">Thème : {theme || "(vide)"}</span>

      {chargement && <p className="discret marge">Interrogation du serveur...</p>}
      {!chargement && <Alerte resultat={resultat} />}

      {!chargement && livres && livres.length === 0 && (
        <p className="discret marge">Aucun livre ne correspond à ce thème.</p>
      )}

      {!chargement && livres && livres.length > 0 && (
        <div className="liste-resultats">
          {livres.map((l) => (
            <article className="ligne-livre" key={l.id}>
              <Couverture petite />
              <div>
                <h3>{l.titre}</h3>
                <p className="id">ID : {l.id}</p>
              </div>
            </article>
          ))}
        </div>
      )}
    </section>
  );
}
