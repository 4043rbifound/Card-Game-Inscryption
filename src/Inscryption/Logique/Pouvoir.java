package Inscryption.Logique;

public abstract class Pouvoir {

    public abstract String getNom();

    public void auDebutTour(CarteLogic carte, Emplacement caseActuelle) {
    }

    public int auCalculAttaque(int degatsBruts, CarteLogic attaquant, CarteLogic cible) {
        return degatsBruts;
    }

    public void apresRecevoirDegats(CarteLogic cible, CarteLogic attaquant, int degatsRecus) {
    }

    public boolean auSacrifice(CarteLogic carteSacrifiee) {
        return false; // la carte meurt normalement
    }

    public void setPlateau(PlateauAccess plateau) {
    }

    public void auMouvement(CarteLogic carte, Emplacement caseActuelle) {
    }
}