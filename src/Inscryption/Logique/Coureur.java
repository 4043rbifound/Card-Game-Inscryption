package Inscryption.Logique;

/**
 * Pouvoir Coureur : après son attaque, la carte se déplace vers la droite.
 * Si la droite est bloquée, elle tente la gauche.
 * Si les deux sont bloquées, elle reste sur place.
 */
public class Coureur extends Pouvoir {
    private PlateauAccess plateau;

    @Override
    public String getNom() {
        return "Coureur";
    }

    @Override
    public void setPlateau(PlateauAccess plateau) {
        this.plateau = plateau;
    }

    @Override
    public void auMouvement(CarteLogic carte, Emplacement caseActuelle) {
        if (this.plateau != null) {
            Emplacement caseAdjacente = this.plateau.trouverCaseAdjacente(caseActuelle);
            if (caseAdjacente != null && caseAdjacente.estVide()) {
                caseActuelle.liberer();
                caseAdjacente.placerCarte(carte);
            }
        }
    }
}