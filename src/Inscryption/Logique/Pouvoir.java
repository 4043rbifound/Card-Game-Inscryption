package Inscryption.Logique;

public abstract class Pouvoir {

    public abstract String getNom();

    public void auDebutTour(CarteAnimalLogic carte, Emplacement caseActuelle) {
    }

    public int auCalculAttaque(int degatsBruts, CarteAnimalLogic attaquant, CarteLogic cible, PlateauLogic plateau) {
        return degatsBruts;
    }

    public void apresRecevoirDegats(CarteAnimalLogic cible, CarteAnimalLogic attaquant, int degatsRecus, PlateauLogic plateau) {
    }

    public boolean auSacrifice(CarteAnimalLogic carteSacrifiee) {
        return false; // la carte meurt normalement
    }

    public void auMouvement(CarteAnimalLogic carte, Emplacement caseActuelle, PlateauLogic plateau) {
    }
}