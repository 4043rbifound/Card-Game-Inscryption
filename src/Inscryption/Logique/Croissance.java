package Inscryption.Logique;

public class Croissance extends Pouvoir {

    @Override
    public String getNom() {
        return "Croissance";
    }

    @Override
    public void auDebutTour(CarteLogic carte, Emplacement caseActuelle) {
        if (carte.getToursSurPlateau() == 2) {
            carte.setAttaque(3);
            carte.setPv(2);
            carte.setPvMax(2);
        }
    }
}