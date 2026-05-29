package Inscryption.Logique;

public class PlanJeu {

    private CarteLogic[][] m_sequencesApparition;

    public PlanJeu() {

        this.m_sequencesApparition = new CarteLogic[10][4];

        initialiserPlan();
    }

    private void initialiserPlan() {

        /*
         * Tour 1
         */
        this.m_sequencesApparition[0][0] = FabriqueCartes.creerLouveteau();
        this.m_sequencesApparition[0][2] = FabriqueCartes.creerMoineau();

        /*
         * Tour 2
         */
        this.m_sequencesApparition[1][1] = FabriqueCartes.creerLoup();

        /*
         * Tour 3
         */
        this.m_sequencesApparition[2][0] = FabriqueCartes.creerCorbeau();
        this.m_sequencesApparition[2][3] = FabriqueCartes.creerCoyote();

        /*
         * Tour 4
         */
        this.m_sequencesApparition[3][2] = FabriqueCartes.creerGrizzly();
    }

    public CarteLogic[] getCartesPourTour(int tour) {

        if (tour <= 0 || tour > this.m_sequencesApparition.length) {
            return new CarteLogic[4];
        }

        return this.m_sequencesApparition[tour - 1];
    }
}