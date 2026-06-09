package Inscryption.Affichage;

import java.util.ArrayList;
import java.util.List;
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
                System.out.println("\n--- VICTOIRE DE LA PARTIE " + partie + " ---");
            } else {
                System.out.println("\n--- DEFAITE ---");
                System.out.println("Vous avez perdu la partie " + partie + ". Partie terminee.");
                return;
            }
        }

        System.out.println("\n==================================================");
        System.out.println("FELICITATIONS ! Vous avez remporte les 3 parties !");
        System.out.println("==================================================");
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
            System.out.println("\n--- PHASE DE COMBAT ---");

            // Attaques du joueur
            int oAvantJoueur = compterCartesVivantesJoueur();
            List<Integer> degatsJoueur = this.m_plateau.resoudreAttaques(true);
            int mortsJoueurApresAttaque = oAvantJoueur - compterCartesVivantesJoueur();
            if (mortsJoueurApresAttaque > 0) {
                this.m_joueur.ajouterOs(mortsJoueurApresAttaque);
                System.out.println("  " + mortsJoueurApresAttaque + " de vos cartes sont mortes au combat : +" + mortsJoueurApresAttaque + " os.");
            }

            int totalJoueur = 0;
            for (int i = 0; i < degatsJoueur.size(); i++) {
                int d = degatsJoueur.get(i);
                if (d > 0) {
                    System.out.println("  Case " + i + " (vos cartes) inflige " + d + " degat(s) direct(s) !");
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
                System.out.println("  " + mortsJoueurEnDefense + " de vos cartes sont mortes en defense : +" + mortsJoueurEnDefense + " os.");
            }

            int totalAdversaire = 0;
            for (int i = 0; i < degatsAdversaire.size(); i++) {
                int d = degatsAdversaire.get(i);
                if (d > 0) {
                    System.out.println("  Case " + i + " (adversaire) inflige " + d + " degat(s) direct(s) !");
                    totalAdversaire += d;
                }
            }
            if (totalAdversaire > 0) this.m_score.ajouterPointsAdversaire(totalAdversaire);

            // Mouvements de fin de tour (comme Coureur)
            this.m_plateau.avancerMouvementsJoueur();
            this.m_plateau.avancerMouvementsAdversaire();

            System.out.println("  Os actuels : " + this.m_joueur.getReserveOs());

            if (this.m_tour == 2 && this.m_partieActuelle == 2) {
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
                System.out.println("Commande invalide. Exemples: 'placer 1 B1' ou 'sacrifier B1' ou 'piocher' ou 'fin'");
                continue;
            }

            String action = commande.getAction();

            if (action.equals("fin")) {
                if (!piocheFaite) {
                    System.out.println("Vous devez piocher une carte avant de terminer votre tour !");
                } else {
                    tourTermine = true;
                }
            }
            else if (action.equals("piocher")) {
                if (piocheFaite) {
                    System.out.println("Vous avez deja pioche une carte ce tour !");
                    continue;
                }

                Scanner scanner = new Scanner(System.in);
                boolean choixValide = false;
                while (!choixValide) {
                    System.out.println("\nChoisissez votre pioche :");
                    System.out.println("1. Deck principal");
                    System.out.println("2. Pile d'ecureuils");
                    System.out.print("Choix (1 ou 2) : ");
                    String choix = scanner.nextLine().trim();
                    if (choix.equals("1")) {
                        if (this.m_joueur.getNombreCartesDeck() <= 0) {
                            System.out.println("Deck principal vide !");
                        } else {
                            this.m_joueur.piocherCartePrincipal();
                            piocheFaite = true;
                            choixValide = true;
                        }
                    } else if (choix.equals("2")) {
                        if (this.m_joueur.getNombreEcureuils() <= 0) {
                            System.out.println("Plus d'ecureuils disponibles !");
                        } else {
                            this.m_joueur.piocherEcureuil();
                            piocheFaite = true;
                            choixValide = true;
                        }
                    } else {
                        System.out.println("Saisie incorrecte (1 ou 2 attendu).");
                    }
                }
                
                CarteLogic[] intentions = this.m_adversaire.obtenirIntention(this.m_tour);
                this.m_vue.afficherEtatJeu(this.m_plateau, this.m_joueur, this.m_score, this.m_partieActuelle, this.m_tour, intentions);
            }
            else if (action.equals("sacrifier")) {
                int positionCible = commande.getPositionPlateau();
                if (positionCible < 0 || positionCible >= m_plateau.getCasesJoueur().size()) {
                    System.out.println("Position sur le plateau invalide (B1 a B4).");
                    continue;
                }

                List<Emplacement> casesJoueur = m_plateau.getCasesJoueur();
                if (casesJoueur.get(positionCible).estVide()) {
                    System.out.println("Il n'y a pas de creature a sacrifier sur cette case !");
                    continue;
                }

                CarteLogic carteSacrifiee = casesJoueur.get(positionCible).getCarteContenue();
                int sangGenere = carteSacrifiee.executerSacrifice(casesJoueur.get(positionCible), this.m_joueur);

                if (sangGenere <= 0) {
                    System.out.println("Cette carte ne peut pas etre sacrifiee !");
                    continue;
                }

                System.out.println("Sacrifice de " + carteSacrifiee.getNom());
                this.m_joueur.ajouterSang(sangGenere);
                System.out.println("  +" + sangGenere + " sang genere. Sang disponible ce tour : " + this.m_joueur.getReserveSang());

                // Redessiner
                CarteLogic[] intentions = this.m_adversaire.obtenirIntention(this.m_tour);
                this.m_vue.afficherEtatJeu(this.m_plateau, this.m_joueur, this.m_score, this.m_partieActuelle, this.m_tour, intentions);
            }
            else if (action.equals("placer")) {
                int indexMainZeroBased = commande.getIndexCarteMain() - 1;
                int positionCible = commande.getPositionPlateau();

                if (indexMainZeroBased < 0 || indexMainZeroBased >= m_joueur.getMain().length
                        || m_joueur.getMain()[indexMainZeroBased] == null) {
                    System.out.println("Index de carte invalide dans votre main.");
                    continue;
                }
                if (positionCible < 0 || positionCible >= m_plateau.getCasesJoueur().size()) {
                    System.out.println("Position sur le plateau invalide (B1 a B4).");
                    continue;
                }

                List<Emplacement> casesJoueur = m_plateau.getCasesJoueur();
                if (!casesJoueur.get(positionCible).estVide()) {
                    System.out.println("Cette case est deja occupee !");
                    continue;
                }

                CarteAnimalLogic carteAPlacer = m_joueur.getMain()[indexMainZeroBased];
                int coutSang = carteAPlacer.getCoutSang();
                int coutOs   = carteAPlacer.getCoutOs();

                // Vérifier les ressources
                if (this.m_joueur.getReserveSang() < coutSang) {
                    System.out.println("Pas assez de sang pour jouer cette carte ("
                            + coutSang + " sang requis, vous en avez " + this.m_joueur.getReserveSang() + ").");
                    System.out.println("Sacrifiez des creatures de votre plateau avec 'sacrifier <position>' d'abord !");
                    continue;
                }
                if (this.m_joueur.getReserveOs() < coutOs) {
                    System.out.println("Pas assez d'os pour jouer cette carte ("
                            + coutOs + " os requis, vous en avez " + this.m_joueur.getReserveOs() + ").");
                    continue;
                }

                // Consommer les ressources
                this.m_joueur.consommerSang(coutSang);
                this.m_joueur.consommerOs(coutOs);
                System.out.println("Consommation de " + coutSang + " sang et " + coutOs + " os.");

                // Poser la carte
                casesJoueur.get(positionCible).placerCarte(carteAPlacer);
                m_joueur.getMain()[indexMainZeroBased] = null;
                tasserMain();

                System.out.println("Placement de " + carteAPlacer.getNom() + " en B" + (positionCible + 1));

                CarteLogic[] intentions = this.m_adversaire.obtenirIntention(this.m_tour);
                this.m_vue.afficherEtatJeu(this.m_plateau, this.m_joueur, this.m_score, this.m_partieActuelle, this.m_tour, intentions);
            }
        }
    }

    private void tasserMain() {
        CarteAnimalLogic[] main = m_joueur.getMain();
        CarteAnimalLogic[] nouvelleMain = new CarteAnimalLogic[main.length];
        int index = 0;
        for (int i = 0; i < main.length; i++) {
            if (main[i] != null) {
                nouvelleMain[index++] = main[i];
            }
        }
        for (int i = 0; i < main.length; i++) {
            main[i] = nouvelleMain[i];
        }
    }

    private void choisirNouvelleCarte() {
        System.out.println("\n==================================================");
        System.out.println("CHOIX D'UNE NOUVELLE CARTE (Fin de Partie 2)");
        System.out.println("==================================================");
        System.out.println("Choisissez une carte a ajouter a votre deck :");
        System.out.println("1. Elan (PV: 4, Att: 2, Cout Sang: 2, Pouvoir: Coureur)");
        System.out.println("2. Vipere (PV: 1, Att: 1, Cout Sang: 2, Pouvoir: Contact Mortel)");
        System.out.println("3. Porc-epic (PV: 2, Att: 1, Cout Sang: 1, Pouvoir: Piques pointues)");
        
        Scanner scanner = new Scanner(System.in);
        boolean choixValide = false;
        while (!choixValide) {
            System.out.print("Votre choix (1, 2 ou 3) : ");
            String choix = scanner.nextLine().trim();
            if (choix.equals("1")) {
                m_joueur.ajouterCarteACollection(FabriqueCartes.creerElan());
                System.out.println("Elan ajoute a votre collection !");
                choixValide = true;
            } else if (choix.equals("2")) {
                m_joueur.ajouterCarteACollection(FabriqueCartes.creerVipere());
                System.out.println("Vipere ajoutee a votre collection !");
                choixValide = true;
            } else if (choix.equals("3")) {
                m_joueur.ajouterCarteACollection(FabriqueCartes.creerPorcEpic());
                System.out.println("Porc-epic ajoute a votre collection !");
                choixValide = true;
            } else {
                System.out.println("Choix invalide. Veuillez entrer 1, 2 ou 3.");
            }
        }
    }

    private void pierreDeSacrifice() {
        System.out.println("\n==================================================");
        System.out.println("PIERRE DE SACRIFICE");
        System.out.println("==================================================");
        List<CarteAnimalLogic> col = m_joueur.getCollection();
        
        if (col.size() < 2) {
            System.out.println("Vous n'avez pas assez de cartes pour effectuer un sacrifice.");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        System.out.println("Choisissez une carte a sacrifier pour recuperer son pouvoir :");
        for (int i = 0; i < col.size(); i++) {
            CarteAnimalLogic c = col.get(i);
            String pstr = c.getLignePouvoir();
            if (!pstr.isEmpty()) {
                pstr = " (Pouvoir: " + pstr + ")";
            }
            System.out.println((i + 1) + ". " + c.getNom() + pstr);
        }

        int indexSacrifie = -1;
        while (indexSacrifie < 0 || indexSacrifie >= col.size()) {
            System.out.print("Entrez le numero de la carte a sacrifier : ");
            try {
                indexSacrifie = Integer.parseInt(scanner.nextLine().trim()) - 1;
                if (indexSacrifie < 0 || indexSacrifie >= col.size()) {
                    System.out.println("Numero invalide.");
                }
            } catch (Exception e) {
                System.out.println("Veuillez saisir un nombre valide.");
            }
        }

        CarteAnimalLogic sacrifiee = col.get(indexSacrifie);
        Pouvoir pouvoirATransferer = sacrifiee.getPouvoirATransferer();

        if (pouvoirATransferer == null) {
            System.out.println(sacrifiee.getNom() + " n'a aucun pouvoir. Elle est detruite sans transfert.");
            col.remove(indexSacrifie);
            return;
        }

        System.out.println("Vous recuperez le pouvoir : " + pouvoirATransferer.getNom());

        col.remove(indexSacrifie);

        System.out.println("\nChoisissez la carte de votre collection qui va recevoir le pouvoir " + pouvoirATransferer.getNom() + " :");
        for (int i = 0; i < col.size(); i++) {
            CarteAnimalLogic c = col.get(i);
            String pstr = c.getLignePouvoir();
            if (!pstr.isEmpty()) {
                pstr = " (Pouvoir: " + pstr + ")";
            }
            System.out.println((i + 1) + ". " + c.getNom() + pstr);
        }

        int indexCible = -1;
        while (indexCible < 0 || indexCible >= col.size()) {
            System.out.print("Entrez le numero de la carte cible : ");
            try {
                indexCible = Integer.parseInt(scanner.nextLine().trim()) - 1;
                if (indexCible < 0 || indexCible >= col.size()) {
                    System.out.println("Numero invalide.");
                }
            } catch (Exception e) {
                System.out.println("Veuillez saisir un nombre valide.");
            }
        }

        CarteAnimalLogic cible = col.get(indexCible);
        cible.ajouterPouvoir(pouvoirATransferer);
        System.out.println("\nLe pouvoir " + pouvoirATransferer.getNom() + " a ete ajoute a " + cible.getNom() + " !");
    }

    public static void main(String[] args) {
        GestionnairePartie gestionnaire = new GestionnairePartie();
        gestionnaire.executerJeu();
    }
}