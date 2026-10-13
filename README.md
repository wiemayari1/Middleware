# TP1 - Bibliothèque universitaire (client-serveur TCP)

## Architecture

```
src/
  metier/       Livre, Bibliotheque, GestionBibliotheque
  protocole/    ProtocoleBibliotheque
  reseau/       ServeurBibliothequeTCP, ClientBibliothequeTCP
  passerelle/   PasserelleHttp (pont HTTP -> TCP pour l'interface web)
  test/         TestBibliothequeLocal
frontend/       Interface React (Vite)
```

Dépendances : reseau -> protocole -> metier ; test -> metier.
Code compatible Java 8 ou supérieur.

## Exécution dans IntelliJ

1. Ouvrir le dossier comme projet ; `src` est la racine des sources.
2. Validation locale (Ex. 1) : lancer `test.TestBibliothequeLocal`.
3. Serveur : lancer `reseau.ServeurBibliothequeTCP` (port 5000 par défaut).
4. Client console : lancer `reseau.ClientBibliothequeTCP`
   (arguments facultatifs : adresse port requete, ex. `127.0.0.1 5000 "RECHERCHE;JAVA"`).

## Interface web

Trois processus à lancer, dans cet ordre :

1. `reseau.ServeurBibliothequeTCP` (port 5000)
2. `passerelle.PasserelleHttp` (port HTTP 8081 -> serveur TCP 127.0.0.1:5000)
   arguments facultatifs : `portHttp hoteServeur portServeur`
3. Interface React :

```
cd frontend
npm install
npm run dev
```

Ouvrir http://localhost:5173

Si le port 8081 est occupé, changer l'argument de la passerelle et le port du proxy
dans `frontend/vite.config.js`.

## Ligne de commande (PowerShell)

```
Get-ChildItem -Recurse src -Filter *.java | ForEach-Object { $_.FullName } | Out-File -Encoding ascii sources.txt
javac -encoding UTF-8 -d out "@sources.txt"
java -cp out reseau.ServeurBibliothequeTCP 5000
java -cp out reseau.ClientBibliothequeTCP 127.0.0.1 5000 "LISTE"
```

## Protocole

| Requête | Réponse |
|---|---|
| `LISTE` | `OK;nombre;id:titre;...` |
| `RECHERCHE;theme` | `OK;nombre;id:titre;...` ou `OK;0` |
| ligne vide, mauvais nombre d'arguments, caractère réservé | `ERR;REQUETE_INVALIDE` |
| commande inconnue | `ERR;COMMANDE_INCONNUE` |

Une connexion transporte une requête et une réponse (UTF-8, ligne terminée par `\n`).
