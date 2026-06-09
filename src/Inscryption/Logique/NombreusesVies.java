package Inscryption.Logique;

public class NombreusesVies extends Pouvoir {

    @Override
    public String getNom() {
        return "Nombreuses Vies";
    }

    
    @Override
    public boolean auSacrifice(CarteLogic carteSacrifiee) {
        return carteSacrifiee != null;
    }
}