// Couche d'acces a la passerelle HTTP. Le client ne connait pas le catalogue :
// il transmet une demande et exploite la reponse recue.

async function appeler(url) {
  let reponse;
  try {
    reponse = await fetch(url);
  } catch {
    return {
      statut: "COMMUNICATION",
      message: "Passerelle HTTP injoignable. Verifiez qu'elle est demarree (port 8081).",
    };
  }
  try {
    return await reponse.json();
  } catch {
    return { statut: "COMMUNICATION", message: "Reponse illisible de la passerelle." };
  }
}

export function listerLivres() {
  return appeler("/api/livres");
}

export function rechercherParTheme(theme) {
  return appeler("/api/recherche?theme=" + encodeURIComponent(theme));
}
