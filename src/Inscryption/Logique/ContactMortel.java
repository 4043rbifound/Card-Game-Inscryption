package Inscryption.Logique;

public class ContactMortel extends Pouvoir {

    @Override
    public String getNom() {
        return "Contact Mortel";
    }

    @Override
    public void apresRecevoirDegats(CarteAnimalLogic cible, CarteAnimalLogic attaquant, int degatsRecus, PlateauLogic plateau) {
        if (degatsRecus > 0 && cible != null &&attaquant != null && attaquant.getPouvoirs().contains(this)) //
        {
            cible.setPv(0);
        }
    }
}