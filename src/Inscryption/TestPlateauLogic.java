package Inscryption;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import Inscryption.Logique.*;

import java.util.List;

public class TestPlateauLogic {
    private PlateauLogic plateau;
    private ScoreLogic score;

    @Before
    public void setUp() {
        plateau = new PlateauLogic();
        score = new ScoreLogic();
    }

    /** Somme tous les dégâts directs d'une résolution d'attaque. */
    private int totalDegats(List<Integer> degatsParCase) {
        int total = 0;
        for (int d : degatsParCase) total += d;
        return total;
    }

    @Test
    public void testAttaqueDirecteEtVolant() {
        // Un moineau (1 ATT, Volant) face à un loup adverse
        CarteAnimalLogic moineau = FabriqueCartes.creerMoineau();
        CarteAnimalLogic loupAdverse = FabriqueCartes.creerLoup();

        plateau.getCasesJoueur().get(0).placerCarte(moineau);
        plateau.getCasesAdversaire().get(0).placerCarte(loupAdverse);

        // Volant doit ignorer la carte en face et impacter le score directement
        List<Integer> degats = plateau.resoudreAttaques(true);
        int total = totalDegats(degats);
        score.ajouterPointsJoueur(total);

        assertEquals(1, score.getValeurRelative());
        assertEquals(2, loupAdverse.getPointsVieActuels()); // Indemne
    }

    @Test
    public void testCombatStandardContreObstacle() {
        CarteAnimalLogic loup = FabriqueCartes.creerLoup(); // 3 ATT
        CarteObstacleLogic rocher = new CarteObstacleLogic("Rocher", 5);

        plateau.getCasesJoueur().get(1).placerCarte(loup);
        plateau.getCasesAdversaire().get(1).placerCarte(rocher);

        List<Integer> degats = plateau.resoudreAttaques(true);
        int total = totalDegats(degats);
        score.ajouterPointsJoueur(total);

        assertEquals(2, rocher.getPointsVieActuels()); // 5 - 3 = 2
        assertEquals(0, score.getValeurRelative());    // Pas d'attaque directe
    }

    @Test
    public void testPouvoirPuant() {
        CarteAnimalLogic loup = FabriqueCartes.creerLoup(); // 3 ATT
        CarteAnimalLogic punaise = FabriqueCartes.creerPunaise(); // A le pouvoir Puant

        plateau.getCasesJoueur().get(0).placerCarte(loup);
        plateau.getCasesAdversaire().get(0).placerCarte(punaise);

        // Le loup subit Puant de la punaise en face, ses dégâts tombent à (3 - 1) = 2
        plateau.resoudreAttaques(true);
        assertEquals(0, punaise.getPointsVieActuels()); // 2 PV - 2 dégâts = Morte
    }

    @Test
    public void testPouvoirContactMortel() {
        // Utilisation d'une Vipère (Contact Mortel) contre un Grizzly (6 PV)
        CarteAnimalLogic vipere = new CarteAnimalLogic("Vipère", 1, 1, 2, 0);
        vipere.ajouterPouvoir(new ContactMortel());
        CarteAnimalLogic grizzly = FabriqueCartes.creerGrizzly();

        plateau.getCasesJoueur().get(2).placerCarte(vipere);
        plateau.getCasesAdversaire().get(2).placerCarte(grizzly);

        plateau.resoudreAttaques(true);
        assertTrue("Le grizzly doit mourir instantanément suite aux dégâts de la Vipère", grizzly.estMorte());
    }

    @Test
    public void testPouvoirPiquesPointues() {
        CarteAnimalLogic hermine = FabriqueCartes.creerHermine(); // 1 ATT, 3 PV
        CarteAnimalLogic porcepict = new CarteAnimalLogic("Porc-épic", 2, 1, 1, 0);
        porcepict.ajouterPouvoir(new PiquePointues());

        plateau.getCasesJoueur().get(0).placerCarte(hermine);
        plateau.getCasesAdversaire().get(0).placerCarte(porcepict);

        plateau.resoudreAttaques(true);
        assertEquals(2, hermine.getPointsVieActuels()); // A pris 1 contre-coup de piques
    }

    @Test
    public void testPouvoirCroissance() {
        CarteAnimalLogic louveteau = FabriqueCartes.creerLouveteau();
        plateau.getCasesJoueur().get(0).placerCarte(louveteau);

        // Tour 1 début
        plateau.appliquerEffetsDebutTour(true);
        assertEquals(1, louveteau.getPointsAttaque()); // Pas encore évolué (1er tour)

        // Tour 2 début
        plateau.appliquerEffetsDebutTour(true);
        CarteLogic carteApresEvolution = plateau.getCasesJoueur().get(0).getCarteContenue();
        assertEquals(3, carteApresEvolution.getPointsAttaque()); // Évolué en Loup (3 ATT / 2 PV)
    }

    @Test
    public void testPouvoirCoureur() {
        CarteAnimalLogic elan = FabriqueCartes.creerElan();
        plateau.getCasesJoueur().get(0).placerCarte(elan);
        
        // Au début, Elan est sur la case 0
        assertSame(elan, plateau.getCasesJoueur().get(0).getCarteContenue());
        assertTrue(plateau.getCasesJoueur().get(1).estVide());
        
        // Avancer les mouvements : Elan doit se déplacer de B1 (index 0) vers B2 (index 1)
        plateau.avancerMouvementsJoueur();
        
        assertTrue(plateau.getCasesJoueur().get(0).estVide());
        assertSame(elan, plateau.getCasesJoueur().get(1).getCarteContenue());
        
        // Placer un obstacle sur B3 (index 2) pour bloquer la droite d'Elan
        CarteObstacleLogic rocher = FabriqueCartes.creerRocher();
        plateau.getCasesJoueur().get(2).placerCarte(rocher);
        
        // Avancer les mouvements : la droite d'Elan (B3) est bloquée par le Rocher. Il doit tenter la gauche (B1, index 0).
        plateau.avancerMouvementsJoueur();
        
        assertTrue(plateau.getCasesJoueur().get(1).estVide());
        assertSame(elan, plateau.getCasesJoueur().get(0).getCarteContenue());
    }
}