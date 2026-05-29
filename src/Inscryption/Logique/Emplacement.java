package Inscryption.Logique;

public class Emplacement {

    private int m_position;
    private CarteLogic m_carteContenue;

    public Emplacement(int position) {
        this.m_position = position;
        this.m_carteContenue = null;
    }

    public int getPosition() {
        return this.m_position;
    }

    public boolean estVide() {
        return this.m_carteContenue == null;
    }

    public CarteLogic getCarteContenue() {
        return this.m_carteContenue;
    }

    public void placerCarte(CarteLogic carte) {
        this.m_carteContenue = carte;
    }

    public void liberer() {
        this.m_carteContenue = null;
    }
}
