package be.ugent.telin.ddcm.aggregator;

import java.util.Collections;
import java.util.List;

/**
 * Double value aggregator getting the maximum value of the {@link List} of Doubles.
 *
 * @author Maxime Deforche
 */
public class MaxAggregator implements Aggregator {

    /**
     * Apply the aggregator to a {@link List} of Double values.
     * The aggregated value is found by getting the maximum value in the {@link List} of Doubles
     *
     * @param values {@link List} of Double values to be aggregated.
     * @return The maximum double value of the {@link List} of doubles
     */
    @Override
    public double apply(List<Double> values) {
        return Collections.max(values);
    }
}
