package be.ugent.telin.ddcm.aggregator;

import java.util.List;

/**
 * Double value aggregator getting the average value of the {@link List} of Doubles.
 *
 * @author Maxime Deforche
 */
public class AvgAggregator implements Aggregator {

    /**
     * Apply the aggregator to a {@link List} of Double values.
     * The aggregated value is calculated by summing up all the values and dividing the result by the amount of values
     * in the list.
     *
     * @param values {@link List} of Double values to be aggregated.
     * @return The average double value of the {@link List} of doubles
     */
    @Override
    public double apply(List<Double> values) {
        double sum = values.stream().mapToDouble(Double::doubleValue).sum();
        return sum / values.size();
    }
}
