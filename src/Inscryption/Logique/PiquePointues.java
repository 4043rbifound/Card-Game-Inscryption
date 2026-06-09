package Inscryption.Logique;

public class PiquePointues extends Pouvoir {

    @Override
    public String getNom() {
        return "Pique Pointues";
    }

    @Override
    public void apresSubirDegats(CarteLogic cible, CarteLogic attaquant, int degatsRecus) {
        if (degatsRecus > 0 && cible != null) {
            if (attaquant != null && !attaquant.estMorte()) {
                int pvActuelsAttaquant = attaquant.getPointsVieActuels();
                attaquant.setPv(pvActuelsAttaquant - 1);
            }
        }
    }
}