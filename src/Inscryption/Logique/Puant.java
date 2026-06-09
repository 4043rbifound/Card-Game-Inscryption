package Inscryption.Logique;

public class Puant extends Pouvoir {

    @Override
    public String getNom() {
        return "Puant";
    }

    @Override
    public int auCalculAttaque(int degatsBruts, CarteLogic attaquant, CarteLogic cible) {
        return Math.max(0, degatsBruts - 1);
    }
}