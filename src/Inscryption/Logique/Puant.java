package Inscryption.Logique;

public class Puant extends Pouvoir {

    @Override
    public String getNom() {
        return "Puant";
    }

    @Override
    public void auDebutTour(CarteAnimalLogic carte, Emplacement caseActuelle) {
        Emplacement caseEnFace = caseActuelle.getCaseEnFace();

        if (caseEnFace != null && caseEnFace.estOccupee()) {
            CarteLogic cible = caseEnFace.getCarteContenue();
            int attaqueActuelle = cible.getPointsAttaque();
            cible.setAttaque(attaqueActuelle - 1);
        }
    }
}