package Inscryption.Logique;

public class FabriqueCartes {

    public static CarteAnimalLogic creerChat() {
        CarteAnimalLogic chat = new CarteAnimalLogic("Chat", 1, 0, 1, 0, false);
        chat.ajouterPouvoir(new NombreusesVies()); // Ajout du pouvoir Phase 2
        return chat;
    }

    public static CarteAnimalLogic creerGrizzly() {
        return new CarteAnimalLogic("Grizzly", 6, 4, 3, 0, false);
    }

    public static CarteAnimalLogic creerCoyote() {
        return new CarteAnimalLogic("Coyote", 1, 2, 0, 4, false);
    }

    public static CarteAnimalLogic creerMoineau() {
        return new CarteAnimalLogic("Moineau", 2, 1, 1, 0, true);
    }

    public static CarteAnimalLogic creerCorbeau() {
        return new CarteAnimalLogic("Corbeau", 3, 2, 2, 0, true);
    }

    public static CarteAnimalLogic creerEcureuil() {
        return new CarteAnimalLogic("Ecureuil", 1, 0, 0, 0, false);
    }

    public static CarteAnimalLogic creerHermine() {
        return new CarteAnimalLogic("Hermine", 3, 1, 1, 0, false);
    }

    public static CarteAnimalLogic creerLouveteau() {
        CarteAnimalLogic louveteau = new CarteAnimalLogic("Louveteau", 1, 1, 1, 0, false);
        louveteau.ajouterPouvoir(new Croissance()); // FIX : Ajout du pouvoir requis par le test !
        return louveteau;
    }

    public static CarteAnimalLogic creerLoup() {
        return new CarteAnimalLogic("Loup", 2, 3, 2, 0, false);
    }

    public static CarteAnimalLogic creerPunaise() {
        CarteAnimalLogic punaise = new CarteAnimalLogic("Punaise", 2, 1, 0, 2, false);
        punaise.ajouterPouvoir(new Puant()); // Ajout du pouvoir Phase 2
        return punaise;
    }
}