package be.ugent.telin.ddcm.implication;

/**
 * Interface as basis for implications to be applied to a value.
 * An implication is performed on a value in the [0.0, 1.0] interval and can adapt the value to another value in the
 * same interval accordingly. An inverse method is always provided.
 *
 * @author Maxime Deforche
 */
public interface Implication {

    /**
     * Apply the implication on the given value.
     *
     * @param value The value the implication is performed on.
     * @return The new value following from the implication.
     */
    double apply(double value);

    /**
     * Apply the implication in an inverse way on the given value.
     *
     * @param value The value the inverse implication is performed on.
     * @return The new value following from the inverse implication.
     */
    double applyInverse(double value);
}
