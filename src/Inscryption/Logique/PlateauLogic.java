package Inscryption.Logique;

public class PlateauLogic {

    private Emplacement[] m_casesJoueur;
    private Emplacement[] m_casesAdversaire;

    public PlateauLogic() {

        this.m_casesJoueur = new Emplacement[4];
        this.m_casesAdversaire = new Emplacement[4];

        for (int i = 0; i < 4; i++) {
            this.m_casesJoueur[i] = new Emplacement(i);
            this.m_casesAdversaire[i] = new Emplacement(i);
        }
    }

    public Emplacement[] getCasesJoueur() {
        return this.m_casesJoueur;
    }

    public Emplacement[] getCasesAdversaire() {
        return this.m_casesAdversaire;
    }

    public String[] resoudreAttaques(boolean campJoueur, ScoreLogic score) {

        String[] logs = new String[20];
        int indexLog = 0;

        Emplacement[] attaquants = campJoueur ? m_casesJoueur : m_casesAdversaire;
        Emplacement[] defenseurs = campJoueur ? m_casesAdversaire : m_casesJoueur;

        for (int i = 0; i < 4; i++) {

            if (attaquants[i].estVide()) {
                continue;
            }

            CarteAnimalLogic attaquant = (CarteAnimalLogic) attaquants[i].getCarteContenue();

            if (attaquant == null) {
                continue;
            }

            int degats = attaquant.getPointsAttaque();

            CarteLogic cible = defenseurs[i].getCarteContenue();

            if (attaquant.estVolant() || cible == null) {

                if (campJoueur) {
                    score.ajouterPointsJoueur(degats);
                } else {
                    score.ajouterPointsAdversaire(degats);
                }

                logs[indexLog++] = attaquant.getNom() + " inflige " + degats + " au score";
            }
            else {

                CarteLogic cibleAnimale = cible;

                cibleAnimale.recevoirDegats(degats);

                logs[indexLog++] = attaquant.getNom() + " attaque " + cibleAnimale.getNom() + " pour " + degats;

                if (cibleAnimale.estMorte()) {
                    defenseurs[i].liberer();
                    logs[indexLog++] = cibleAnimale.getNom() + " est mort";
                }
            }
        }

        return logs;
    }

    public void avancerMouvements(boolean campJoueur) {
        // Simplifié pour phase 1 : pas de déplacement
    }

    public void appliquerEffetsDebutTour(boolean campJoueur) {
        // Effets des pouvoirs (phase 2)
    }

    public Emplacement trouverCaseAdjacente(Emplacement caseActuelle) {
        return caseActuelle;
    }
}

