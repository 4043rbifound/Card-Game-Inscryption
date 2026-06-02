package Inscryption.Logique;

import java.util.ArrayList;
import java.util.List;

public class PlateauLogic {
    private final List<Emplacement> m_casesJoueur;
    private final List<Emplacement> m_casesAdversaire;

    public PlateauLogic() {
        this.m_casesJoueur = new ArrayList<>();
        this.m_casesAdversaire = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            this.m_casesJoueur.add(new Emplacement(i));
            this.m_casesAdversaire.add(new Emplacement(i));
        }
    }

    public void AppliquerEffetsDebutTour(boolean campJoueur) {
        List<Emplacement> cases = campJoueur ? m_casesJoueur : m_casesAdversaire;

        for (Emplacement emp : cases) {
            if (!emp.estVide() && !emp.getCarteContenue().estObstacle()) {
                CarteAnimalLogic animal = (CarteAnimalLogic) emp.getCarteContenue();

                animal.incrementerToursSurPlateau();

                for (Pouvoir p : animal.getPouvoirs()) {
                    p.auDebutTour(animal, emp);
                }
            }
        }
    }

    public void avancerMouvements(boolean campJoueur) {
        List<Emplacement> cases = campJoueur ? m_casesJoueur : m_casesAdversaire;

        for (Emplacement emp : cases) {
            if (!emp.estVide() && !emp.getCarteContenue().estObstacle()) {
                CarteAnimalLogic animal = (CarteAnimalLogic) emp.getCarteContenue();

                for (Pouvoir p : animal.getPouvoirs()) {
                    p.auMouvement(animal, emp, this);
                }
            }
        }
    }

    public List<String> resoudreAttaques(boolean campJoueur, ScoreLogic score) {
        List<String> messages = new ArrayList<>();

        List<Emplacement> mesCases = campJoueur ? m_casesJoueur : m_casesAdversaire;
        List<Emplacement> casesAdverses = campJoueur ? m_casesAdversaire : m_casesJoueur;

        for (Emplacement emp : mesCases) {
            if (emp.estVide()) continue;

            CarteLogic carteAttaquante = emp.getCarteContenue();

            if (carteAttaquante.estObstacle()) continue;

            CarteAnimalLogic attaquant = (CarteAnimalLogic) carteAttaquante;
            int pos = emp.getPosition();
            Emplacement caseEnFace = casesAdverses.get(pos);
// combat volant ou case vide
            if (attaquant.estVolant() || caseEnFace.estVide()) {
                int degats = attaquant.getPointsAttaque();
                if (degats > 0) {
                    if (campJoueur) {
                        score.ajouterPointsJoueur(degats);
                        messages.add(attaquant.getNom() + " attaque directement ! +" + degats + " points pour le Joueur.");
                    } else {
                        score.ajouterPointsAdversaire(degats);
                        messages.add(attaquant.getNom() + " attaque directement ! +" + degats + " points pour l'Adversaire.");
                    }
                }
            }
// combat contre carte
            else {
                CarteLogic cible = caseEnFace.getCarteContenue();
                int degatsCalcules = attaquant.getPointsAttaque();

                if (!cible.estObstacle()) {
                    CarteAnimalLogic animalCible = (CarteAnimalLogic) cible;
                    for (Pouvoir p : animalCible.getPouvoirs()) {
                        degatsCalcules = p.auCalculAttaque(degatsCalcules, attaquant, cible, this);
                    }
                }
                if (degatsCalcules > 0) {
                    cible.recevoirDegats(degatsCalcules);
                    messages.add(attaquant.getNom() + " inflige " + degatsCalcules + " dégâts à " + cible.getNom() + ".");

                    if (!cible.estObstacle()) {
                        CarteAnimalLogic animalCible = (CarteAnimalLogic) cible;
                        for (Pouvoir p : animalCible.getPouvoirs()) {
                            p.apresRecevoirDegats(animalCible, attaquant, degatsCalcules, this);
                        }
                        for (Pouvoir p : attaquant.getPouvoirs()) {
                            p.apresRecevoirDegats(animalCible, attaquant, degatsCalcules, this);
                        }
                    }
                }

                if (cible.estMorte()) {
                    messages.add(cible.getNom() + " est détruit(e) !");
                    caseEnFace.liberer();
                }
                if (attaquant.estMorte()) {
                    messages.add(attaquant.getNom() + " est mort suite au combat.");
                    emp.liberer();
                }
            }
        }
        return messages;
    }

    public Emplacement trouverCaseAdjacente(Emplacement caseActuelle) {
        int pos = caseActuelle.getPosition();
        if (pos + 1 < 4) {
            return m_casesJoueur.contains(caseActuelle) ? m_casesJoueur.get(pos + 1) : m_casesAdversaire.get(pos + 1);
        } else if (pos - 1 >= 0) {
            return m_casesJoueur.contains(caseActuelle) ? m_casesJoueur.get(pos - 1) : m_casesAdversaire.get(pos - 1);
        }
        return null;
    }

    public List<Emplacement> getCasesJoueur() { return m_casesJoueur; }
    public List<Emplacement> getCasesAdversaire() { return m_casesAdversaire; }
}