package Inscryption;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import Inscryption.Logique.ScoreLogic;

public class TestScoreLogic {
    private ScoreLogic score;

    @Before
    public void setUp() {
        score = new ScoreLogic();
    }

    @Test
    public void testInitialisation() {
        assertEquals(0, score.getValeurRelative());
        assertFalse(score.estPartieTerminee());
        assertNull(score.getGagnant());
    }

    @Test
    public void testAjouterPointsJoueur() {
        score.ajouterPointsJoueur(3);
        assertEquals(3, score.getValeurRelative());
        assertFalse(score.estPartieTerminee());

        score.ajouterPointsJoueur(2); // Atteint 5
        assertEquals(5, score.getValeurRelative());
        assertTrue(score.estPartieTerminee());
        assertEquals("Joueur", score.getGagnant());
    }

    @Test
    public void testAjouterPointsAdversaire() {
        score.ajouterPointsAdversaire(4);
        assertEquals(-4, score.getValeurRelative());
        assertFalse(score.estPartieTerminee());

        score.ajouterPointsAdversaire(1); // Atteint -5
        assertEquals(-5, score.getValeurRelative());
        assertTrue(score.estPartieTerminee());
        assertEquals("Adversaire", score.getGagnant());
    }
}