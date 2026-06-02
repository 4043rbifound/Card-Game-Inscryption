package Inscryption.Logique;

import java.util.List;

public class CarteVolanteLogic extends CarteAnimalLogic {

    public CarteVolanteLogic(String nom, int pv, int att, int sang, int os) {
        super(nom, pv, att, sang, os);
    }

    @Override
    public void attaquer(Emplacement caseEnFace, ScoreLogic score, List<String> messages, PlateauLogic plateau) {
        int degats = this.getPointsAttaque();
        if (degats > 0) {
            plateau.appliquerDegatsDirects(degats, this, score, messages);
        }
    }
}