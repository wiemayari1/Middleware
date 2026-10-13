import Icone from "./Icones.jsx";

export default function Couverture({ petite = false }) {
  return (
    <div className={"couverture" + (petite ? " petite" : "")}>
      <Icone nom="livre" taille={petite ? 22 : 30} />
    </div>
  );
}
