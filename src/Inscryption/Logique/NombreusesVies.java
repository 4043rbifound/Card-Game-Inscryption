package Inscryption.Logique;

public class NombreusesVies extends Pouvoir {

    @Override
    public String getNom() {
        return "Nombreuses Vies";
    }

    @Override
    public boolean auSacrifice(CarteAnimalLogic carteSacrifiee) {
        if(carteSacrifiee != null && carteSacrifiee.getPouvoirs().contains(this)) {
            return true;
        }
        return false;
    }
}