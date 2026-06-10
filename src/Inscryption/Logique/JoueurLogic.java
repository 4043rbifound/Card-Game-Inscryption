package Inscryption.Logique;

public class JoueurLogic {

    private int m_reserveOs;
    private int m_reserveSang;

    private CarteAnimalLogic[] m_main;
    private CarteAnimalLogic[] m_deckPrincipal;
    private CarteAnimalLogic[] m_pileEcureuils;

    private int m_nombreCartesMain;
    private int m_nombreCartesDeck;
    private int m_nombreEcureuils;

    private java.util.List<CarteAnimalLogic> m_collection;

    public JoueurLogic() {
        this.m_reserveOs = 0;
        this.m_reserveSang = 0;
        this.m_main = new CarteAnimalLogic[100];
        this.m_deckPrincipal = new CarteAnimalLogic[100];
        this.m_pileEcureuils = new CarteAnimalLogic[100];
        this.m_nombreCartesMain = 0;
        this.m_nombreCartesDeck = 0;
        this.m_nombreEcureuils = 0;
        this.m_collection = new java.util.ArrayList<CarteAnimalLogic>();

        initialiserCollectionEtDeck();
    }

    private void initialiserCollectionEtDeck() {
        // Deck de 15 cartes : 8 écureuils + 7 animaux
        // Les 4 dernières cartes = 1ers piochées => 3 écureuils + 1 loup en main de départ
        m_collection.add(FabriqueCartes.creerLouveteau());
        m_collection.add(FabriqueCartes.creerMoineau());
        m_collection.add(FabriqueCartes.creerHermine());
        m_collection.add(FabriqueCartes.creerElan());
        m_collection.add(FabriqueCartes.creerVipere());
        m_collection.add(FabriqueCartes.creerPorcEpic());
        m_collection.add(FabriqueCartes.creerEcureuil());
        m_collection.add(FabriqueCartes.creerEcureuil());
        m_collection.add(FabriqueCartes.creerEcureuil());
        m_collection.add(FabriqueCartes.creerEcureuil());
        m_collection.add(FabriqueCartes.creerEcureuil());
        m_collection.add(FabriqueCartes.creerLoup());      // 4e carte piochée
        m_collection.add(FabriqueCartes.creerEcureuil()); // 3e carte piochée
        m_collection.add(FabriqueCartes.creerEcureuil()); // 2e carte piochée
        m_collection.add(FabriqueCartes.creerEcureuil()); // 1re carte piochée

        resetForNewPartie();
    }

    public void resetForNewPartie() {
        this.m_reserveOs = 0;
        this.m_reserveSang = 0;

        // Vider la main
        for (int i = 0; i < this.m_main.length; i++) {
            this.m_main[i] = null;
        }
        this.m_nombreCartesMain = 0;

        // Vider le deck
        for (int i = 0; i < this.m_deckPrincipal.length; i++) {
            this.m_deckPrincipal[i] = null;
        }
        this.m_nombreCartesDeck = 0;

        // Remplir le deck à partir de copies fraîches de la collection
        for (CarteAnimalLogic template : m_collection) {
            ajouterCarteDeck(FabriqueCartes.creerCopieFraiche(template));
        }

        // Réinitialiser la pile d'écureuils
        for (int i = 0; i < this.m_pileEcureuils.length; i++) {
            this.m_pileEcureuils[i] = null;
        }
        this.m_nombreEcureuils = 0;
        for (int i = 0; i < 20; i++) {
            ajouterEcureuil(FabriqueCartes.creerEcureuil());
        }

        // Le joueur pioche ses 4 cartes de départ
        for (int i = 0; i < 4; i++) {
            piocherCartePrincipal();
        }
    }

    /**
     * Réinitialisation légère entre deux parties :
     * on conserve la main et la pioche du joueur (elles persistent entre les parties),
     * on remet juste les ressources de combat (os et sang) à zéro.
     */
    public void resetRessourcesPourNouvellePartie() {
        this.m_reserveOs = 0;
        this.m_reserveSang = 0;
    }

    public void ajouterCarteACollection(CarteAnimalLogic carte) {
        if (carte != null) {
            this.m_collection.add(carte);
        }
    }

    public java.util.List<CarteAnimalLogic> getCollection() {
        return this.m_collection;
    }

    public void ajouterCarteDeck(CarteAnimalLogic carte) {
        this.m_deckPrincipal[this.m_nombreCartesDeck] = carte;
        this.m_nombreCartesDeck++;
    }

    public void ajouterEcureuil(CarteAnimalLogic carte) {
        this.m_pileEcureuils[this.m_nombreEcureuils] = carte;
        this.m_nombreEcureuils++;
    }

    public void piocherCartePrincipal() {
        if (this.m_nombreCartesDeck <= 0) {
            return;
        }
        CarteAnimalLogic carte = this.m_deckPrincipal[this.m_nombreCartesDeck - 1];
        this.m_nombreCartesDeck--;
        this.m_main[this.m_nombreCartesMain] = carte;
        this.m_nombreCartesMain++;
    }

    public void piocherEcureuil() {
        if (this.m_nombreEcureuils <= 0) {
            return;
        }
        CarteAnimalLogic carte = this.m_pileEcureuils[this.m_nombreEcureuils - 1];
        this.m_nombreEcureuils--;
        this.m_main[this.m_nombreCartesMain] = carte;
        this.m_nombreCartesMain++;
    }

    public void ajouterOs(int quantite) {
        this.m_reserveOs += quantite;
    }

    public void consommerOs(int quantite) {
        this.m_reserveOs -= quantite;
        if (this.m_reserveOs < 0) {
            this.m_reserveOs = 0;
        }
    }

    public int getReserveOs() {
        return this.m_reserveOs;
    }

    public void ajouterSang(int quantite) {
        this.m_reserveSang += quantite;
    }

    public void consommerSang(int quantite) {
        this.m_reserveSang -= quantite;
        if (this.m_reserveSang < 0) {
            this.m_reserveSang = 0;
        }
    }

    public int getReserveSang() {
        return this.m_reserveSang;
    }

    public void resetSang() {
        this.m_reserveSang = 0;
    }

    public CarteAnimalLogic[] getMain() {
        return this.m_main;
    }

    public int getNombreCartesMain() {
        return this.m_nombreCartesMain;
    }

    public int getNombreCartesDeck() {
        return this.m_nombreCartesDeck;
    }

    public int getNombreEcureuils() {
        return this.m_nombreEcureuils;
    }

    public void ajouterCarteMain(CarteAnimalLogic carte) {
        if (carte != null && this.m_nombreCartesMain < this.m_main.length) {
            this.m_main[this.m_nombreCartesMain] = carte;
            this.m_nombreCartesMain++;
        }
    }

    public void supprimerCarteMain(int index) {
        if (index >= 0 && index < this.m_nombreCartesMain) {
            this.m_main[index] = null;
            for (int i = index; i < this.m_nombreCartesMain - 1; i++) {
                this.m_main[i] = this.m_main[i + 1];
            }
            this.m_main[this.m_nombreCartesMain - 1] = null;
            this.m_nombreCartesMain--;
        }
    }
}
