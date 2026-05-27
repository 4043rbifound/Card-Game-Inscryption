package Inscryption.Affichage;

public class AnalyseurSasie {
    public AnalyseurSaisie() {}

    public Commande interpreter(String saisie) {

        if (saisie == null || saisie.isEmpty()) {
            return null;
        }

        String[] morceaux = saisie.split(" ");

        String action = morceaux[0].toLowerCase();

        if (action.equals("fin")) {
            return new Commande("fin");
        }

        if (action.equals("piocher")) {
            return new Commande("piocher");
        }

        if (action.equals("placer")) {

            if (morceaux.length != 3) {
                return null;
            }

            try {
                int indexCarte = Integer.parseInt(morceaux[1]);
                int position = Integer.parseInt(morceaux[2]);

                return new Commande("placer", indexCarte, position);

            } catch (NumberFormatException e) {
                return null;
            }
        }

        return null;
    }
}
