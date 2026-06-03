package Inscryption.Affichage;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import Inscryption.Logique.*;

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
        // Main de départ standard
        m_joueur.piocherCartePrincipal();
        m_joueur.piocherEcureuil();

        while (!this.m_score.estPartieTerminee()) {
            // 1. Effets de début de tour (Croissance, etc.)
            this.m_plateau.AppliquerEffetsDebutTour(true);
            this.m_plateau.AppliquerEffetsDebutTour(false);

            // 2. Récupération des intentions de l'IA
            CarteLogic[] intentions = this.m_adversaire.obtenirIntention(this.m_tour);

            // 3. Affichage avant la pioche
            this.m_vue.afficherEtatJeu(this.m_plateau, this.m_joueur, this.m_score, this.m_tour, intentions);

            // 4. PHASE DE PIOCHE OBLIGATOIRE
            phaseDePioche();

            // 5. Phase d'action (Placer des cartes / Fin)
            boucleTourJoueur();

            // 6. L'adversaire joue
            this.m_adversaire.jouerTour(this.m_tour, this.m_plateau);

            System.out.println("\n--- PHASE DE COMBAT ---");
            List<String> logsJoueur = this.m_plateau.resoudreAttaques(true, this.m_score);
            this.m_vue.afficherMessages(logsJoueur);

            List<String> logsAdversaire = this.m_plateau.resoudreAttaques(false, this.m_score);
            this.m_vue.afficherMessages(logsAdversaire);

            m_tour++;
        }

        System.out.println("\n============================");
        System.out.println("FIN DE LA PARTIE ! Vainqueur : " + this.m_score.getGagnant());
        System.out.println("============================");
    }

    private void phaseDePioche() {
        Scanner scanner = new Scanner(System.in);
        boolean piocheFaite = false;

        while (!piocheFaite) {
            System.out.println("\n📥 PHASE DE PIOCHE ! Choisissez votre carte pour ce tour :");
            System.out.println("1. Piocher dans le DECK principal");
            System.out.println("2. Piocher un ÉCUREUIL gratuit");
            System.out.print("Votre choix (1 ou 2) : ");

            String choix = scanner.nextLine().trim();
            if (choix.equals("1")) {
                this.m_joueur.piocherCartePrincipal();
                piocheFaite = true;
            } else if (choix.equals("2")) {
                this.m_joueur.piocherEcureuil();
                piocheFaite = true;
            } else {
                System.out.println("Choix invalide.");
            }
        }
        // On réaffiche le jeu mis à jour avec la nouvelle carte en main
        this.m_vue.afficherPlateau(this.m_plateau);
        this.m_vue.afficherMain(this.m_joueur);
    }

    private void boucleTourJoueur() {
        boolean tourTermine = false;

        while (!tourTermine) {
            String saisie = this.m_vue.lireSaisie();
            Commande commande = this.m_analyseur.interpreter(saisie);

            if (commande == null) {
                System.out.println("Commande invalide. Exemples: 'placer 1 0' ou 'fin'");
                continue;
            }

            String action = commande.getAction();

            if (action.equals("fin")) {
                tourTermine = true;
            }
            else if (action.equals("placer")) {
                int indexMainZeroBased = commande.getIndexCarteMain() - 1;
                int positionCible = commande.getPositionPlateau();

                // Validations de base
                if (indexMainZeroBased < 0 || indexMainZeroBased >= m_joueur.getMain().length || m_joueur.getMain()[indexMainZeroBased] == null) {
                    System.out.println("Index de carte invalide dans votre main.");
                    continue;
                }
                if (positionCible < 0 || positionCible >= m_plateau.getCasesJoueur().size()) {
                    System.out.println("Position sur le plateau invalide (0 à 3).");
                    continue;
                }

                List<Emplacement> casesJoueur = m_plateau.getCasesJoueur();
                if (!casesJoueur.get(positionCible).estVide()) {
                    System.out.println("Cette case est déjà occupée !");
                    continue;
                }

                CarteAnimalLogic carteAPlacer = m_joueur.getMain()[indexMainZeroBased];
                int coutSang = carteAPlacer.getCoutSang(); // Supposant que vous avez getCout() dans votre classe Carte

                // GESTION DES SACRIFICES
                if (coutSang > 0) {
                    int cartesDisponibles = 0;
                    for (Emplacement emp : casesJoueur) {
                        if (!emp.estVide()) cartesDisponibles++;
                    }

                    if (cartesDisponibles < coutSang) {
                        System.out.println("❌ Pas assez de créatures sur le plateau pour payer le coût en sang (" + coutSang + "🩸 requis).");
                        continue;
                    }

                    // On demande à l'utilisateur quelles cartes sacrifier
                    System.out.println("🩸 " + carteAPlacer.getNom() + " nécessite " + coutSang + " sacrifice(s).");
                    Scanner sacScanner = new Scanner(System.in);
                    List<Integer> positionsSacrifiees = new ArrayList<>();
                    boolean sacrificesValides = true;

                    for (int i = 0; i < coutSang; i++) {
                        System.out.print("Indiquez la position (0 à 3) de la créature n°" + (i + 1) + " à sacrifier : ");
                        try {
                            int posSac = Integer.parseInt(sacScanner.nextLine().trim());
                            if (posSac < 0 || posSac >= 4 || casesJoueur.get(posSac).estVide() || positionsSacrifiees.contains(posSac)) {
                                System.out.println("Position invalide ou case vide. Annulation du placement.");
                                sacrificesValides = false;
                                break;
                            }
                            positionsSacrifiees.add(posSac);
                        } catch (Exception e) {
                            sacrificesValides = false;
                            break;
                        }
                    }

                    if (!sacrificesValides) continue;

                    // On applique les sacrifices
                    for (int posSac : positionsSacrifiees) {
                        System.out.println("💀 Sacrifice de " + casesJoueur.get(posSac).getCarteContenue().getNom());
                        casesJoueur.get(posSac).liberer();
                    }
                }

                // Pose finale de la carte
                casesJoueur.get(positionCible).placerCarte(carteAPlacer);
                m_joueur.getMain()[indexMainZeroBased] = null;

                System.out.println("✅ Placement de " + carteAPlacer.getNom() + " en position " + positionCible);

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