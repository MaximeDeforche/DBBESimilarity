package be.ugent.telin.ddcm.aggregator;

import java.util.List;

/**
 * General interface for aggregators of Double values.
 *
 * @author Maxime Deforche
 */
public interface Aggregator {

    /**
     * Apply the aggregator to a {@link List} of Double values.
     *
     * @param values {@link List} of Double values to be aggregated.
     * @return A double with the aggregation result is returned.
     */
    double apply(List<Double> values);
}
