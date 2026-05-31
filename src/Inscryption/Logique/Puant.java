package Inscryption.Logique;

public class Puant extends Pouvoir {

    @Override
    public String getNom() {
        return "Puant";
    }

    @Override
    public int auCalculAttaque(int degatsBruts, CarteAnimalLogic attaquant, CarteLogic cible, PlateauLogic plateau) {
        if (cible != null && !cible.estObstacle()) {
            CarteAnimalLogic animalCible = (CarteAnimalLogic) cible;

            for (Pouvoir p : animalCible.getPouvoirs()) {
                if (p.getNom().equalsIgnoreCase("Puant")) {
                    return Math.max(0, degatsBruts - 1);
                }
            }
        }
        return degatsBruts;
    }
}