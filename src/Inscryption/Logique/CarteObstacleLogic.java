package Inscryption.Logique;

public class CarteObstacleLogic extends CarteLogic {

    public CarteObstacleLogic(String nom, int pv) {
        super(nom, pv, 0);
    }

    @Override
    public boolean estObstacle() {
        return true;
    }
}