package Inscryption.Affichage;

public class AnalyseurSaisie {
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
                String posStr = morceaux[2].toUpperCase();
                int position;
                if (posStr.equals("B1")) {
                    position = 0;
                } else if (posStr.equals("B2")) {
                    position = 1;
                } else if (posStr.equals("B3")) {
                    position = 2;
                } else if (posStr.equals("B4")) {
                    position = 3;
                } else {
                    position = Integer.parseInt(posStr);
                }

                return new Commande("placer", indexCarte, position);

            } catch (NumberFormatException e) {
                return null;
            }
        }

        if (action.equals("sacrifier")) {
            if (morceaux.length != 2) {
                return null;
            }
            try {
                String posStr = morceaux[1].toUpperCase();
                int position;
                if (posStr.equals("B1")) {
                    position = 0;
                } else if (posStr.equals("B2")) {
                    position = 1;
                } else if (posStr.equals("B3")) {
                    position = 2;
                } else if (posStr.equals("B4")) {
                    position = 3;
                } else {
                    position = Integer.parseInt(posStr);
                }
                return new Commande("sacrifier", 0, position);
            } catch (NumberFormatException e) {
                return null;
            }
        }

        return null;
    }
}
