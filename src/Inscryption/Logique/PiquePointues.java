package Inscryption.Logique;

public class PiquePointues extends Pouvoir {

    @Override
    public String getNom() {
        return "Pique Pointues";
    }

    @Override
    public void apresRecevoirDegats(CarteAnimalLogic cible, CarteAnimalLogic attaquant, int degatsRecus, PlateauLogic plateau) {
        if (degatsRecus > 0 && cible != null && cible.getPouvoirs().contains(this)) {
            if (attaquant != null && !attaquant.estMorte()) // 2 if pcq je trouve ca plus lisible
            {
                int pvActuelsAttaquant = attaquant.getPointsVieActuels();
                attaquant.setPv(pvActuelsAttaquant - 1);
            }
        }
    }
}