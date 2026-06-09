package Inscryption.Logique;

import java.util.ArrayList;
import java.util.Collections;
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

    /** Applique les effets de début de tour pour le camp indiqué. */
    public void appliquerEffetsDebutTour(boolean campJoueur) {
        appliquerEffetsDebutTourSurCases(campJoueur ? m_casesJoueur : m_casesAdversaire);
    }

    /** Raccourci pour le camp joueur. */
    public void appliquerEffetsDebutTourJoueur() {
        appliquerEffetsDebutTourSurCases(m_casesJoueur);
    }

    /** Raccourci pour le camp adversaire. */
    public void appliquerEffetsDebutTourAdversaire() {
        appliquerEffetsDebutTourSurCases(m_casesAdversaire);
    }

    private void appliquerEffetsDebutTourSurCases(List<Emplacement> cases) {
        for (Emplacement emp : cases) {
            if (!emp.estVide()) {
                CarteLogic carte = emp.getCarteContenue();
                carte.incrementerToursSurPlateau();
                for (Pouvoir p : carte.getPouvoirs()) {
                    p.auDebutTour(carte, emp);
                }
            }
        }
    }

    public void avancerMouvementsJoueur() {
        avancerMouvements(m_casesJoueur);
    }

    public void avancerMouvementsAdversaire() {
        avancerMouvements(m_casesAdversaire);
    }

    private void avancerMouvements(List<Emplacement> cases) {
        for (Emplacement emp : cases) {
            if (!emp.estVide()) {
                CarteLogic carte = emp.getCarteContenue();
                Emplacement caseAdjacente = trouverCaseAdjacente(emp);
                for (Pouvoir p : carte.getPouvoirs()) {
                    p.auMouvement(carte, emp, caseAdjacente);
                }
            }
        }
    }

    /**
     * Résout toutes les attaques du camp indiqué.
     * Retourne la liste des dégâts directs par case (index 0 à 3).
     * La valeur est &gt; 0 si la carte en cette position a infligé des dégâts directs au score,
     * 0 si une carte adverse a absorbé l'attaque ou si la case était vide.
     * C'est l'appelant (couche Affichage) qui met à jour le score et affiche les messages.
     *
     * @param campJoueur true = les cartes du joueur attaquent, false = les cartes adverses attaquent
     * @return liste de 4 entiers représentant les dégâts directs par position
     */
    public List<Integer> resoudreAttaques(boolean campJoueur) {
        List<Integer> degatsParCase = new ArrayList<>(Collections.nCopies(4, 0));

        List<Emplacement> mesCases     = campJoueur ? m_casesJoueur    : m_casesAdversaire;
        List<Emplacement> casesAdverses = campJoueur ? m_casesAdversaire : m_casesJoueur;

        for (Emplacement emp : mesCases) {
            if (emp.estVide()) continue;

            CarteLogic attaquant = emp.getCarteContenue();
            Emplacement caseEnFace = casesAdverses.get(emp.getPosition());

            int degats = attaquant.attaquer(caseEnFace);
            degatsParCase.set(emp.getPosition(), degats);

            // L'attaquant peut lui-même avoir subi des dégâts en retour (ex : Piques Pointues)
            emp.libererSiMorte();
        }

        return degatsParCase;
    }

    public Emplacement trouverCaseAdjacente(Emplacement caseActuelle) {
        List<Emplacement> cases = m_casesJoueur.contains(caseActuelle) ? m_casesJoueur : m_casesAdversaire;
        int pos = caseActuelle.getPosition();
        if (pos + 1 < 4 && cases.get(pos + 1).estVide()) {
            return cases.get(pos + 1);
        } else if (pos - 1 >= 0 && cases.get(pos - 1).estVide()) {
            return cases.get(pos - 1);
        }
        return null;
    }

    public List<Emplacement> getCasesJoueur()    { return m_casesJoueur; }
    public List<Emplacement> getCasesAdversaire() { return m_casesAdversaire; }
}