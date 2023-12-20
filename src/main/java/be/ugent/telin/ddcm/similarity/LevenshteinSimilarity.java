package be.ugent.telin.ddcm.similarity;

import be.ugent.telin.ddcm.aggregator.Aggregator;
import be.ugent.telin.ddcm.distance.LevenshteinEditDistance;
import be.ugent.telin.ddcm.splitter.TextSplitter;
import be.ugent.telin.ddcm.util.StringUtils;
import be.ugent.telin.ddcm.util.objects.UnorderedPair;

import java.util.ArrayList;
import java.util.List;

public class LevenshteinSimilarity  implements Similarity {

    private final LevenshteinEditDistance distance;
    private final TextSplitter textSplitter;

    private final Aggregator aggregator;

    public LevenshteinSimilarity(LevenshteinEditDistance distance, TextSplitter textSplitter, Aggregator aggregator) {
        this.distance = distance;
        this.textSplitter = textSplitter;
        this.aggregator = aggregator;
    }

    public LevenshteinEditDistance getDistance() {
        return this.distance;
    }

    @Override
    public double apply(String a, String b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("Strings cannot be null");
        }

        String[] s1 = textSplitter.apply(a);
        String[] s2 = textSplitter.apply(b);

        return compare(s1, s2);
    }

    @Override
    public double apply(String[] a, String[] b) {
        return compare(a, b);
    }

    private double compare(String[] s1, String[] s2) {
        double dist = distance.apply(s1, s2);
        double agg = getAggregatedLength(s1, s2);

        return 1 - (dist / agg);
    }

    private double getAggregatedLength(String[] s1, String[] s2) {
        List<Double> lengths = new ArrayList<>();
        lengths.add((double) s1.length);
        lengths.add((double) s2.length);

        return aggregator.apply(lengths);
    }

    public void addReplacementCost(UnorderedPair<String> pair, double cost) {
        distance.addReplacementCost(pair, cost);
    }

    public void clearReplacementCostTable() {
        distance.clearReplacementCostTable();
    }

    public boolean hasCost(UnorderedPair<String> pair) {
        return distance.hasCost(pair);
    }
}
