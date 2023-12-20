package be.ugent.telin.ddcm.implication;

/**
 * A default implementation of an implication. In this special case the returned values are the exact value that was
 * also put into the implication.
 *
 * @author Maxime Deforche
 */
public class DefaultImplication implements Implication {

    /**
     * The default implication on the given value, the original value will just be returned.
     *
     * @param value The value the implication is performed on.
     * @return The original value is simply returned.
     */
    @Override
    public double apply(double value) {
        return value;
    }

    /**
     * The default inverse implication on the given value, the original value will just be returned.
     *
     * @param value The value the inverse implication is performed on.
     * @return The original value is simply returned.
     */
    @Override
    public double applyInverse(double value) {
        return value;
    }
}
