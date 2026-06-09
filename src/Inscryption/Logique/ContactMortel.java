package Inscryption.Logique;

public class ContactMortel extends Pouvoir {

    @Override
    public String getNom() {
        return "Contact Mortel";
    }

    @Override
    public void apresRecevoirDegats(CarteLogic cible, CarteLogic attaquant, int degatsRecus) {
        if (degatsRecus > 0 && cible != null) {
            cible.tuerParContactMortel();
        }
    }
}