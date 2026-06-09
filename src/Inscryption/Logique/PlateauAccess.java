package Inscryption.Logique;

/**
 * Interface permettant a pouvoir COureur d'interagir avec le plateau
 * de sans cycle de deps.
 */
public interface PlateauAccess {
    java.util.Optional<Emplacement> trouverCaseAdjacente(Emplacement caseActuelle);
}
