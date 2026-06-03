package Inscryption.Affichage;

import java.util.List;
import Inscryption.Logique.*;

public class InterfaceConsole {

    public void afficherEtatJeu(PlateauLogic plateau, JoueurLogic joueur, ScoreLogic score, int tour, CarteLogic[] intentions) {
        System.out.println("\n==================================================================");
        System.out.println(" TOURNÉE N°" + tour + " | SCORE ACTUEL : " + score.getValeurRelative());
        System.out.println("==================================================================");

        System.out.print("🔮 Intentions de l'IA : ");
        for (int i = 0; i < intentions.length; i++) {
            if (intentions[i] != null) {
                System.out.print("[Case " + i + ": " + intentions[i].getNom() + "] ");
            }
        }
        System.out.println("\n------------------------------------------------------------------");

        afficherPlateau(plateau);
        System.out.println("------------------------------------------------------------------");
        afficherMain(joueur);
        System.out.println("==================================================================");
    }

    public void afficherPlateau(PlateauLogic plateau) {
        List<Emplacement> casesAdversaire = plateau.getCasesAdversaire();
        List<Emplacement> casesJoueur = plateau.getCasesJoueur();

        System.out.print("☠️ Plateau Adversaire : ");
        for (int i = 0; i < casesAdversaire.size(); i++) {
            CarteLogic carte = casesAdversaire.get(i).getCarteContenue();
            if (carte == null) {
                System.out.print("[   .   ] ");
            } else {
                System.out.print("[" + carte.getNom() + " " + carte.getPointsVieActuels() + "PV] ");
            }
        }
        System.out.println();

        System.out.print("🛡️ Plateau Joueur     : ");
        for (int i = 0; i < casesJoueur.size(); i++) {
            CarteLogic carte = casesJoueur.get(i).getCarteContenue();
            if (carte == null) {
                System.out.print("[   .   ] ");
            } else {
                System.out.print("[" + carte.getNom() + " " + carte.getPointsVieActuels() + "PV] ");
            }
        }
        System.out.println();
    }

    public void afficherMain(JoueurLogic joueur) {
        System.out.print("🖐️ Votre Main : ");
        CarteAnimalLogic[] main = joueur.getMain();
        boolean mainVide = true;
        for (int i = 0; i < main.length; i++) {
            if (main[i] != null) {
                // On affiche le coût en gouttes de sang (ex: 2🩸)
                String sang = main[i].getCoutSang() > 0 ? " (" + main[i].getCoutSang() + "🩸)" : " (Gratuit)";
                System.out.print((i + 1) + ". " + main[i].getNom() + sang + " [" + main[i].getPointsAttaque() + "/" + main[i].getPointsVieActuels() + "]  ");
                mainVide = false;
            }
        }
        if (mainVide) {
            System.out.print("(vide)");
        }
        System.out.println();
    }

    public void afficherMessages(List<String> messages) {
        if (messages != null) {
            for (String message : messages) {
                if (message != null) {
                    System.out.println("⚔️ " + message);
                }
            }
        }
    }

    public String lireSaisie() {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        System.out.print("👉 Entrez une commande : ");
        if (scanner.hasNextLine()) {
            return scanner.nextLine();
        }
        return "fin";
    }
}