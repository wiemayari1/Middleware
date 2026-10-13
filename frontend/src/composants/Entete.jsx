import Icone from "./Icones.jsx";

const LIENS = [
  ["accueil", "Accueil"],
  ["catalogue", "Catalogue"],
  ["recherche", "Recherche par thème"],
];

export default function Entete({ page, aller }) {
  const active = page === "resultats" ? "recherche" : page;

  return (
    <header className="entete">
      <button type="button" className="logo" onClick={() => aller("accueil")}>
        <span className="logo-icone">
          <Icone nom="livre" taille={26} />
        </span>
        <span>Bibliothèque Universitaire</span>
      </button>

      <nav className="navigation">
        {LIENS.map(([cle, libelle]) => (
          <button
            key={cle}
            type="button"
            className={"lien" + (active === cle ? " actif" : "")}
            onClick={() => aller(cle)}
          >
            {libelle}
          </button>
        ))}
      </nav>

      <button
        type="button"
        className="bouton-icone"
        aria-label="Recherche par thème"
        onClick={() => aller("recherche")}
      >
        <Icone nom="recherche" taille={18} />
      </button>
    </header>
  );
}
