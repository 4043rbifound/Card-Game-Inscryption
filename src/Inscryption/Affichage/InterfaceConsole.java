package Inscryption.Affichage;

import java.util.List;
import Inscryption.Logique.*;

public class InterfaceConsole {

    private String padRight(String s, int n) {
        if (s == null) s = "";
        while (s.length() < n) {
            s += " ";
        }
        return s;
    }

    private String centerText(String s, int width) {
        if (s == null) s = "";
        if (s.length() > width) {
            return s.substring(0, width);
        }
        int totalSpaces = width - s.length();
        int leftSpaces = totalSpaces / 2;
        int rightSpaces = totalSpaces - leftSpaces;
        String res = "";
        for (int i = 0; i < leftSpaces; i++) {
            res += " ";
        }
        res += s;
        for (int i = 0; i < rightSpaces; i++) {
            res += " ";
        }
        return res;
    }

    private String getShortPowerName(String fullName) {
        if (fullName == null) return "";
        if (fullName.equalsIgnoreCase("Nombreuses Vies")) return "Nb. Vies";
        if (fullName.equalsIgnoreCase("Contact Mortel")) return "Contact M.";
        if (fullName.equalsIgnoreCase("Pique Pointues") || fullName.equalsIgnoreCase("Piques pointues")) return "Piques";
        if (fullName.equalsIgnoreCase("Croissance")) return "Croiss.";
        if (fullName.equalsIgnoreCase("Coureur")) return "Coureur";
        if (fullName.equalsIgnoreCase("Puant")) return "Puant";
        return fullName;
    }

    private String[] getCardLines(CarteLogic carte, String emptyLabel) {
        String[] lines = new String[7];
        if (carte == null) {
            lines[0] = "*************";
            lines[1] = "*           *";
            lines[2] = "*           *";
            lines[3] = "*" + centerText(emptyLabel, 11) + "*";
            lines[4] = "*           *";
            lines[5] = "*           *";
            lines[6] = "*************";
        } else {
            lines[0] = "*-----------*";
            lines[1] = "|" + centerText(carte.getNom(), 11) + "|";
            lines[2] = "|-----------|";
            lines[3] = "|" + padRight(String.format(" PV: %d", carte.getPointsVieActuels()), 11) + "|";
            
            String attStr = carte.getLigneAttaque();
            lines[4] = "|" + padRight(attStr, 11) + "|";
            
            String pstr = carte.getLignePouvoir();
            if (!pstr.isEmpty()) {
                pstr = getShortPowerName(pstr);
            }
            lines[5] = "|" + centerText(pstr, 11) + "|";
            lines[6] = "*-----------*";
        }
        return lines;
    }

    public void afficherEtatJeu(PlateauLogic plateau, JoueurLogic joueur, ScoreLogic score, int partie, int tour, CarteLogic[] intentions) {
        System.out.println("\n    Partie " + partie);
        System.out.println("\n    Tour " + tour + ":\n");

        List<Emplacement> casesAdversaire = plateau.getCasesAdversaire();
        List<Emplacement> casesJoueur = plateau.getCasesJoueur();

        // 1. Intentions row
        String[][] intentionsLines = new String[4][7];
        for (int i = 0; i < 4; i++) {
            intentionsLines[i] = getCardLines(intentions != null ? intentions[i] : null, "");
        }
        for (int line = 0; line < 7; line++) {
            System.out.print("        ");
            for (int col = 0; col < 4; col++) {
                System.out.print(intentionsLines[col][line]);
                if (col < 3) System.out.print("   ");
            }
            System.out.println();
        }

        // 2. Arrows row
        System.out.println("              ||              ||              ||              ||");
        System.out.println("              \\/              \\/              \\/              \\/");

        // 3. Adversaire row (row A)
        String[][] advLines = new String[4][7];
        for (int i = 0; i < 4; i++) {
            advLines[i] = getCardLines(casesAdversaire.get(i).getCarteContenue(), "A" + (i + 1));
        }
        for (int line = 0; line < 7; line++) {
            if (line == 6) {
                System.out.print("Score   ");
            } else {
                System.out.print("        ");
            }
            for (int col = 0; col < 4; col++) {
                System.out.print(advLines[col][line]);
                if (col < 3) System.out.print("   ");
            }
            System.out.println();
        }

        // 4. Score value gap
        System.out.println(String.format("  %-6d", score.getValeurRelative()));

        // 5. Player row (row B)
        String[][] joueurLines = new String[4][7];
        for (int i = 0; i < 4; i++) {
            joueurLines[i] = getCardLines(casesJoueur.get(i).getCarteContenue(), "B" + (i + 1));
        }
        for (int line = 0; line < 7; line++) {
            System.out.print("        ");
            for (int col = 0; col < 4; col++) {
                System.out.print(joueurLines[col][line]);
                if (col < 3) System.out.print("   ");
            }
            System.out.println();
        }

        // 6. Main and Pioche row
        afficherMainEtPioche(joueur);
    }

    public void afficherPlateau(PlateauLogic plateau) {
        List<Emplacement> casesAdversaire = plateau.getCasesAdversaire();
        List<Emplacement> casesJoueur = plateau.getCasesJoueur();

        String[][] advLines = new String[4][7];
        for (int i = 0; i < 4; i++) {
            advLines[i] = getCardLines(casesAdversaire.get(i).getCarteContenue(), "A" + (i + 1));
        }
        System.out.println("    Plateau Adversaire:");
        for (int line = 0; line < 7; line++) {
            System.out.print("        ");
            for (int col = 0; col < 4; col++) {
                System.out.print(advLines[col][line]);
                if (col < 3) System.out.print("   ");
            }
            System.out.println();
        }

        System.out.println("              ||              ||              ||              ||");
        System.out.println("              \\/              \\/              \\/              \\/");

        String[][] joueurLines = new String[4][7];
        for (int i = 0; i < 4; i++) {
            joueurLines[i] = getCardLines(casesJoueur.get(i).getCarteContenue(), "B" + (i + 1));
        }
        System.out.println("    Plateau Joueur:");
        for (int line = 0; line < 7; line++) {
            System.out.print("        ");
            for (int col = 0; col < 4; col++) {
                System.out.print(joueurLines[col][line]);
                if (col < 3) System.out.print("   ");
            }
            System.out.println();
        }
    }

    public void afficherMainEtPioche(JoueurLogic joueur) {
        java.util.List<String> handLines = new java.util.ArrayList<>();
        handLines.add(String.format("Ressources disponibles : Sang : %d | Os : %d", joueur.getReserveSang(), joueur.getReserveOs()));
        handLines.add("Votre main :");
        
        CarteAnimalLogic[] main = joueur.getMain();
        int idx = 1;
        for (int i = 0; i < main.length; i++) {
            if (main[i] != null) {
                CarteAnimalLogic c = main[i];
                String pstr = c.getLignePouvoir();
                if (!pstr.isEmpty()) {
                    pstr = " Pouvoir: " + pstr;
                }
                String line = String.format("  %d. %-10s  PV:%-2d  Att:%-2d  Gouttes de sang:%-2d  Os:%-2d%s",
                        idx, c.getNom(), c.getPointsVieActuels(), c.getPointsAttaque(), c.getCoutSang(), c.getCoutOs(), pstr);
                handLines.add(line);
                idx++;
            }
        }
        if (handLines.size() == 1) {
            handLines.add("  (vide)");
        }

        int cardsInDeck = joueur.getNombreCartesDeck();
        String[] piocheLines = new String[7];
        piocheLines[0] = "                                            Pioche";
        piocheLines[1] = "                                        *-----------*";
        piocheLines[2] = "                                        |           |";
        piocheLines[3] = String.format("                                        |    %2d     |", cardsInDeck);
        piocheLines[4] = "                                        |  cartes   |";
        piocheLines[5] = "                                        |           |";
        piocheLines[6] = "                                        *-----------*";

        int maxLines = Math.max(handLines.size(), piocheLines.length);
        for (int i = 0; i < maxLines; i++) {
            String left = "";
            if (i < handLines.size()) {
                left = handLines.get(i);
            }
            if (i < piocheLines.length) {
                if (left.length() > 56) {
                    left = left.substring(0, 56);
                }
                System.out.println(padRight(left, 56) + piocheLines[i].substring(40));
            } else {
                System.out.println(left);
            }
        }
        System.out.println();
    }

    public void afficherMain(JoueurLogic joueur) {
        System.out.println("Votre main :");
        CarteAnimalLogic[] main = joueur.getMain();
        int idx = 1;
        for (int i = 0; i < main.length; i++) {
            if (main[i] != null) {
                CarteAnimalLogic c = main[i];
                String pstr = c.getLignePouvoir();
                if (!pstr.isEmpty()) {
                    pstr = " Pouvoir: " + pstr;
                }
                System.out.println(String.format("  %d. %-10s  PV:%-2d  Att:%-2d  Gouttes de sang:%-2d  Os:%-2d%s",
                        idx, c.getNom(), c.getPointsVieActuels(), c.getPointsAttaque(), c.getCoutSang(), c.getCoutOs(), pstr));
                idx++;
            }
        }
        if (idx == 1) {
            System.out.println("  (vide)");
        }
    }

    public void afficherMessages(List<String> messages) {
        if (messages != null) {
            for (String message : messages) {
                if (message != null) {
                    System.out.println("  " + message);
                }
            }
        }
    }

    public void afficherActionsPossibles() {
        System.out.println("Actions possibles: ");
        System.out.println("  [fin] Terminer votre tour");
        System.out.println("  [piocher] Piocher une carte");
        System.out.println("  [placer <numero carte> <position>] Placer une carte sur le plateau");
        System.out.println("  [sacrifier <position>] Sacrifier une creature sur la case indiquee (genere 1 sang)");
    }

    public void afficherMessageSimple(String message) {
        System.out.println(message);
    }

    public String lireSaisieAvecPrompt(String prompt) {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        System.out.print(prompt);
        if (scanner.hasNextLine()) {
            return scanner.nextLine();
        }
        return "";
    }

    public String lireSaisie() {
        return lireSaisieAvecPrompt("$ ");
    }

    public String demanderChoixPioche() {
        System.out.println("\nChoisissez votre pioche :");
        System.out.println("1. Deck principal");
        System.out.println("2. Pile d'ecureuils");
        return lireSaisieAvecPrompt("Choix (1 ou 2) : ");
    }

    public String demanderChoixNouvelleCarte(int partie) {
        System.out.println("\n==================================================");
        System.out.println("CHOIX D'UNE NOUVELLE CARTE (Partie " + partie + ")");
        System.out.println("==================================================");
        System.out.println("Choisissez une carte a ajouter a votre deck :");
        System.out.println("1. Elan (PV: 4, Att: 2, Cout Sang: 2, Pouvoir: Coureur)");
        System.out.println("2. Vipere (PV: 1, Att: 1, Cout Sang: 2, Pouvoir: Contact Mortel)");
        System.out.println("3. Porc-epic (PV: 2, Att: 1, Cout Sang: 1, Pouvoir: Piques pointues)");
        return lireSaisieAvecPrompt("Votre choix (1, 2 ou 3) : ");
    }

    public int demanderCartePlateauASacrifier(List<Emplacement> cases) {
        System.out.println("\n==================================================");
        System.out.println("PIERRE DE SACRIFICE");
        System.out.println("==================================================");
        System.out.println("Choisissez une de vos cartes sur le plateau a sacrifier pour recuperer son pouvoir :");
        for (int i = 0; i < cases.size(); i++) {
            Emplacement emp = cases.get(i);
            if (!emp.estVide()) {
                CarteLogic c = emp.getCarteContenue();
                String pstr = c.getLignePouvoir();
                if (!pstr.isEmpty()) {
                    pstr = " (Pouvoir: " + pstr + ")";
                }
                System.out.println((i + 1) + ". Case B" + (i + 1) + " : " + c.getNom() + pstr);
            } else {
                System.out.println((i + 1) + ". Case B" + (i + 1) + " : (vide)");
            }
        }
        String saisie = lireSaisieAvecPrompt("Entrez la position (1 a 4 pour B1 a B4) : ");
        try {
            return Integer.parseInt(saisie.trim()) - 1;
        } catch (Exception e) {
            return -1;
        }
    }

    public int demanderCarteCibleTransfert(List<CarteAnimalLogic> collection, String nomPouvoir) {
        System.out.println("\nChoisissez la carte de votre collection qui va recevoir le pouvoir " + nomPouvoir + " :");
        for (int i = 0; i < collection.size(); i++) {
            CarteAnimalLogic c = collection.get(i);
            String pstr = c.getLignePouvoir();
            if (!pstr.isEmpty()) {
                pstr = " (Pouvoir: " + pstr + ")";
            }
            System.out.println((i + 1) + ". " + c.getNom() + pstr);
        }
        String saisie = lireSaisieAvecPrompt("Entrez le numero de la carte cible : ");
        try {
            return Integer.parseInt(saisie.trim()) - 1;
        } catch (Exception e) {
            return -1;
        }
    }
}