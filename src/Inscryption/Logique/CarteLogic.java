package Inscryption.Logique;

import java.util.ArrayList;
import java.util.List;

public abstract class CarteLogic {
    private String m_nom;
    private int m_pointsVieMax;
    private int m_pointsVieActuels;
    private int m_pointsAttaque;
    protected List<Pouvoir> m_pouvoirs;

    public CarteLogic(String nom, int pv, int att) {
        this.m_nom = nom;
        this.m_pointsVieMax = pv;
        this.m_pointsVieActuels = pv;
        this.m_pointsAttaque = att;
        this.m_pouvoirs = new ArrayList<>();
    }

    public abstract void attaquer(Emplacement caseEnFace, ScoreLogic score, List<String> messages, PlateauLogic plateau);

    public List<Pouvoir> getPouvoirs() { return m_pouvoirs; }
    public void ajouterPouvoir(Pouvoir pouvoir) { this.m_pouvoirs.add(pouvoir); }

    public List<Pouvoir> retirerPouvoirs() {
        List<Pouvoir> anciens = new ArrayList<>(this.m_pouvoirs);
        this.m_pouvoirs.clear();
        return anciens;
    }

    public String getNom() { return m_nom; }
    public int getPointsVieMax() { return m_pointsVieMax; }
    public int getPointsVieActuels() { return m_pointsVieActuels; }
    public int getPointsAttaque() { return m_pointsAttaque; }

    public boolean estMorte() { return this.m_pointsVieActuels <= 0; }

    public void recevoirDegats(int valeur) {
        if (valeur > 0) {
            if ((this.m_pointsVieActuels - valeur) < 0) {
                this.m_pointsVieActuels = 0;
            } else {
                this.m_pointsVieActuels -= valeur;
            }
        }
    }


    public void setAttaque(int valeur) { this.m_pointsAttaque = valeur; }
    public void setPv(int valeur) { this.m_pointsVieActuels = valeur; }
    public void setNom(String nom) { this.m_nom = nom; }
    public void setPvMax(int val) { this.m_pointsVieMax = val; }
    public void incrementerToursSurPlateau() {
    }

    public int getToursSurPlateau() {
        return 0;
    }
}