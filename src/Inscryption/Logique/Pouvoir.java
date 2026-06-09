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

    public void auMouvement(CarteLogic carte, Emplacement caseActuelle) {
    }

    /**
     * Variante de auMouvement avec la case adjacente pré-calculée par PlateauLogic.
     * Les pouvoirs de déplacement (ex: Coureur) surchargent CETTE méthode
     * pour éviter de dépendre directement de PlateauLogic.
     *
     * @param carte         la carte qui se déplace
     * @param caseActuelle  l’emplacement actuel de la carte
     * @param caseAdjacente l’emplacement adjacent calculé par le plateau (null si aucun)
     */
    public void auMouvement(CarteLogic carte, Emplacement caseActuelle, Emplacement caseAdjacente) {
    }
}