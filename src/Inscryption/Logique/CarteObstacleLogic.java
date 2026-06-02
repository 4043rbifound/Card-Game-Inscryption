package Inscryption.Logique;

import java.util.List;

public class CarteObstacleLogic extends CarteLogic {

    public CarteObstacleLogic(String nom, int pv) {
        super(nom, pv, 0);
    }

    @Override
    public void attaquer(Emplacement caseEnFace, ScoreLogic score, List<String> messages, PlateauLogic plateau) {
        // Un obstacle n'attaque pas : le corps de la méthode reste vide !
    }
}