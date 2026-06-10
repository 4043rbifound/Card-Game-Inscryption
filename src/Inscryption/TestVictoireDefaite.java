package Inscryption;

import static org.junit.Assert.*;
import org.junit.Test;
import Inscryption.Logique.*;

/**
 * Tests du déroulement global du jeu (gagner ou perdre).
 * Vérifie que la condition de fin de partie et de fin de jeu fonctionnent correctement.
 */
public class TestVictoireDefaite {

    // -----------------------------------------------------------------------
    // Fin de PARTIE (score relatif)
    // -----------------------------------------------------------------------

    @Test
    public void testPartieTermineeParVictoireJoueur() {
        ScoreLogic score = new ScoreLogic();
        score.ajouterPointsJoueur(5);

        assertTrue("La partie doit être terminée quand le joueur atteint +5", score.estPartieTerminee());
        assertEquals("Le gagnant doit être le Joueur", "Joueur", score.getGagnant());
    }

    @Test
    public void testPartieTermineeParVictoireAdversaire() {
        ScoreLogic score = new ScoreLogic();
        score.ajouterPointsAdversaire(5);

        assertTrue("La partie doit être terminée quand l'adversaire atteint +5", score.estPartieTerminee());
        assertEquals("Le gagnant doit être l'Adversaire", "Adversaire", score.getGagnant());
    }

    @Test
    public void testPartieNonTermineeA4Points() {
        ScoreLogic score = new ScoreLogic();
        score.ajouterPointsJoueur(4);

        assertFalse("La partie ne doit pas encore être terminée à 4 points", score.estPartieTerminee());
        assertNull("Pas encore de gagnant à 4 points", score.getGagnant());
    }

    // -----------------------------------------------------------------------
    // Attaque menant à la fin de partie
    // -----------------------------------------------------------------------

    @Test
    public void testAttaqueDirecteMenantALaVictoire() {
        PlateauLogic plateau = new PlateauLogic();
        ScoreLogic score = new ScoreLogic();

        // Un Corbeau (2 ATT, Volant) sur la ligne joueur, face à une case vide adverse
        CarteAnimalLogic corbeau = FabriqueCartes.creerCorbeau();
        plateau.getCasesJoueur().get(0).placerCarte(corbeau);

        // Le Corbeau est volant : attaque directe même si case adverse vide
        score.ajouterPointsJoueur(3); // score déjà à 3

        java.util.List<Integer> degats = plateau.resoudreAttaques(true);
        int total = 0;
        for (int d : degats) total += d;
        score.ajouterPointsJoueur(total); // +2 => score à 5

        assertTrue("La partie doit être terminée (score = 5)", score.estPartieTerminee());
        assertEquals("Joueur", score.getGagnant());
    }

    @Test
    public void testAttaqueAdversaireMenantALaDefaite() {
        PlateauLogic plateau = new PlateauLogic();
        ScoreLogic score = new ScoreLogic();

        // Un Grizzly (4 ATT) de l'adversaire face à une case joueur vide
        CarteAnimalLogic grizzly = FabriqueCartes.creerGrizzly();
        plateau.getCasesAdversaire().get(0).placerCarte(grizzly);

        score.ajouterPointsAdversaire(1); // score à -1

        java.util.List<Integer> degats = plateau.resoudreAttaques(false);
        int total = 0;
        for (int d : degats) total += d;
        score.ajouterPointsAdversaire(total); // +4 => score à -5

        assertTrue("La partie doit être terminée (score = -5)", score.estPartieTerminee());
        assertEquals("Adversaire", score.getGagnant());
    }

    // -----------------------------------------------------------------------
    // Collection et ajout de carte (fin de partie 2)
    // -----------------------------------------------------------------------

    @Test
    public void testAjoutCarteACollectionApresPartie2() {
        JoueurLogic joueur = new JoueurLogic();
        int tailleAvant = joueur.getCollection().size();

        // Simuler l'ajout d'une carte (comme fait GestionnairePartie après partie 2)
        CarteAnimalLogic grizzly = FabriqueCartes.creerGrizzly();
        joueur.ajouterCarteACollection(grizzly);

        assertEquals("La collection doit avoir une carte de plus", tailleAvant + 1, joueur.getCollection().size());
        assertTrue("Le Grizzly doit être dans la collection", joueur.getCollection().contains(grizzly));

        // La nouvelle partie doit inclure une copie du Grizzly dans le deck
        joueur.resetForNewPartie();

        // Vider la main puis piocher tout pour trouver le Grizzly
        boolean grizzlyTrouve = false;
        for (int i = 0; i < 30; i++) {
            joueur.piocherCartePrincipal();
        }
        for (CarteAnimalLogic c : joueur.getMain()) {
            if (c != null && c.getNom().equals("Grizzly")) {
                grizzlyTrouve = true;
                assertEquals(6, c.getPointsVieActuels());
                assertEquals(4, c.getPointsAttaque());
            }
        }
        assertTrue("Le Grizzly doit se trouver dans le deck pour la partie suivante", grizzlyTrouve);
    }
}
