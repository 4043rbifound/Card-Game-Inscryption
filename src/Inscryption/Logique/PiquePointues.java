package Inscryption.Logique;

public class PiquePointues extends Pouvoir {

    @Override
    public String getNom() {
        return "Pique Pointues";
    }

    @Override
    public void apresRecevoirDegats(CarteLogic cible, CarteLogic attaquant, int degatsRecus, PlateauLogic plateau) {
        if (degatsRecus > 0 && cible != null) {
            if (attaquant != null && !attaquant.estMorte()) {
                int pvActuelsAttaquant = attaquant.getPointsVieActuels();
                attaquant.setPv(pvActuelsAttaquant - 1);
            }
        }
    }
}