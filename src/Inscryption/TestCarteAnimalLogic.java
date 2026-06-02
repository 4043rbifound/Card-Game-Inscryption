package Inscryption.Tests;

import static org.junit.Assert.*;
import org.junit.Test;
import Inscryption.Logique.FabriqueCartes;
import Inscryption.Logique.CarteAnimalLogic;
import Inscryption.Logique.Puant;

public class TestCarteAnimalLogic {

    @Test
    public void testDegatsEtMort() {
        CarteAnimalLogic hermine = FabriqueCartes.creerHermine(); // 3 PV
        assertFalse(hermine.estMorte());

        hermine.recevoirDegats(2);
        assertEquals(1, hermine.getPointsVieActuels());
        assertFalse(hermine.estMorte());

        hermine.recevoirDegats(2); // Dépasse les PV restants
        assertEquals(0, hermine.getPointsVieActuels());
        assertTrue(hermine.estMorte());
    }

    @Test
    public void testGestionDynamiquePouvoirs() {
        CarteAnimalLogic coyote = FabriqueCartes.creerCoyote();
        assertTrue(coyote.getPouvoirs().isEmpty());

        Puant puant = new Puant();
        coyote.ajouterPouvoir(puant);
        assertEquals(1, coyote.getPouvoirs().size());
        assertEquals("Puant", coyote.getPouvoirs().get(0).getNom());

        // Test de retrait des pouvoirs (Pierre de sacrifice)
        var anciensPouvoirs = coyote.retirerPouvoirs();
        assertEquals(1, anciensPouvoirs.size());
        assertTrue(coyote.getPouvoirs().isEmpty());
    }
}