package Inscryption.Affichage;

import java.util.Scanner;
import Inscryption.Logique.CarteAnimalLogic;
import Inscryption.Logique.CarteLogic;
import Inscryption.Logique.Emplacement;
import Inscryption.Logique.JoueurLogic;
import Inscryption.Logique.PlateauLogic;
import Inscryption.Logique.ScoreLogic;

public class InterfaceConsole {

    private Scanner m_scanner;

    public InterfaceConsole() {
        this.m_scanner = new Scanner(System.in);
    }

    public void afficherEtatJeu(
            PlateauLogic plateau,
            JoueurLogic joueur,
            ScoreLogic score,
            int tour,
            CarteLogic[] intentions) {

        System.out.println();
        System.out.println("============================");
        System.out.println("TOUR " + tour);
        System.out.println("============================");

        System.out.println("Score : " + score.getValeurRelative());
        System.out.println();

        System.out.println("Intentions adverses : ");

        for (int i = 0; i < intentions.length; i++) {

            if (intentions[i] == null) {
                System.out.print("[VIDE] ");
            } else {
                System.out.print("[" + intentions[i].getNom() + "] ");
            }
        }

        System.out.println();
        System.out.println();

        afficherPlateau(plateau);

        System.out.println();

        afficherMain(joueur);

        System.out.println();

        System.out.println("Commandes possibles :");
        System.out.println("- piocher");
        System.out.println("- fin");
        System.out.println("- placer <indexCarte> <position>");
    }

    public void afficherPlateau(PlateauLogic plateau) {

        Emplacement[] casesAdversaire = plateau.getCasesAdversaire();
        Emplacement[] casesJoueur = plateau.getCasesJoueur();

        System.out.println("Plateau adverse :");

        for (int i = 0; i < casesAdversaire.length; i++) {

            CarteLogic carte = casesAdversaire[i].getCarteContenue();
    
            if (carte == null) {
                System.out.print("[VIDE] ");
            } else {
                System.out.print("[" + carte.getNom() + " PV:" + carte.getPointsVieActuels() + "] ");
            }
        }

        System.out.println();

        System.out.println("Plateau joueur :");

        for (int i = 0; i < casesJoueur.length; i++) {

            CarteLogic carte = casesJoueur[i].getCarteContenue();

            if (carte == null) {
                System.out.print("[VIDE] ");
            } else {
                System.out.print("[" + carte.getNom() + " PV:" + carte.getPointsVieActuels() + "] ");
            }
        }

        System.out.println();
    }

    public void afficherMain(JoueurLogic joueur) {

        System.out.println("Main du joueur :");

        for (int i = 0; i < joueur.getMain().length; i++) {

            CarteAnimalLogic carte = joueur.getMain()[i];

            System.out.println(
                    (i + 1)
                            + ". "
                            + carte.getNom()
                            + " | PV: "
                            + carte.getPointsVieActuels()
                            + " | ATT: "
                            + carte.getPointsAttaque());
        }
    }

    public void afficherMessages(String[] messages) {

        for (int i = 0; i < messages.length; i++) {
            System.out.println(messages[i]);
        }
    }

    public String lireSaisie() {

        System.out.print("> ");
        return this.m_scanner.nextLine();
    }
}