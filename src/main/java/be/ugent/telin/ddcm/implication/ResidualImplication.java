package be.ugent.telin.ddcm.implication;

/**
 *
 *
 * @author Maxime Deforche
 */
public class ResidualImplication implements Implication {

    /**
     *
     */
    private final double threshold;

    /**
     *
     * @param threshold
     */
    public ResidualImplication(double threshold) {
        if (threshold < 0.0 || threshold > 1.0) {
            throw new IllegalArgumentException("The threshold should be a value in the interval [0.0, 1.0].");
        }

        this.threshold = threshold;
    }

    /**
     *
     *
     * @param value The value the implication is performed on.
     * @return
     */
    @Override
    public double apply(double value) {
        return value >= threshold ? 1.0 : value;
    }

    /**
     *
     *
     * @param value The value the inverse implication is performed on.
     * @return
     */
    @Override
    public double applyInverse(double value) {
        return value <= (1.0 - threshold) ? 0.0 : value;
    }
}
