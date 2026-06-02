package Inscryption.Affichage; // Calé sur votre package racine

import java.util.List;
import java.util.ArrayList;
import Inscryption.Logique.*;
import Inscryption.Affichage.InterfaceConsole;

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
        // Distribution initiale pour les tests console
        m_joueur.piocherCartePrincipal();
        m_joueur.piocherCartePrincipal();
        m_joueur.piocherEcureuil();

        while (!this.m_score.estPartieTerminee()) {
            // 1. Déclenchement des effets de début de tour (Ex: Croissance)
            this.m_plateau.AppliquerEffetsDebutTour(true);
            this.m_plateau.AppliquerEffetsDebutTour(false);

            // 2. Récupération des intentions de l'IA
            CarteLogic[] intentions = this.m_adversaire.obtenirIntention(this.m_tour);

            // 3. Affichage de l'état global du jeu
            this.m_vue.afficherEtatJeu(
                    this.m_plateau,
                    this.m_joueur,
                    this.m_score,
                    this.m_tour,
                    intentions);

            // 4. Phase d'action du Joueur
            boucleTourJoueur();

            // 5. Déploiement des cartes prévues par l'IA
            this.m_adversaire.jouerTour(this.m_tour, this.m_plateau);

            System.out.println("\n--- PHASE DE COMBAT ---");

            // 6. FIX : Résolution des attaques stockée dans une List<String>
            List<String> logsJoueur = this.m_plateau.resoudreAttaques(true, this.m_score);
            this.m_vue.afficherMessages(logsJoueur);

            // 7. FIX : Même chose pour l'adversaire
            List<String> logsAdversaire = this.m_plateau.resoudreAttaques(false, this.m_score);
            this.m_vue.afficherMessages(logsAdversaire);

            // Passage au tour suivant
            this.m_tour++;
        }

        System.out.println("\n============================");
        System.out.println("FIN DE LA PARTIE !");
        System.out.println("Vainqueur : " + this.m_score.getGagnant());
        System.out.println("============================");
    }

    private void boucleTourJoueur() {
        boolean tourTermine = false;

        while (!tourTermine) {
            String saisie = this.m_vue.lireSaisie();
            Commande commande = this.m_analyseur.interpreter(saisie);

            if (commande == null) {
                System.out.println("Commande invalide. Exemple: 'placer 1 0', 'piocher' ou 'fin'");
                continue;
            }

            String action = commande.getAction();

            if (action.equals("fin")) {
                tourTermine = true;
            }
            else if (action.equals("piocher")) {
                this.m_joueur.piocherCartePrincipal();
                this.m_vue.afficherPlateau(this.m_plateau);
                this.m_vue.afficherMain(this.m_joueur);
            }
            else if (action.equals("placer")) {
                int indexMainZeroBased = commande.getIndexCarteMain() - 1;
                int position = commande.getPositionPlateau();

                if (indexMainZeroBased < 0 || indexMainZeroBased >= m_joueur.getMain().length || m_joueur.getMain()[indexMainZeroBased] == null) {
                    System.out.println("Index de carte invalide dans votre main.");
                    continue;
                }
                if (position < 0 || position >= m_plateau.getCasesJoueur().size()) {
                    System.out.println("Position sur le plateau invalide (0 à 3).");
                    continue;
                }

                List<Emplacement> casesJoueur = m_plateau.getCasesJoueur();
                if (!casesJoueur.get(position).estVide()) {
                    System.out.println("Cette case est déjà occupée !");
                    continue;
                }

                CarteAnimalLogic carteAPlacer = m_joueur.getMain()[indexMainZeroBased];
                casesJoueur.get(position).placerCarte(carteAPlacer);
                m_joueur.getMain()[indexMainZeroBased] = null;

                System.out.println("Placement de " + carteAPlacer.getNom() + " en position " + position);

                this.m_vue.afficherPlateau(this.m_plateau);
                this.m_vue.afficherMain(this.m_joueur);
            }
        }
    }

    public static void main(String[] args) {
        GestionnairePartie gestionnaire = new GestionnairePartie();
        gestionnaire.executerJeu();
    }
}