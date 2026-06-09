package Inscryption.Logique;

public class CarteAnimalLogic extends CarteLogic {
    private int m_coutSang;
    private int m_coutOs;
    private int m_toursSurPlateau;

    public CarteAnimalLogic(String nom, int pv, int att, int sang, int os) {
        super(nom, pv, att);
        this.m_coutSang = sang;
        this.m_coutOs = os;
        this.m_toursSurPlateau = 0;
    }

    @Override
    public void tuerParContactMortel() {
        this.setPv(0);
    }

    @Override
    public int executerSacrifice(Emplacement caseActuelle, JoueurLogic joueur) {
        boolean survit = false;
        for (Pouvoir p : this.getPouvoirs()) {
            if (p.auSacrifice(this)) {
                survit = true;
            }
        }
        if (survit) {
            System.out.println("  " + this.getNom() + " resiste au sacrifice grace a Nombreuses Vies !");
        } else {
            caseActuelle.liberer();
            joueur.ajouterOs(1);
            System.out.println("  +1 os. Total : " + joueur.getReserveOs());
        }
        return 1;
    }

    @Override
    public String getLigneAttaque() {
        return String.format(" Att: %d", getPointsAttaque());
    }

    @Override
    public String getLignePouvoir() {
        if (this.getPouvoirs() != null && !this.getPouvoirs().isEmpty()) {
            return this.getPouvoirs().get(0).getNom();
        }
        return "";
    }

    public CarteAnimalLogic creerCopieFraiche() {
        CarteAnimalLogic copie = new CarteAnimalLogic(this.getNom(), this.getPointsVieMax(), this.getPointsAttaque(), this.getCoutSang(), this.getCoutOs());
        for (Pouvoir p : this.getPouvoirs()) {
            copie.ajouterPouvoir(p);
        }
        return copie;
    }

    public Pouvoir getPouvoirATransferer() {
        if (!this.getPouvoirs().isEmpty()) {
            return this.getPouvoirs().get(0);
        }
        if (this.getLignePouvoir().equalsIgnoreCase("Volant")) {
            return new Volant();
        }
        return null;
    }

    /**
     * Délègue la résolution du combat à l'emplacement ciblé.
     * Retourne les dégâts directs au score si la case est vide, 0 sinon.
     */
    @Override
    public int attaquer(Emplacement caseEnFace) {
        return caseEnFace.recevoirAttaque(this);
    }

    public int getCoutSang() { return m_coutSang; }
    public int getCoutOs() { return m_coutOs; }

    @Override
    public void incrementerToursSurPlateau() {
        this.m_toursSurPlateau++;
    }

    @Override
    public int getToursSurPlateau() {
        return this.m_toursSurPlateau;
    }
}