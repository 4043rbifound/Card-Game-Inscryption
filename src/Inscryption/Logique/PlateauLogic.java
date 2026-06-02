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
            if (!emp.estVide()) {
                CarteLogic carte = emp.getCarteContenue();
                if (carte instanceof CarteAnimalLogic) {
                    CarteAnimalLogic animal = (CarteAnimalLogic) carte;
                    animal.incrementerToursSurPlateau();

                    for (Pouvoir p : animal.getPouvoirs()) {
                        p.auDebutTour(animal, emp);
                    }
                }
            }
        }
    }

    public void avancerMouvements(boolean campJoueur) {
        List<Emplacement> cases = campJoueur ? m_casesJoueur : m_casesAdversaire;

        for (Emplacement emp : cases) {
            if (!emp.estVide()) {
                CarteLogic carte = emp.getCarteContenue();
                for (Pouvoir p : carte.getPouvoirs()) {
                    p.auMouvement(carte, emp, this);
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

            CarteLogic attaquant = emp.getCarteContenue();
            int pos = emp.getPosition();
            Emplacement caseEnFace = casesAdverses.get(pos);

            attaquant.attaquer(caseEnFace, score, messages, this);

            if (!caseEnFace.estVide() && caseEnFace.getCarteContenue().getPointsVieActuels() <= 0) {
                messages.add(caseEnFace.getCarteContenue().getNom() + " est détruit(e) !");
                caseEnFace.liberer();
            }
            if (attaquant.getPointsVieActuels() <= 0) {
                messages.add(attaquant.getNom() + " est mort suite au combat.");
                emp.liberer();
            }
        }
        return messages;
    }

    public void appliquerDegatsDirects(int degats, CarteLogic attaquant, ScoreLogic score, List<String> messages) {
        if (m_casesJoueur.stream().anyMatch(e -> !e.estVide() && e.getCarteContenue() == attaquant)) {
            score.ajouterPointsJoueur(degats);
            messages.add(attaquant.getNom() + " attaque directement ! +" + degats + " points pour le Joueur.");
        } else {
            score.ajouterPointsAdversaire(degats);
            messages.add(attaquant.getNom() + " attaque directement ! +" + degats + " points pour l'Adversaire.");
        }
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