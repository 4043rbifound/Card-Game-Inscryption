package Inscryption.Logique;

public class ScoreLogic {

    private int m_valeurRelative;

    public ScoreLogic() {
        this.m_valeurRelative = 0;
    }

    public void ajouterPointsJoueur(int points) {
        this.m_valeurRelative += points;
    }

    public void ajouterPointsAdversaire(int points) {
        this.m_valeurRelative -= points;
    }

    public int getValeurRelative() {
        return this.m_valeurRelative;
    }

    public boolean estPartieTerminee() {

        return this.m_valeurRelative >= 5
                || this.m_valeurRelative <= -5;
    }

    public String getGagnant() {

        if (this.m_valeurRelative >= 5) {
            return "Joueur";
        }

        if (this.m_valeurRelative <= -5) {
            return "Adversaire";
        }

        return null;
    }
}
