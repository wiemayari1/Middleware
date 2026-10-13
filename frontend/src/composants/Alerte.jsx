const MESSAGES = {
  REQUETE_INVALIDE:
    "Requête invalide : le thème est vide ou contient un caractère réservé (; : ,).",
  COMMANDE_INCONNUE: "Commande inconnue du serveur.",
};

// Distingue une erreur du serveur (métier ou protocole) d'un échec de communication.
export default function Alerte({ resultat }) {
  if (!resultat) return null;

  if (resultat.statut === "COMMUNICATION") {
    return (
      <div className="alerte alerte-comm">
        <strong>Échec de communication.</strong> {resultat.message}
      </div>
    );
  }
  if (resultat.statut === "ERREUR") {
    return (
      <div className="alerte alerte-erreur">
        <strong>Erreur du serveur ({resultat.code}).</strong>{" "}
        {MESSAGES[resultat.code] || ""}
      </div>
    );
  }
  return null;
}
