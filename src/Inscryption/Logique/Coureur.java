package Inscryption.Logique;

public class Coureur extends Pouvoir {

    @Override
    public String getNom() {
        return "Coureur";
    }

    @Override
    public void auMouvement(CarteLogic carte, Emplacement caseActuelle, PlateauLogic plateau) {
        Emplacement caseSuivante = plateau.trouverCaseAdjacente(caseActuelle);

        if (caseSuivante != null && caseSuivante.estVide()) {
            caseActuelle.liberer();
            caseSuivante.placerCarte(carte);
        }
    }
}