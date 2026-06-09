package Inscryption.Logique;

/**
 * Représente un emplacement physique sur le plateau de jeu.
 * Un emplacement a une position fixe, peut contenir une carte, et sait
 * gérer les attaques qu'il reçoit ainsi que la mort de la carte qu'il héberge.
 */
public class Emplacement {

    private final int m_position;
    private CarteLogic m_carteContenue;

    public Emplacement(int position) {
        this.m_position = position;
        this.m_carteContenue = null;
    }

    public int getPosition() {
        return this.m_position;
    }

    public boolean estVide() {
        return this.m_carteContenue == null;
    }

    public java.util.Optional<CarteLogic> getCarteContenue() {
        return java.util.Optional.ofNullable(this.m_carteContenue);
    }

    public void placerCarte(CarteLogic carte) {
        this.m_carteContenue = carte;
    }

    public void liberer() {
        this.m_carteContenue = null;
    }

    /**
     * Libère automatiquement l'emplacement si la carte qu'il contient est morte.
     */
    public void libererSiMorte() {
        if (this.m_carteContenue != null && this.m_carteContenue.estMorte()) {
            this.m_carteContenue = null;
        }
    }

    /**
     * Reçoit une attaque de la part d'une carte attaquante.
     * Si l'emplacement est vide, retourne les dégâts directs à répercuter sur le score.
     * Si l'emplacement est occupé, résout le combat localement (dégâts, pouvoirs, mort)
     * et retourne 0 (pas de dégâts directs au score).
     *
     * @param attaquant la carte qui attaque cet emplacement
     * @return les dégâts directs à ajouter au score, ou 0 si une cible a absorbé l'attaque
     */
    public int recevoirAttaque(CarteLogic attaquant) {
        if (estVide()) {
            return attaquant.getPointsAttaque();
        }

        int degatsCalcules = attaquant.getPointsAttaque();

        for (Pouvoir p : m_carteContenue.getPouvoirs()) {
            degatsCalcules = p.auCalculAttaque(degatsCalcules, attaquant, m_carteContenue);
        }

        if (degatsCalcules > 0) {
            m_carteContenue.recevoirDegats(degatsCalcules);

            for (Pouvoir p : m_carteContenue.getPouvoirs()) {
                p.apresSubirDegats(m_carteContenue, attaquant, degatsCalcules);
            }
            for (Pouvoir p : attaquant.getPouvoirs()) {
                p.apresInfligerDegats(m_carteContenue, attaquant, degatsCalcules);
            }
        }

        libererSiMorte();
        return 0;
    }

    /**
     * Indique si une carte peut être placée sur cet emplacement.
     */
    public boolean peutAccepterCarte() {
        return estVide();
    }
}
