package Inscryption;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import Inscryption.Logique.*;

public class TestAdversaireLogic {
    private PlateauLogic plateau;
    private AdversaireLogic adversaire;

    @Before
    public void setUp() {
        plateau = new PlateauLogic();
        // L'adversaire est initialisé avec un PlanJeu standard
        adversaire = new AdversaireLogic(new PlanJeu());
    }

    @Test
    public void testObtenirIntention() {
        // CORRECTION ICI : Ajout du point entre 'adversaire' et 'obtenirIntention'
        CarteLogic[] intentions = adversaire.obtenirIntention(1);
        assertNotNull(intentions);
        assertEquals("Louveteau", intentions[0].getNom());
        assertEquals("Moineau", intentions[2].getNom());
    }

    @Test
    public void testJouerTourSurPlateauVide() {
        // Au tour 1, l'adversaire doit placer un Louveteau en case 0 et un Moineau en case 2
        adversaire.jouerTour(1, plateau);

        // Vérification sur les cases de l'adversaire (List<Emplacement>)
        assertFalse("La case 0 ne doit plus être vide", plateau.getCasesAdversaire().get(0).estVide());
        assertEquals("Louveteau", plateau.getCasesAdversaire().get(0).getCarteContenue().getNom());

        assertTrue("La case 1 doit rester vide", plateau.getCasesAdversaire().get(1).estVide());

        assertFalse("La case 2 ne doit plus être vide", plateau.getCasesAdversaire().get(2).estVide());
        assertEquals("Moineau", plateau.getCasesAdversaire().get(2).getCarteContenue().getNom());

        assertTrue("La case 3 doit rester vide", plateau.getCasesAdversaire().get(3).estVide());
    }

    @Test
    public void testJouerTourNeEcrasePasUneCarteExistante() {
        // On pré-place manuellement un Grizzly sur la case 1 de l'adversaire
        CarteAnimalLogic grizzlyDeDefense = FabriqueCartes.creerGrizzly();
        plateau.getCasesAdversaire().get(1).placerCarte(grizzlyDeDefense);

        // Au tour 2, le PlanJeu prévoit un Loup sur la case 1
        adversaire.jouerTour(2, plateau);

        // L'adversaire ne doit pas avoir écrasé le Grizzly car la case n'était pas vide
        assertNotNull(plateau.getCasesAdversaire().get(1).getCarteContenue());
        assertEquals("Le Grizzly doit être resté sur la case 1",
                "Grizzly", plateau.getCasesAdversaire().get(1).getCarteContenue().getNom());
    }
}