package Inscryption.Logique;

import java.util.ArrayList;
import java.util.List;

public class CarteAnimalLogic extends CarteLogic {
    private int m_coutSang;
    private int m_coutOs;
    private boolean m_estVolant;
    private int m_toursSurPlateau;
    private List<Pouvoir> m_pouvoirs;

    public CarteAnimalLogic(String nom, int pv, int att, int sang, int os, boolean volant) {
        super(nom, pv, att);
        this.m_coutSang = sang;
        this.m_coutOs = os;
        this.m_estVolant = volant;
        this.m_toursSurPlateau = 0;
        this.m_pouvoirs = new ArrayList<>();
    }

    // qlq getters
    public int getCoutSang() { return m_coutSang; }
    public int getCoutOs() { return m_coutOs; }
    public boolean estVolant() { return m_estVolant; }
    public int getToursSurPlateau() { return m_toursSurPlateau; }

    public void incrementerToursSurPlateau() {
        this.m_toursSurPlateau++;
    }
    // pour pierre sacrifice
    public void ajouterPouvoir(Pouvoir pouvoir) {
            this.m_pouvoirs.add(pouvoir);
        }


    public List<Pouvoir> retirerPouvoirs() {
        List<Pouvoir> anciens = new ArrayList<>(this.m_pouvoirs);
        this.m_pouvoirs.clear();
        return anciens;
    }

    public List<Pouvoir> getPouvoirs() {
        return m_pouvoirs;
    }
}
