import { useState } from "react";
import { rechercherParTheme } from "./api.js";
import Entete from "./composants/Entete.jsx";
import Accueil from "./pages/Accueil.jsx";
import Catalogue from "./pages/Catalogue.jsx";
import RechercheTheme from "./pages/RechercheTheme.jsx";
import Resultats from "./pages/Resultats.jsx";

export default function App() {
  const [page, setPage] = useState("accueil");
  const [theme, setTheme] = useState("");
  const [resultat, setResultat] = useState(null);
  const [chargement, setChargement] = useState(false);

  async function rechercher(valeur) {
    setTheme(valeur.trim());
    setResultat(null);
    setChargement(true);
    setPage("resultats");
    setResultat(await rechercherParTheme(valeur));
    setChargement(false);
  }

  return (
    <div className="fenetre">
      <Entete page={page} aller={setPage} />

      {page === "accueil" && <Accueil aller={setPage} rechercher={rechercher} />}
      {page === "catalogue" && <Catalogue />}
      {page === "recherche" && <RechercheTheme rechercher={rechercher} />}
      {page === "resultats" && (
        <Resultats theme={theme} resultat={resultat} chargement={chargement} aller={setPage} />
      )}
    </div>
  );
}
