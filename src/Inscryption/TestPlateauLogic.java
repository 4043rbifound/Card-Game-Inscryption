package Inscryption;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import Inscryption.Logique.*;

public class TestPlateauLogic {
    private PlateauLogic plateau;
    private ScoreLogic score;

    @Before
    public void setUp() {
        plateau = new PlateauLogic();
        score = new ScoreLogic();
    }

    @Test
    public void testAttaqueDirecteEtVolant() {
        // Un moineau (1 ATT, Volant) face à un loup adverse
        CarteAnimalLogic moineau = FabriqueCartes.creerMoineau();
        CarteAnimalLogic loupAdverse = FabriqueCartes.creerLoup();

        // MODIFICATION ICI : .get(0) au lieu de [0]
        plateau.getCasesJoueur().get(0).placerCarte(moineau);
        plateau.getCasesAdversaire().get(0).placerCarte(loupAdverse);

        // Volant doit ignorer la carte en face et impacter le score directement
        plateau.resoudreAttaques(true, score);
        assertEquals(1, score.getValeurRelative());
        assertEquals(2, loupAdverse.getPointsVieActuels()); // Indemne
    }

    @Test
    public void testCombatStandardContreObstacle() {
        CarteAnimalLogic loup = FabriqueCartes.creerLoup(); // 3 ATT
        CarteObstacleLogic rocher = new CarteObstacleLogic("Rocher", 5);

        // MODIFICATION ICI : .get(1) au lieu de [1]
        plateau.getCasesJoueur().get(1).placerCarte(loup);
        plateau.getCasesAdversaire().get(1).placerCarte(rocher);

        plateau.resoudreAttaques(true, score);
        assertEquals(2, rocher.getPointsVieActuels()); // 5 - 3 = 2
        assertEquals(0, score.getValeurRelative());    // Pas d'attaque directe
    }

    @Test
    public void testPouvoirPuant() {
        CarteAnimalLogic loup = FabriqueCartes.creerLoup(); // 3 ATT
        CarteAnimalLogic punaise = FabriqueCartes.creerPunaise(); // A le pouvoir Puant

        // MODIFICATION ICI : .get(0) au lieu de [0]
        plateau.getCasesJoueur().get(0).placerCarte(loup);
        plateau.getCasesAdversaire().get(0).placerCarte(punaise);

        // Le loup subit Puant de la punaise en face, ses dégâts tombent à (3 - 1) = 2
        plateau.resoudreAttaques(true, score);
        assertEquals(0, punaise.getPointsVieActuels()); // 2 PV - 2 dégâts = Morte
    }

    @Test
    public void testPouvoirContactMortel() {
        // Utilisation d'une Vipère (Contact Mortel) contre un Grizzly (6 PV)
        CarteAnimalLogic vipere = new CarteAnimalLogic("Vipère", 1, 1, 2, 0, false);
        vipere.ajouterPouvoir(new ContactMortel());
        CarteAnimalLogic grizzly = FabriqueCartes.creerGrizzly();

        // MODIFICATION ICI : .get(2) au lieu de [2]
        plateau.getCasesJoueur().get(2).placerCarte(vipere);
        plateau.getCasesAdversaire().get(2).placerCarte(grizzly);

        plateau.resoudreAttaques(true, score);
        assertTrue("Le grizzly doit mourir instantanément suite aux dégâts de la Vipère", grizzly.estMorte());
    }

    @Test
    public void testPouvoirPiquesPointues() {
        CarteAnimalLogic hermine = FabriqueCartes.creerHermine(); // 1 ATT, 3 PV
        CarteAnimalLogic porcepict = new CarteAnimalLogic("Porc-épic", 2, 1, 1, 0, false);
        porcepict.ajouterPouvoir(new PiquePointues());

        // MODIFICATION ICI : .get(0) au lieu de [0]
        plateau.getCasesJoueur().get(0).placerCarte(hermine);
        plateau.getCasesAdversaire().get(0).placerCarte(porcepict);

        plateau.resoudreAttaques(true, score);
        assertEquals(2, hermine.getPointsVieActuels()); // A pris 1 contre-coup de piques
    }

    @Test
    public void testPouvoirCroissance() {
        CarteAnimalLogic louveteau = FabriqueCartes.creerLouveteau();
        // MODIFICATION ICI : .get(0) au lieu de [0]
        plateau.getCasesJoueur().get(0).placerCarte(louveteau);

        // Tour 1 début (Vérification de la casse sur AppliquerEffetsDebutTour)
        plateau.AppliquerEffetsDebutTour(true);
        assertEquals(1, louveteau.getPointsAttaque()); // Pas encore évolué (1er tour)

        // Tour 2 début
        plateau.AppliquerEffetsDebutTour(true);
        // MODIFICATION ICI : .get(0) au lieu de [0]
        CarteAnimalLogic carteApresEvolution = (CarteAnimalLogic) plateau.getCasesJoueur().get(0).getCarteContenue();
        assertEquals(3, carteApresEvolution.getPointsAttaque()); // Évolué en Loup (3 ATT / 2 PV)
    }
}