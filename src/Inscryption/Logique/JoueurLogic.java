package Inscryption.Logique;

public class JoueurLogic {

    private int m_reserveOs;

    private CarteAnimalLogic[] m_main;
    private CarteAnimalLogic[] m_deckPrincipal;
    private CarteAnimalLogic[] m_pileEcureuils;

    private int m_nombreCartesMain;
    private int m_nombreCartesDeck;
    private int m_nombreEcureuils;

    public JoueurLogic() {

        this.m_reserveOs = 0;

        this.m_main = new CarteAnimalLogic[20];
        this.m_deckPrincipal = new CarteAnimalLogic[20];
        this.m_pileEcureuils = new CarteAnimalLogic[20];

        this.m_nombreCartesMain = 0;
        this.m_nombreCartesDeck = 0;
        this.m_nombreEcureuils = 0;

        initialiserDeck();
    }

    private void initialiserDeck() {

        ajouterCarteDeck(FabriqueCartes.creerLoup());
        ajouterCarteDeck(FabriqueCartes.creerLouveteau());
        ajouterCarteDeck(FabriqueCartes.creerMoineau());
        ajouterCarteDeck(FabriqueCartes.creerHermine());

        ajouterEcureuil(FabriqueCartes.creerEcureuil());
        ajouterEcureuil(FabriqueCartes.creerEcureuil());
        ajouterEcureuil(FabriqueCartes.creerEcureuil());
        ajouterEcureuil(FabriqueCartes.creerEcureuil());

        for (int i = 0; i < 4; i++) {
            piocherCartePrincipal();
        }
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

    public CarteAnimalLogic[] getMain() {
        return this.m_main;
    }

    public int getNombreCartesMain() {
        return this.m_nombreCartesMain;
    }
}
