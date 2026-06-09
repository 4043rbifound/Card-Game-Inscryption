package Inscryption.Affichage;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import Inscryption.Logique.*;

public class GestionnairePartie {

    private PlateauLogic m_plateau;
    private JoueurLogic m_joueur;
    private AdversaireLogic m_adversaire;
    private ScoreLogic m_score;

    private InterfaceConsole m_vue;
    private AnalyseurSaisie m_analyseur;
    private int m_tour;
    private int m_partieActuelle;

    public GestionnairePartie() {
        this.m_joueur = new JoueurLogic();
        this.m_adversaire = new AdversaireLogic(new PlanJeu());
        this.m_vue = new InterfaceConsole();
        this.m_analyseur = new AnalyseurSaisie();
    }

    public void executerJeu() {
        int victoires = 0;
        for (int partie = 1; partie <= 3; partie++) {
            this.m_partieActuelle = partie;
            boolean gagne = executerPartie(partie);
            if (gagne) {
                victoires++;
                this.m_vue.afficherMessageSimple("\n--- VICTOIRE DE LA PARTIE " + partie + " ---");
            } else {
                this.m_vue.afficherMessageSimple("\n--- DEFAITE ---");
                this.m_vue.afficherMessageSimple("Vous avez perdu la partie " + partie + ". Partie terminee.");
                return;
            }
        }

        this.m_vue.afficherMessageSimple("\n==================================================");
        this.m_vue.afficherMessageSimple("FELICITATIONS ! Vous avez remporte les 3 parties !");
        this.m_vue.afficherMessageSimple("==================================================");
    }

    private boolean executerPartie(int partie) {
        // Initialisation de la partie
        this.m_plateau = new PlateauLogic();
        // Rocher obstacle sur B2 (index 1)
        this.m_plateau.getCasesJoueur().get(1).placerCarte(FabriqueCartes.creerRocher());
        
        this.m_joueur.resetForNewPartie();
        this.m_score = new ScoreLogic();
        this.m_tour = 1;

        while (!this.m_score.estPartieTerminee()) {
            // Le sang généré est réinitialisé au début de chaque tour
            this.m_joueur.resetSang();

            // 1. Effets de début de tour (Croissance, etc.)
            this.m_plateau.appliquerEffetsDebutTourJoueur();
            this.m_plateau.appliquerEffetsDebutTourAdversaire();

            // 2. Intentions de l'IA
            CarteLogic[] intentions = this.m_adversaire.obtenirIntention(this.m_tour);

            // 3. Affichage initial du tour
            this.m_vue.afficherEtatJeu(this.m_plateau, this.m_joueur, this.m_score, this.m_partieActuelle, this.m_tour, intentions);

            // 4. Boucle d'action du joueur (avec piocher optionnel et obligatoire avant fin)
            boucleTourJoueur();

            // 5. L'adversaire joue
            this.m_adversaire.jouerTour(this.m_tour, this.m_plateau);

            // 6. Phase de combat
            this.m_vue.afficherMessageSimple("\n--- PHASE DE COMBAT ---");

            // Attaques du joueur
            int oAvantJoueur = compterCartesVivantesJoueur();
            List<Integer> degatsJoueur = this.m_plateau.resoudreAttaques(true);
            int mortsJoueurApresAttaque = oAvantJoueur - compterCartesVivantesJoueur();
            if (mortsJoueurApresAttaque > 0) {
                this.m_joueur.ajouterOs(mortsJoueurApresAttaque);
                this.m_vue.afficherMessageSimple("  " + mortsJoueurApresAttaque + " de vos cartes sont mortes au combat : +" + mortsJoueurApresAttaque + " os.");
            }

            int totalJoueur = 0;
            for (int i = 0; i < degatsJoueur.size(); i++) {
                int d = degatsJoueur.get(i);
                if (d > 0) {
                    this.m_vue.afficherMessageSimple("  Case " + i + " (vos cartes) inflige " + d + " degat(s) direct(s) !");
                    totalJoueur += d;
                }
            }
            if (totalJoueur > 0) this.m_score.ajouterPointsJoueur(totalJoueur);

            // Attaques de l'adversaire
            int oAvantAdversaire = compterCartesVivantesJoueur();
            List<Integer> degatsAdversaire = this.m_plateau.resoudreAttaques(false);
            int mortsJoueurEnDefense = oAvantAdversaire - compterCartesVivantesJoueur();
            if (mortsJoueurEnDefense > 0) {
                this.m_joueur.ajouterOs(mortsJoueurEnDefense);
                this.m_vue.afficherMessageSimple("  " + mortsJoueurEnDefense + " de vos cartes sont mortes en defense : +" + mortsJoueurEnDefense + " os.");
            }

            int totalAdversaire = 0;
            for (int i = 0; i < degatsAdversaire.size(); i++) {
                int d = degatsAdversaire.get(i);
                if (d > 0) {
                    this.m_vue.afficherMessageSimple("  Case " + i + " (adversaire) inflige " + d + " degat(s) direct(s) !");
                    totalAdversaire += d;
                }
            }
            if (totalAdversaire > 0) this.m_score.ajouterPointsAdversaire(totalAdversaire);

            // Mouvements de fin de tour (comme Coureur)
            this.m_plateau.avancerMouvementsJoueur();
            this.m_plateau.avancerMouvementsAdversaire();

            this.m_vue.afficherMessageSimple("  Os actuels : " + this.m_joueur.getReserveOs());

            if (this.m_tour == 2) {
                choisirNouvelleCarte();
                pierreDeSacrifice();
            }

            m_tour++;
        }

        return this.m_score.getValeurRelative() >= 5;
    }

    private int compterCartesVivantesJoueur() {
        int count = 0;
        for (Emplacement emp : this.m_plateau.getCasesJoueur()) {
            if (!emp.estVide()) count++;
        }
        return count;
    }

    private void boucleTourJoueur() {
        boolean tourTermine = false;
        boolean piocheFaite = false;

        while (!tourTermine) {
            this.m_vue.afficherActionsPossibles();
            String saisie = this.m_vue.lireSaisie();
            Commande commande = this.m_analyseur.interpreter(saisie);

            if (commande == null) {
                this.m_vue.afficherMessageSimple("Commande invalide. Exemples: 'placer pl1 B1' ou 'sacrifier B1' ou 'piocher' ou 'fin'");
                continue;
            }

            String action = commande.getAction();

            if (action.equals("fin")) {
                if (!piocheFaite) {
                    this.m_vue.afficherMessageSimple("Vous devez piocher une carte avant de terminer votre tour !");
                } else {
                    tourTermine = true;
                }
            }
            else if (action.equals("piocher")) {
                if (piocheFaite) {
                    this.m_vue.afficherMessageSimple("Vous avez deja pioche une carte ce tour !");
                    continue;
                }

                boolean choixValide = false;
                while (!choixValide) {
                    String choix = this.m_vue.demanderChoixPioche();
                    if (choix.equals("1")) {
                        if (this.m_joueur.getNombreCartesDeck() <= 0) {
                            this.m_vue.afficherMessageSimple("Deck principal vide !");
                        } else {
                            this.m_joueur.piocherCartePrincipal();
                            piocheFaite = true;
                            choixValide = true;
                        }
                    } else if (choix.equals("2")) {
                        if (this.m_joueur.getNombreEcureuils() <= 0) {
                            this.m_vue.afficherMessageSimple("Plus d'ecureuils disponibles !");
                        } else {
                            this.m_joueur.piocherEcureuil();
                            piocheFaite = true;
                            choixValide = true;
                        }
                    } else {
                        this.m_vue.afficherMessageSimple("Saisie incorrecte (1 ou 2 attendu).");
                    }
                }
                
                CarteLogic[] intentions = this.m_adversaire.obtenirIntention(this.m_tour);
                this.m_vue.afficherEtatJeu(this.m_plateau, this.m_joueur, this.m_score, this.m_partieActuelle, this.m_tour, intentions);
            }
            else if (action.equals("sacrifier")) {
                int positionCible = commande.getPositionPlateau();
                if (positionCible < 0 || positionCible >= m_plateau.getCasesJoueur().size()) {
                    this.m_vue.afficherMessageSimple("Position sur le plateau invalide (B1 a B4).");
                    continue;
                }

                List<Emplacement> casesJoueur = m_plateau.getCasesJoueur();
                if (casesJoueur.get(positionCible).estVide()) {
                    this.m_vue.afficherMessageSimple("Il n'y a pas de creature a sacrifier sur cette case !");
                    continue;
                }

                CarteLogic carteSacrifiee = casesJoueur.get(positionCible).getCarteContenue().orElseThrow();
                int sangGenere = carteSacrifiee.executerSacrifice(casesJoueur.get(positionCible), this.m_joueur);

                if (sangGenere <= 0) {
                    this.m_vue.afficherMessageSimple("Cette carte ne peut pas etre sacrifiee !");
                    continue;
                }

                this.m_vue.afficherMessageSimple("Sacrifice de " + carteSacrifiee.getNom());
                if (casesJoueur.get(positionCible).estVide()) {
                    this.m_vue.afficherMessageSimple("  +1 os. Total : " + this.m_joueur.getReserveOs());
                } else {
                    this.m_vue.afficherMessageSimple("  " + carteSacrifiee.getNom() + " resiste au sacrifice grace a Nombreuses Vies !");
                }
                this.m_joueur.ajouterSang(sangGenere);
                this.m_vue.afficherMessageSimple("  +" + sangGenere + " sang genere. Sang disponible ce tour : " + this.m_joueur.getReserveSang());

                // Redessiner
                CarteLogic[] intentions = this.m_adversaire.obtenirIntention(this.m_tour);
                this.m_vue.afficherEtatJeu(this.m_plateau, this.m_joueur, this.m_score, this.m_partieActuelle, this.m_tour, intentions);
            }
            else if (action.equals("placer")) {
                int indexMainZeroBased = commande.getIndexCarteMain() - 1;
                int positionCible = commande.getPositionPlateau();

                if (indexMainZeroBased < 0 || indexMainZeroBased >= m_joueur.getMain().length
                        || m_joueur.getMain()[indexMainZeroBased] == null) {
                    this.m_vue.afficherMessageSimple("Index de carte invalide dans votre main.");
                    continue;
                }
                if (positionCible < 0 || positionCible >= m_plateau.getCasesJoueur().size()) {
                    this.m_vue.afficherMessageSimple("Position sur le plateau invalide (B1 a B4).");
                    continue;
                }

                List<Emplacement> casesJoueur = m_plateau.getCasesJoueur();
                if (!casesJoueur.get(positionCible).estVide()) {
                    this.m_vue.afficherMessageSimple("Cette case est deja occupee !");
                    continue;
                }

                CarteAnimalLogic carteAPlacer = m_joueur.getMain()[indexMainZeroBased];
                int coutSang = carteAPlacer.getCoutSang();
                int coutOs   = carteAPlacer.getCoutOs();

                // Vérifier les ressources
                if (this.m_joueur.getReserveSang() < coutSang) {
                    this.m_vue.afficherMessageSimple("Pas assez de sang pour jouer cette carte ("
                            + coutSang + " sang requis, vous en avez " + this.m_joueur.getReserveSang() + ").");
                    this.m_vue.afficherMessageSimple("Sacrifiez des creatures de votre plateau avec 'sacrifier <position>' d'abord !");
                    continue;
                }
                if (this.m_joueur.getReserveOs() < coutOs) {
                    this.m_vue.afficherMessageSimple("Pas assez d'os pour jouer cette carte ("
                            + coutOs + " os requis, vous en avez " + this.m_joueur.getReserveOs() + ").");
                    continue;
                }

                // Consommer les ressources
                this.m_joueur.consommerSang(coutSang);
                this.m_joueur.consommerOs(coutOs);
                this.m_vue.afficherMessageSimple("Consommation de " + coutSang + " sang et " + coutOs + " os.");

                // Poser la carte
                casesJoueur.get(positionCible).placerCarte(carteAPlacer);
                m_joueur.supprimerCarteMain(indexMainZeroBased);

                this.m_vue.afficherMessageSimple("Placement de " + carteAPlacer.getNom() + " en B" + (positionCible + 1));

                CarteLogic[] intentions = this.m_adversaire.obtenirIntention(this.m_tour);
                this.m_vue.afficherEtatJeu(this.m_plateau, this.m_joueur, this.m_score, this.m_partieActuelle, this.m_tour, intentions);
            }
        }
    }

    private void choisirNouvelleCarte() {
        boolean choixValide = false;
        while (!choixValide) {
            String choix = this.m_vue.demanderChoixNouvelleCarte(this.m_partieActuelle);
            CarteAnimalLogic nouvelle = null;
            if (choix.equals("1")) {
                nouvelle = FabriqueCartes.creerElan();
                choixValide = true;
            } else if (choix.equals("2")) {
                nouvelle = FabriqueCartes.creerVipere();
                choixValide = true;
            } else if (choix.equals("3")) {
                nouvelle = FabriqueCartes.creerPorcEpic();
                choixValide = true;
            } else {
                this.m_vue.afficherMessageSimple("Choix invalide. Veuillez entrer 1, 2 ou 3.");
            }

            if (choixValide && nouvelle != null) {
                // Ajouter à la collection pour persister d'une partie à l'autre
                m_joueur.ajouterCarteACollection(nouvelle);
                // Ajouter égalementpl directement dans la main pour pouvoir la jouer tout de suite
                m_joueur.ajouterCarteMain(FabriqueCartes.creerCopieFraiche(nouvelle));
                this.m_vue.afficherMessageSimple(nouvelle.getNom() + " ajoutee a votre collection et directement dans votre main !");
            }
        }
    }

    private void pierreDeSacrifice() {
        List<Emplacement> casesJoueur = m_plateau.getCasesJoueur();
        
        // Vérifier s'il y a des créatures avec un pouvoir sur le plateau
        boolean aCreatureAvecPouvoir = false;
        for (Emplacement emp : casesJoueur) {
            if (emp.getCarteContenue().flatMap(CarteLogic::getPouvoirATransferer).isPresent()) {
                aCreatureAvecPouvoir = true;
                break;
            }
        }
        
        if (!aCreatureAvecPouvoir) {
            this.m_vue.afficherMessageSimple("\n==================================================");
            this.m_vue.afficherMessageSimple("PIERRE DE SACRIFICE");
            this.m_vue.afficherMessageSimple("==================================================");
            this.m_vue.afficherMessageSimple("Vous n'avez aucune creature avec un pouvoir sur le plateau a sacrifier.");
            return;
        }

        List<CarteAnimalLogic> col = m_joueur.getCollection();
        if (col.size() < 2) {
            this.m_vue.afficherMessageSimple("Vous n'avez pas assez de cartes dans votre collection pour effectuer un transfert.");
            return;
        }

        int indexSacrifie = -1;
        CarteLogic sacrifiee = null;
        java.util.Optional<Pouvoir> optPouvoir = java.util.Optional.empty();

        while (optPouvoir.isEmpty()) {
            indexSacrifie = this.m_vue.demanderCartePlateauASacrifier(casesJoueur);
            if (indexSacrifie < 0 || indexSacrifie >= casesJoueur.size()) {
                this.m_vue.afficherMessageSimple("Position invalide.");
                continue;
            }
            Emplacement emp = casesJoueur.get(indexSacrifie);
            if (emp.estVide()) {
                this.m_vue.afficherMessageSimple("Cette case est vide !");
                continue;
            }
            sacrifiee = emp.getCarteContenue().orElseThrow();
            optPouvoir = sacrifiee.getPouvoirATransferer();
            if (optPouvoir.isEmpty()) {
                this.m_vue.afficherMessageSimple(sacrifiee.getNom() + " n'a aucun pouvoir a transferer. Choisissez une autre carte.");
            }
        }

        Pouvoir pouvoirATransferer = optPouvoir.get();

        this.m_vue.afficherMessageSimple("Vous recuperez le pouvoir : " + pouvoirATransferer.getNom());
        
        // Retirer la créature du plateau
        casesJoueur.get(indexSacrifie).liberer();

        // Retirer la créature correspondante de la collection
        CarteAnimalLogic templateASupprimer = null;
        for (CarteAnimalLogic c : col) {
            if (c.getNom().equals(sacrifiee.getNom())) {
                templateASupprimer = c;
                break;
            }
        }
        if (templateASupprimer != null) {
            col.remove(templateASupprimer);
        }

        int indexCible = -1;
        while (indexCible < 0 || indexCible >= col.size()) {
            indexCible = this.m_vue.demanderCarteCibleTransfert(col, pouvoirATransferer.getNom());
            if (indexCible < 0 || indexCible >= col.size()) {
                this.m_vue.afficherMessageSimple("Numero invalide.");
            }
        }

        CarteAnimalLogic cible = col.get(indexCible);
        cible.ajouterPouvoir(pouvoirATransferer);
        this.m_vue.afficherMessageSimple("\nLe pouvoir " + pouvoirATransferer.getNom() + " a ete ajoute a " + cible.getNom() + " !");
    }

    public static void main(String[] args) {
        GestionnairePartie gestionnaire = new GestionnairePartie();
        gestionnaire.executerJeu();
    }
}