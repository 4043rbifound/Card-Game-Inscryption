package Inscryption;

import static org.junit.Assert.*;
import org.junit.Test;
import Inscryption.Logique.Emplacement;
import Inscryption.Logique.FabriqueCartes;
import Inscryption.Logique.CarteObstacleLogic;
import Inscryption.Logique.CarteAnimalLogic;

public class TestEmplacement {

    @Test
    public void testGestionEmplacement() {
        Emplacement emp = new Emplacement(2);
        assertEquals(2, emp.getPosition());
        assertTrue(emp.estVide());
        assertNull(emp.getCarteContenue());

        // Placement d'un animal
        CarteAnimalLogic hermine = FabriqueCartes.creerHermine();
        emp.placerCarte(hermine);
        assertFalse(emp.estVide());
        assertEquals(hermine, emp.getCarteContenue());

        // Libération
        emp.liberer();
        assertTrue(emp.estVide());
        assertNull(emp.getCarteContenue());

        // Placement d'un obstacle
        CarteObstacleLogic rocher = new CarteObstacleLogic("Rocher", 5);
        emp.placerCarte(rocher);
        assertFalse(emp.estVide());
        assertTrue(emp.getCarteContenue().estObstacle());
    }
}