package Inscryption.Logique;

import java.util.List;

public class CarteObstacleLogic extends CarteLogic {

    public CarteObstacleLogic(String nom, int pv) {
        super(nom, pv, 0);
    }

    @Override
    public int attaquer(Emplacement caseEnFace) {
        // Un obstacle n'attaque pas : le corps de la méthode reste vide !
        return 0;
    }
}