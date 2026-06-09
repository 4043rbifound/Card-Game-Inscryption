package Inscryption.Logique;

import java.util.List;

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

        // MODIFICATION ICI : On utilise une List au lieu d'un tableau
        List<Emplacement> casesAdversaire = plateau.getCasesAdversaire();

        for (int i = 0; i < cartesATour.length; i++) {
            CarteLogic carte = cartesATour[i];

            // MODIFICATION ICI : On utilise .get(i) au lieu de [i]
            if (carte != null && casesAdversaire.get(i).estVide()) {
                casesAdversaire.get(i).placerCarte(carte);
            }
        }
    }
}