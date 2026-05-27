package Inscryption.Affichage;


import inscryption.logique.AdversaireLogic;
import inscryption.logique.CarteLogic;
import inscryption.logique.JoueurLogic;
import inscryption.logique.PlanJeu;
import inscryption.logique.PlateauLogic;
import inscryption.logique.ScoreLogic;

public class GestionnairePartie {

    private PlateauLogic m_plateau;
    private JoueurLogic m_joueur;
    private AdversaireLogic m_adversaire;
    private ScoreLogic m_score;

    private InterfaceConsole m_vue;
    private AnalyseurSaisie m_analyseur;

    private int m_tour;

    public GestionnairePartie() {

        this.m_plateau = new PlateauLogic();
        this.m_joueur = new JoueurLogic();
        this.m_adversaire = new AdversaireLogic(new PlanJeu());
        this.m_score = new ScoreLogic();

        this.m_vue = new InterfaceConsole();
        this.m_analyseur = new AnalyseurSaisie();

        this.m_tour = 1;
    }

    public void executerJeu() {

        while (!this.m_score.estPartieTerminee()) {

            CarteLogic[] intentions = this.m_adversaire.obtenirIntention(this.m_tour);

            this.m_vue.afficherEtatJeu(
                    this.m_plateau,
                    this.m_joueur,
                    this.m_score,
                    this.m_tour,
                    intentions);

            boucleTourJoueur();

            this.m_adversaire.jouerTour(this.m_tour, this.m_plateau);

            this.m_tour++;
        }

        System.out.println();
        System.out.println("Partie terminée !");
        System.out.println("Gagnant : " + this.m_score.getGagnant());
    }

    private void boucleTourJoueur() {

        boolean tourTermine = false;

        while (!tourTermine) {

            String saisie = this.m_vue.lireSaisie();

            Commande commande = this.m_analyseur.interpreter(saisie);

            if (commande == null) {
                System.out.println("Commande invalide.");
                continue;
            }

            String action = commande.getAction();

            if (action.equals("fin")) {

                tourTermine = true;
            }

            else if (action.equals("piocher")) {

                this.m_joueur.piocherCartePrincipal();
                System.out.println("Carte piochée.");
            }

            else if (action.equals("placer")) {

                int indexCarte = commande.getIndexCarteMain();
                int position = commande.getPositionPlateau();

                System.out.println(
                        "Placement de la carte "
                                + indexCarte
                                + " en position "
                                + position);

                /*
                 * Ici vous ajouterez plus tard :
                 * - récupération de la carte
                 * - vérification du coût
                 * - placement sur le plateau
                 */
            }
        }
    }

    public static void main(String[] args) {

        GestionnairePartie gestionnaire = new GestionnairePartie();
        gestionnaire.executerJeu();
    }
}