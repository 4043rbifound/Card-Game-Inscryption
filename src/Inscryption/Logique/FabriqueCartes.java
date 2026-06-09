package Inscryption.Logique;

public class FabriqueCartes {

    public static CarteAnimalLogic creerChat() {
        CarteAnimalLogic chat = new CarteAnimalLogic("Chat", 1, 0, 1, 0);
        chat.ajouterPouvoir(new NombreusesVies());
        return chat;
    }

    public static CarteAnimalLogic creerGrizzly() {
        return new CarteAnimalLogic("Grizzly", 6, 4, 3, 0);
    }

    public static CarteAnimalLogic creerCoyote() {
        return new CarteAnimalLogic("Coyote", 1, 2, 0, 4);
    }

    public static CarteVolanteLogic creerMoineau() {
        return new CarteVolanteLogic("Moineau", 2, 1, 1, 0);
    }

    public static CarteVolanteLogic creerCorbeau() {
        return new CarteVolanteLogic("Corbeau", 3, 2, 2, 0);
    }

    public static CarteAnimalLogic creerEcureuil() {
        return new CarteAnimalLogic("Ecureuil", 1, 0, 0, 0);
    }

    public static CarteAnimalLogic creerHermine() {
        return new CarteAnimalLogic("Hermine", 3, 1, 1, 0);
    }

    public static CarteAnimalLogic creerLouveteau() {
        CarteAnimalLogic louveteau = new CarteAnimalLogic("Louveteau", 1, 1, 1, 0);
        louveteau.ajouterPouvoir(new Croissance());
        return louveteau;
    }

    public static CarteAnimalLogic creerLoup() {
        return new CarteAnimalLogic("Loup", 2, 3, 2, 0);
    }

    public static CarteAnimalLogic creerPunaise() {
        CarteAnimalLogic punaise = new CarteAnimalLogic("Punaise", 2, 1, 0, 2);
        punaise.ajouterPouvoir(new Puant());
        return punaise;
    }

    public static CarteObstacleLogic creerRocher() {
        return new CarteObstacleLogic("Rocher", 5);
    }

    public static CarteObstacleLogic creerSapin() {
        return new CarteObstacleLogic("Sapin", 3);
    }

    public static CarteAnimalLogic creerElan() {
        CarteAnimalLogic elan = new CarteAnimalLogic("Elan", 4, 2, 2, 0);
        elan.ajouterPouvoir(new Coureur());
        return elan;
    }

    public static CarteAnimalLogic creerVipere() {
        CarteAnimalLogic vipere = new CarteAnimalLogic("Vipère", 1, 1, 2, 0);
        vipere.ajouterPouvoir(new ContactMortel());
        return vipere;
    }

    public static CarteAnimalLogic creerPorcEpic() {
        CarteAnimalLogic porc = new CarteAnimalLogic("Porc-épic", 2, 1, 1, 0);
        porc.ajouterPouvoir(new PiquePointues());
        return porc;
    }

    public static CarteAnimalLogic creerCarteParNom(String nom) {
        if (nom == null) return null;
        if (nom.equalsIgnoreCase("Chat")) return creerChat();
        if (nom.equalsIgnoreCase("Grizzly")) return creerGrizzly();
        if (nom.equalsIgnoreCase("Coyote")) return creerCoyote();
        if (nom.equalsIgnoreCase("Moineau")) return creerMoineau();
        if (nom.equalsIgnoreCase("Corbeau")) return creerCorbeau();
        if (nom.equalsIgnoreCase("Ecureuil")) return creerEcureuil();
        if (nom.equalsIgnoreCase("Hermine")) return creerHermine();
        if (nom.equalsIgnoreCase("Louveteau")) return creerLouveteau();
        if (nom.equalsIgnoreCase("Loup")) return creerLoup();
        if (nom.equalsIgnoreCase("Punaise")) return creerPunaise();
        if (nom.equalsIgnoreCase("Elan")) return creerElan();
        if (nom.equalsIgnoreCase("Vipère") || nom.equalsIgnoreCase("Vipere")) return creerVipere();
        if (nom.equalsIgnoreCase("Porc-épic") || nom.equalsIgnoreCase("Porc-epic")) return creerPorcEpic();
        return creerEcureuil();
    }

    public static CarteAnimalLogic creerCopieFraiche(CarteAnimalLogic template) {
        if (template == null) return null;
        return template.creerCopieFraiche();
    }
}