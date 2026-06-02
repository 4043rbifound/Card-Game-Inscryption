package Inscryption;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import Inscryption.Logique.*;

public class TestPlanJeu {
    private PlanJeu planJeu;

    @Before
    public void setUp() {
        planJeu = new PlanJeu();
    }

    @Test
    public void testCartesTourUn() {
        CarteLogic[] cartesTour1 = planJeu.getCartesPourTour(1);

        assertNotNull("Le tableau du tour 1 ne doit pas être nul", cartesTour1);
        assertEquals("Le tableau doit contenir 4 emplacements", 4, cartesTour1.length);

        // Vérification des cartes spécifiées dans initialiserPlan()
        assertNotNull("La case 0 doit contenir un Louveteau", cartesTour1[0]);
        assertEquals("Louveteau", cartesTour1[0].getNom());

        assertNull("La case 1 doit être vide", cartesTour1[1]);

        assertNotNull("La case 2 doit contenir un Moineau", cartesTour1[2]);
        assertEquals("Moineau", cartesTour1[2].getNom());

        assertNull("La case 3 doit être vide", cartesTour1[3]);
    }

    @Test
    public void testCartesTourDeux() {
        CarteLogic[] cartesTour2 = planJeu.getCartesPourTour(2);

        assertNull("La case 0 doit être vide au tour 2", cartesTour2[0]);
        assertNotNull("La case 1 doit contenir un Loup au tour 2", cartesTour2[1]);
        assertEquals("Loup", cartesTour2[1].getNom());
    }

    @Test
    public void testTourInvalide() {
        // Test aux limites (Tour 0 ou négatif)
        CarteLogic[] cartesTour0 = planJeu.getCartesPourTour(0);
        assertEquals("Un tour invalide doit retourner un tableau vide de taille 4", 4, cartesTour0.length);
        for (CarteLogic carte : cartesTour0) {
            assertNull("Toutes les cases doivent être nulles", carte);
        }

        // Test au-delà de la limite des 10 tours configurés
        CarteLogic[] cartesTour11 = planJeu.getCartesPourTour(11);
        assertEquals(4, cartesTour11.length);
        for (CarteLogic carte : cartesTour11) {
            assertNull(carte);
        }
    }
}