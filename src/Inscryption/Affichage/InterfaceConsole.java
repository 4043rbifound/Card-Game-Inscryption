package Inscryption.Affichage;

import java.util.List;
import Inscryption.Logique.*;

public class InterfaceConsole {

    public void afficherEtatJeu(PlateauLogic plateau, JoueurLogic joueur, ScoreLogic score, int tour, CarteLogic[] intentions) {
        System.out.println("\n========================================");
        System.out.println("TOURNÉE N°" + tour + " | SCORE ACTUEL : " + score.getValeurRelative());
        System.out.println("========================================");

        // Affichage des intentions de l'adversaire
        System.out.print("Intentions de l'IA pour ce tour : ");
        for (int i = 0; i < intentions.length; i++) {
            if (intentions[i] != null) {
                System.out.print("[Case " + i + ": " + intentions[i].getNom() + "] ");
            }
        }
        System.out.println("\n----------------------------------------");

        afficherPlateau(plateau);
        System.out.println("----------------------------------------");
        afficherMain(joueur);
        System.out.println("========================================");
        System.out.print("Entrez une commande (ex: 'placer 3 0', 'piocher', 'fin') : ");
    }

    public void afficherPlateau(PlateauLogic plateau) {
        List<Emplacement> casesAdversaire = plateau.getCasesAdversaire();
        List<Emplacement> casesJoueur = plateau.getCasesJoueur();

        System.out.print("Plateau Adversaire : ");
        for (int i = 0; i < casesAdversaire.size(); i++) {
            CarteLogic carte = casesAdversaire.get(i).getCarteContenue();
            if (carte == null) {
                System.out.print("[   .   ] ");
            } else {
                System.out.print("[" + carte.getNom() + " " + carte.getPointsVieActuels() + "PV] ");
            }
        }
        System.out.println();

        System.out.print("Plateau Joueur     : ");
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
        System.out.print("Votre Main : ");
        CarteAnimalLogic[] main = joueur.getMain();
        boolean mainVide = true;
        for (int i = 0; i < main.length; i++) {
            if (main[i] != null) {
                System.out.print((i + 1) + ". " + main[i].getNom() + " (" + main[i].getPointsAttaque() + " ATK/" + main[i].getPointsVieActuels() + " PV)  ");
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
        if (scanner.hasNextLine()) {
            return scanner.nextLine();
        }
        return "fin";
    }
}