package Inscryption.Logique;

import java.util.List;

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
    public void attaquer(Emplacement caseEnFace, ScoreLogic score, List<String> messages, PlateauLogic plateau) {
        if (caseEnFace.estVide()) {
            int degats = this.getPointsAttaque();
            if (degats > 0) {
                plateau.appliquerDegatsDirects(degats, this, score, messages);
            }
        }
        else {
            CarteLogic cible = caseEnFace.getCarteContenue();
            int degatsCalcules = this.getPointsAttaque();

            for (Pouvoir p : cible.getPouvoirs()) {
                degatsCalcules = p.auCalculAttaque(degatsCalcules, this, cible, plateau);
            }

            if (degatsCalcules > 0) {
                cible.recevoirDegats(degatsCalcules);
                messages.add(this.getNom() + " inflige " + degatsCalcules + " dégâts à " + cible.getNom() + ".");

                for (Pouvoir p : cible.getPouvoirs()) {
                    p.apresRecevoirDegats(cible, this, degatsCalcules, plateau);
                }
                for (Pouvoir p : this.getPouvoirs()) {
                    p.apresRecevoirDegats(cible, this, degatsCalcules, plateau);
                }
            }
        }
    }

    public int getCoutSang() { return m_coutSang; }
    public int getCoutOs() { return m_coutOs; }
    public void incrementerToursSurPlateau() {
        this.m_toursSurPlateau++;
    }

    @Override
    public int getToursSurPlateau() {
        return this.m_toursSurPlateau;
    }
}