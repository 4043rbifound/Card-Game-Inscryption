package Inscryption.Logique;

public class AdversaireLogic {

    private PlanJeu m_planJeu;

    public AdversaireLogic(PlanJeu plan) {
        this.m_planJeu = plan;
    }

    public CarteLogic[] obtenirIntention(int tour) {

        return this.m_planJeu.getCartesPourTour(tour);
    }

    public void jouerTour(int tour, PlateauLogic plateau) {

        CarteLogic[] cartesATour = this.m_planJeu.getCartesPourTour(tour);

        Emplacement[] casesAdversaire = plateau.getCasesAdversaire();

        for (int i = 0; i < cartesATour.length; i++) {

            CarteLogic carte = cartesATour[i];

            if (carte != null && casesAdversaire[i].estVide()) {
                casesAdversaire[i].placerCarte(carte);
            }
        }
    }
}