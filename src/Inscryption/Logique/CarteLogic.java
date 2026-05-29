package Inscryption.Logique;

public abstract class CarteLogic {
    private String m_nom;
    private int m_pointsVieMax;
    private int m_pointsVieActuels;
    private int m_pointsAttaque;

    public CarteLogic(String nom, int pv, int att) {
        this.m_nom = nom;
        this.m_pointsVieMax = pv;
        this.m_pointsVieActuels = pv;
        this.m_pointsAttaque = att;
    }

    public String getNom() { return m_nom; }
    public int getPointsVieMax() { return m_pointsVieMax; }
    public int getPointsVieActuels() { return m_pointsVieActuels; }
    public int getPointsAttaque() { return m_pointsAttaque; }

    public boolean estMorte() {
        return this.m_pointsVieActuels <= 0;
    }

    public boolean estObstacle()
    {
        return false;
    }

    public void recevoirDegats(int valeur) {
        if (valeur > 0) {
            if( (this.m_pointsVieActuels - valeur) < 0) {this.m_pointsVieActuels = 0;}
            else
            {
                this.m_pointsVieActuels -= valeur;
            }
        }
    }




    public void setAttaque(int valeur) {
        this.m_pointsAttaque = valeur;
        }
    public void setPv(int valeur)
    {
        this.m_pointsVieActuels = valeur;
    }

    }