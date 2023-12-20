package be.ugent.telin.ddcm.aggregator;

import java.util.Collections;
import java.util.List;

/**
 * Double value aggregator getting the minimal value of the {@link List} of Doubles.
 *
 * @author Maxime Deforche
 */
public class MinAggregator implements Aggregator {

    /**
     * Apply the aggregator to a {@link List} of Double values.
     * The aggregated value is found by getting the minimal value in the {@link List} of Doubles
     *
     * @param values {@link List} of Double values to be aggregated.
     * @return The minimal double value of the {@link List} of doubles
     */
    @Override
    public double apply(List<Double> values) {
        return Collections.min(values);
    }
}
