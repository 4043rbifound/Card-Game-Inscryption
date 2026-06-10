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
        assertTrue(emp.getCarteContenue().isEmpty());

        // Placement d'un animal
        CarteAnimalLogic hermine = FabriqueCartes.creerHermine();
        emp.placerCarte(hermine);
        assertFalse(emp.estVide());
        assertEquals(hermine, emp.getCarteContenue().orElseThrow());

        // Libération
        emp.liberer();
        assertTrue(emp.estVide());
        assertTrue(emp.getCarteContenue().isEmpty());

        // Placement d'un obstacle : on vérifie le comportement (attaque = 0), pas le type
        CarteObstacleLogic rocher = new CarteObstacleLogic("Rocher", 5);
        emp.placerCarte(rocher);
        assertFalse(emp.estVide());
        // Un obstacle n'attaque pas : son attaque vaut 0
        assertEquals(0, emp.getCarteContenue().orElseThrow().getPointsAttaque());
        // Un obstacle ne peut pas se déplacer : ses tours sur plateau restent à 0 après incrémentation
        emp.getCarteContenue().orElseThrow().incrementerToursSurPlateau();
        assertEquals(0, emp.getCarteContenue().orElseThrow().getToursSurPlateau());
    }
}