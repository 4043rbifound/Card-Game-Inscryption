package Inscryption.Logique;

public class FabriqueCartes {

    public static CarteAnimalLogic creerChat() {
        return new CarteAnimalLogic("Chat", 1, 0, 1, 0);
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
        return new CarteAnimalLogic("Louveteau", 1, 1, 1, 0);
    }

    public static CarteAnimalLogic creerLoup() {
        return new CarteAnimalLogic("Loup", 2, 3, 2, 0);
    }

    public static CarteAnimalLogic creerPunaise() {
        return new CarteAnimalLogic("Punaise", 2, 1, 0, 2);
    }

    public static CarteObstacleLogic creerRocher() {
        return new CarteObstacleLogic("Rocher", 5);
    }

    public static CarteObstacleLogic creerSapin() {
        return new CarteObstacleLogic("Sapin", 3);
    }
}