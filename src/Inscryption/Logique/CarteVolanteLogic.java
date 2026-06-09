package Inscryption.Logique;

public class CarteVolanteLogic extends CarteAnimalLogic {

    public CarteVolanteLogic(String nom, int pv, int att, int sang, int os) {
        super(nom, pv, att, sang, os);
    }

    @Override
    public String getLignePouvoir() {
        String p = super.getLignePouvoir();
        if (p.isEmpty()) {
            return "Volant";
        }
        return p;
    }

    @Override
    public CarteAnimalLogic creerCopieFraiche() {
        CarteAnimalLogic copie = new CarteVolanteLogic(this.getNom(), this.getPointsVieMax(), this.getPointsAttaque(), this.getCoutSang(), this.getCoutOs());
        for (Pouvoir p : this.getPouvoirs()) {
            copie.ajouterPouvoir(p);
        }
        return copie;
    }

    /**
     * Une carte volante attaque toujours directement le score,
     * peu importe ce qui se trouve en face d'elle.
     */
    @Override
    public int attaquer(Emplacement caseEnFace) {
        return this.getPointsAttaque();
    }
}