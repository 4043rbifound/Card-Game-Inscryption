package Inscryption.Logique;

/**
 * Pouvoir Coureur : après son attaque, la carte se déplace vers la droite.
 * Si la droite est bloquée, elle tente la gauche.
 * Si les deux sont bloquées, elle reste sur place.
 *
 * Le déplacement est délégué à PlateauLogic via le hook auMouvement,
 * mais Coureur ne stocke PAS de référence au plateau :
 * c'est PlateauLogic.avancerMouvements() qui injecte la case adjacente.
 */
public class Coureur extends Pouvoir {

    @Override
    public String getNom() {
        return "Coureur";
    }

    /**
     * Reçoit la case adjacente calculée par PlateauLogic.
     * Si elle est non-nulle et vide, effectue le déplacement.
     *
     * @param carte        la carte qui se déplace
     * @param caseActuelle l'emplacement actuel de la carte
     * @param caseAdjacente l'emplacement adjacent fourni par le plateau (null si aucun)
     */
    @Override
    public void auMouvement(CarteLogic carte, Emplacement caseActuelle, Emplacement caseAdjacente) {
        if (caseAdjacente != null && caseAdjacente.estVide()) {
            caseActuelle.liberer();
            caseAdjacente.placerCarte(carte);
        }
    }
}