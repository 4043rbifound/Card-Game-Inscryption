package Inscryption.Affichage;

public class Commande {

    private String m_action;
    private Integer m_indexCarteMain;
    private Integer m_positionPlateau;

    public Commande(String action) {
        this.m_action = action;
        this.m_indexCarteMain = null;
        this.m_positionPlateau = null;
    }

    public Commande(String action, Integer indexCarteMain, Integer positionPlateau) {
        this.m_action = action;
        this.m_indexCarteMain = indexCarteMain;
        this.m_positionPlateau = positionPlateau;
    }

    public String getAction() {
        return this.m_action;
    }

    public Integer getIndexCarteMain() {
        return this.m_indexCarteMain;
    }

    public Integer getPositionPlateau() {
        return this.m_positionPlateau;
    }
}