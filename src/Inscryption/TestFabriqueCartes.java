package Inscryption.Tests;

import static org.junit.Assert.*;
import org.junit.Test;
import Inscryption.Logique.FabriqueCartes;
import Inscryption.Logique.CarteAnimalLogic;
import Inscryption.Logique.Pouvoir;

public class TestFabriqueCartes {

    @Test
    public void testCaracteristiquesEcureuil() {
        CarteAnimalLogic ecureuil = FabriqueCartes.creerEcureuil();
        assertEquals("Ecureuil", ecureuil.getNom());
        assertEquals(0, ecureuil.getPointsAttaque());
        assertEquals(1, ecureuil.getPointsVieMax());
        assertEquals(0, ecureuil.getCoutSang());
        assertEquals(0, ecureuil.getCoutOs());
        assertFalse(ecureuil.estVolant());
    }

    @Test
    public void testCaracteristiquesLouveteauEtPouvoir() {
        CarteAnimalLogic louveteau = FabriqueCartes.creerLouveteau();
        assertEquals("Louveteau", louveteau.getNom());
        assertEquals(1, louveteau.getPointsAttaque());
        assertEquals(1, louveteau.getPointsVieMax());
        assertEquals(1, louveteau.getCoutSang());

        // Phase 2: Vérifier la présence de Croissance
        boolean aCroissance = false;
        for (Pouvoir p : louveteau.getPouvoirs()) {
            if (p.getNom().equalsIgnoreCase("Croissance")) {
                aCroissance = true;
            }
        }
        assertTrue("Le louveteau doit posséder le pouvoir Croissance", aCroissance);
    }
}