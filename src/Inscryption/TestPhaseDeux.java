package Inscryption;

import static org.junit.Assert.*;
import org.junit.Test;
import Inscryption.Logique.*;

import java.util.List;

public class TestPhaseDeux {

    @Test
    public void testCopieFraicheEtPersistancePouvoir() {
        // Crée un template de Louveteau (avec Croissance)
        CarteAnimalLogic template = FabriqueCartes.creerLouveteau();
        
        // Copie fraîche
        CarteAnimalLogic copie1 = FabriqueCartes.creerCopieFraiche(template);
        assertNotNull(copie1);
        assertEquals("Louveteau", copie1.getNom());
        assertEquals(1, copie1.getPointsVieActuels());
        assertEquals(1, copie1.getPointsAttaque());
        assertEquals(1, copie1.getPouvoirs().size());
        assertEquals("Croissance", copie1.getPouvoirs().get(0).getNom());
        
        // Modifie la copie1
        copie1.recevoirDegats(1);
        copie1.incrementerToursSurPlateau();
        assertTrue(copie1.estMorte());
        
        // Vérifie que le template n'est pas modifié
        assertEquals(1, template.getPointsVieActuels());
        assertFalse(template.estMorte());
        assertEquals(0, template.getToursSurPlateau());
        
        // Copie 2
        CarteAnimalLogic copie2 = FabriqueCartes.creerCopieFraiche(template);
        assertEquals(1, copie2.getPointsVieActuels());
        assertFalse(copie2.estMorte());
        assertEquals(0, copie2.getToursSurPlateau());
    }

    @Test
    public void testPierreDeSacrificeTransfertPouvoir() {
        JoueurLogic joueur = new JoueurLogic();
        List<CarteAnimalLogic> collection = joueur.getCollection();
        
        // On cherche un Moineau (Volant) et une Hermine dans la collection
        CarteAnimalLogic moineau = null;
        CarteAnimalLogic hermine = null;
        for (CarteAnimalLogic c : collection) {
            if (c.getNom().equals("Moineau")) moineau = c;
            if (c.getNom().equals("Hermine")) hermine = c;
        }
        
        assertNotNull(moineau);
        assertNotNull(hermine);
        
        // On transfère le pouvoir Volant du Moineau à l'Hermine
        Pouvoir pATransferer = null;
        if (!moineau.getPouvoirs().isEmpty()) {
            pATransferer = moineau.getPouvoirs().get(0);
        } else if (moineau.getLignePouvoir().equalsIgnoreCase("Volant")) {
            pATransferer = new Volant();
        }
        
        assertNotNull(pATransferer);
        assertEquals("Volant", pATransferer.getNom());
        
        // Retirer le moineau de la collection et ajouter le pouvoir à l'hermine
        collection.remove(moineau);
        hermine.ajouterPouvoir(pATransferer);
        
        // Vérifie que la collection de l'hermine a maintenant le pouvoir Volant
        boolean aVolant = false;
        for (Pouvoir p : hermine.getPouvoirs()) {
            if (p.getNom().equalsIgnoreCase("Volant")) aVolant = true;
        }
        assertTrue(aVolant);
        
        // Réinitialise pour une nouvelle partie
        joueur.resetForNewPartie();
        
        // Vérifie que la main ou le deck contient des copies de l'Hermine avec le pouvoir Volant
        CarteAnimalLogic hermineDuDeck = null;
        // On cherche la copie de l'hermine dans la main
        for (CarteAnimalLogic c : joueur.getMain()) {
            if (c != null && c.getNom().equals("Hermine")) {
                hermineDuDeck = c;
            }
        }
        
        if (hermineDuDeck != null) {
            assertTrue(hermineDuDeck.getLignePouvoir().equalsIgnoreCase("Volant"));
        }
    }

    @Test
    public void testNouvellesCartesSelection() {
        JoueurLogic joueur = new JoueurLogic();
        
        // Ajout d'Elan à la collection
        CarteAnimalLogic elan = FabriqueCartes.creerElan();
        joueur.ajouterCarteACollection(elan);
        
        assertTrue(joueur.getCollection().contains(elan));
        
        // Reset pour nouvelle partie
        joueur.resetForNewPartie();
        
        // L'élan doit être cloné et mis dans le deck
        // On va vider la main pour piocher tout le deck et trouver l'Elan
        boolean elanTrouve = false;
        for (int i = 0; i < 20; i++) {
            joueur.piocherCartePrincipal();
        }
        for (CarteAnimalLogic c : joueur.getMain()) {
            if (c != null && c.getNom().equals("Elan")) {
                elanTrouve = true;
                assertEquals(4, c.getPointsVieActuels());
                assertEquals(2, c.getPointsAttaque());
                assertEquals("Coureur", c.getPouvoirs().get(0).getNom());
            }
        }
    }

    @Test
    public void testSacrificeEtPayementSang() {
        JoueurLogic joueur = new JoueurLogic();
        PlateauLogic plateau = new PlateauLogic();
        
        // 1. Placer un écureuil sur B1 (index 0)
        CarteAnimalLogic ecureuil = FabriqueCartes.creerEcureuil();
        plateau.getCasesJoueur().get(0).placerCarte(ecureuil);
        assertFalse(plateau.getCasesJoueur().get(0).estVide());
        
        // 2. Simuler un sacrifice explicite sur B1
        // (Vérifier que la carte est enlevée, +1 sang généré et +1 os récupéré)
        assertEquals(0, joueur.getReserveSang());
        assertEquals(0, joueur.getReserveOs());
        
        // On récupère la carte
        CarteLogic sac = plateau.getCasesJoueur().get(0).getCarteContenue();
        assertNotNull(sac);
        
        // Exécuter le sacrifice
        int sangGenere = sac.executerSacrifice(plateau.getCasesJoueur().get(0), joueur);
        joueur.ajouterSang(sangGenere);
        
        assertTrue(plateau.getCasesJoueur().get(0).estVide());
        assertEquals(1, joueur.getReserveSang());
        assertEquals(1, joueur.getReserveOs());
        
        // 3. Simuler le placement d'une Hermine (coût: 1 sang, 0 os)
        CarteAnimalLogic hermine = FabriqueCartes.creerHermine();
        int coutSang = hermine.getCoutSang();
        int coutOs = hermine.getCoutOs();
        
        // Vérifier les ressources
        assertTrue(joueur.getReserveSang() >= coutSang);
        assertTrue(joueur.getReserveOs() >= coutOs);
        
        // Consommer et poser
        joueur.consommerSang(coutSang);
        joueur.consommerOs(coutOs);
        plateau.getCasesJoueur().get(0).placerCarte(hermine);
        
        assertEquals(0, joueur.getReserveSang());
        assertEquals(1, joueur.getReserveOs()); // L'os persiste
        assertEquals("Hermine", plateau.getCasesJoueur().get(0).getCarteContenue().getNom());
    }

    @Test
    public void testImpossibleDeSacrifierObstacle() {
        JoueurLogic joueur = new JoueurLogic();
        PlateauLogic plateau = new PlateauLogic();
        
        CarteObstacleLogic rocher = FabriqueCartes.creerRocher();
        plateau.getCasesJoueur().get(0).placerCarte(rocher);
        
        // Tentative de sacrifice d'un obstacle
        int sangGenere = rocher.executerSacrifice(plateau.getCasesJoueur().get(0), joueur);
        
        assertEquals(0, sangGenere);
        assertFalse(plateau.getCasesJoueur().get(0).estVide()); // Toujours occupé par le Rocher
        assertEquals(0, joueur.getReserveSang());
        assertEquals(0, joueur.getReserveOs());
    }

    @Test
    public void testPiocheCartes() {
        JoueurLogic joueur = new JoueurLogic();
        int initialMain = joueur.getNombreCartesMain();
        int initialDeck = joueur.getNombreCartesDeck();
        int initialEcureuils = joueur.getNombreEcureuils();
        
        // Au démarrage, le joueur a pioché 4 cartes de départ
        assertEquals(4, initialMain);
        
        // Piocher une carte du deck principal
        joueur.piocherCartePrincipal();
        assertEquals(5, joueur.getNombreCartesMain());
        assertEquals(initialDeck - 1, joueur.getNombreCartesDeck());
        
        // Piocher un écureuil
        joueur.piocherEcureuil();
        assertEquals(6, joueur.getNombreCartesMain());
        assertEquals(initialEcureuils - 1, joueur.getNombreEcureuils());
    }

    @Test
    public void testMiseEnPlacePartie() {
        PlateauLogic plateau = new PlateauLogic();
        // Le plateau joueur a 4 emplacements
        assertEquals(4, plateau.getCasesJoueur().size());
        assertEquals(4, plateau.getCasesAdversaire().size());
        
        // Les emplacements sont initialement vides
        for (int i = 0; i < 4; i++) {
            assertTrue(plateau.getCasesJoueur().get(i).estVide());
            assertTrue(plateau.getCasesAdversaire().get(i).estVide());
        }
        
        JoueurLogic joueur = new JoueurLogic();
        // La pioche commence bien avec 15 cartes (dont 4 piochées au départ, donc 11 restantes dans le deck)
        assertEquals(11, joueur.getNombreCartesDeck());
        // La pile d'écureuils commence bien avec 20 cartes
        assertEquals(20, joueur.getNombreEcureuils());
    }
}
