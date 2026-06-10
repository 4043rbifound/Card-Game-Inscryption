package Inscryption;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import Inscryption.Logique.*;

/**
 * Tests du pouvoir Nombreuses Vies (Chat).
 * Le Chat survit au sacrifice : il reste sur le plateau et ne libère pas l'emplacement.
 */
public class TestNombreusesVies {

    private PlateauLogic plateau;
    private JoueurLogic joueur;

    @Before
    public void setUp() {
        plateau = new PlateauLogic();
        joueur = new JoueurLogic();
    }

    @Test
    public void testChatSurvitAuSacrifice() {
        // Placer un Chat (Nombreuses Vies) sur B1 (index 0)
        CarteAnimalLogic chat = FabriqueCartes.creerChat();
        plateau.getCasesJoueur().get(0).placerCarte(chat);

        assertFalse("La case doit contenir le Chat avant le sacrifice",
                plateau.getCasesJoueur().get(0).estVide());

        // Exécuter le sacrifice : le Chat a Nombreuses Vies => il survit
        int sangGenere = chat.executerSacrifice(plateau.getCasesJoueur().get(0), joueur);

        // 1 sang est quand même généré
        assertEquals("Le sacrifice doit générer 1 sang même si la carte survit", 1, sangGenere);

        // Le Chat est toujours sur le plateau (il n'a pas libéré la case)
        assertFalse("Le Chat doit rester sur le plateau grâce à Nombreuses Vies",
                plateau.getCasesJoueur().get(0).estVide());

        assertEquals("C'est toujours le Chat sur la case",
                "Chat", plateau.getCasesJoueur().get(0).getCarteContenue().orElseThrow().getNom());

        // Aucun os supplémentaire (le Chat n'est pas mort)
        assertEquals("Aucun os ajouté puisque le Chat survit", 0, joueur.getReserveOs());
    }

    @Test
    public void testEcureuilMeurtAuSacrifice() {
        // Contrairement au Chat, un Écureuil (sans Nombreuses Vies) meurt au sacrifice
        CarteAnimalLogic ecureuil = FabriqueCartes.creerEcureuil();
        plateau.getCasesJoueur().get(0).placerCarte(ecureuil);

        int sangGenere = ecureuil.executerSacrifice(plateau.getCasesJoueur().get(0), joueur);

        assertEquals("Le sacrifice doit générer 1 sang", 1, sangGenere);
        assertTrue("L'écureuil doit avoir libéré la case", plateau.getCasesJoueur().get(0).estVide());
        assertEquals("1 os doit être ajouté après la mort de l'écureuil", 1, joueur.getReserveOs());
    }

    @Test
    public void testNombreusesViesNEmpechePasLaMortAuCombat() {
        // Nombreuses Vies ne protège que du sacrifice, pas des dégâts de combat
        CarteAnimalLogic chat = FabriqueCartes.creerChat(); // 0 ATT, 1 PV
        CarteAnimalLogic loup = FabriqueCartes.creerLoup(); // 3 ATT

        plateau.getCasesAdversaire().get(0).placerCarte(loup);
        plateau.getCasesJoueur().get(0).placerCarte(chat);

        // L'adversaire attaque : le loup inflige 3 dégâts au Chat (1 PV)
        plateau.resoudreAttaques(false);

        // Le Chat doit être mort (libéré de la case)
        assertTrue("Le Chat doit mourir au combat malgré Nombreuses Vies",
                plateau.getCasesJoueur().get(0).estVide());
    }
}
