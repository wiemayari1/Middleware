import { useState } from "react";
import Icone from "../composants/Icones.jsx";

const ATOUTS = [
  { icone: "livre", classe: "lavande", titre: "Catalogue complet", texte: "Découvrez notre collection de livres." },
  { icone: "recherche", classe: "lavande", titre: "Recherche facile", texte: "Par thème, en quelques mots." },
  { icone: "losange", classe: "vert", titre: "Accès rapide", texte: "Trouvez les informations en quelques clics." },
  { icone: "wifi", classe: "bleu", titre: "Service en ligne", texte: "Consultez le catalogue depuis le réseau." },
];

export default function Accueil({ aller, rechercher }) {
  const [saisie, setSaisie] = useState("");

  function envoyer(e) {
    e.preventDefault();
    rechercher(saisie);
  }

  return (
      <>
        <section className="hero">
          <div className="hero-contenu">
            <h1>
              Bienvenue à la
              <br />
              Bibliothèque Universitaire
            </h1>
            <p>Découvrez notre catalogue de livres et explorez nos ressources par thème.</p>

            <form className="barre" onSubmit={envoyer}>
              <Icone nom="recherche" taille={18} />
              <input
                  type="text"
                  value={saisie}
                  onChange={(e) => setSaisie(e.target.value)}
                  placeholder="Rechercher un livre par thème..."
                  aria-label="Thème recherché"
              />
              <button type="submit" className="btn-pilule">
                Rechercher
              </button>
            </form>

            <div className="actions">
              <button type="button" className="btn-plein" onClick={() => aller("catalogue")}>
                <Icone nom="livre" taille={18} />
                Consulter le catalogue
              </button>
              <button type="button" className="btn-contour" onClick={() => aller("recherche")}>
                <Icone nom="grille" taille={18} />
                Rechercher par thème
              </button>
            </div>
          </div>
        </section>

        <section className="atouts">
          {ATOUTS.map((a) => (
              <div className="atout" key={a.titre}>
            <span className={"atout-icone " + a.classe}>
              <Icone nom={a.icone} taille={22} />
            </span>
                <div>
                  <h3>{a.titre}</h3>
                  <p>{a.texte}</p>
                </div>
              </div>
          ))}
        </section>
      </>
  );
}