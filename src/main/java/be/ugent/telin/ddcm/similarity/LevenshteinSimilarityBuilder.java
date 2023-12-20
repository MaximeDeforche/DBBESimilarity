package be.ugent.telin.ddcm.similarity;

import be.ugent.telin.ddcm.aggregator.Aggregator;
import be.ugent.telin.ddcm.aggregator.MaxAggregator;
import be.ugent.telin.ddcm.distance.LevenshteinDamerauEditDistance;
import be.ugent.telin.ddcm.distance.LevenshteinEditDistance;
import be.ugent.telin.ddcm.implication.DefaultImplication;
import be.ugent.telin.ddcm.implication.Implication;
import be.ugent.telin.ddcm.splitter.TextSplitter;
import be.ugent.telin.ddcm.splitter.WordSplitter;
import be.ugent.telin.ddcm.util.objects.Pair;
import be.ugent.telin.ddcm.util.objects.UnorderedPair;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LevenshteinSimilarityBuilder {

    private double insertionDeletionCost;
    private double replacementCost;
    private double transpositionCost;

    private Implication implication;
    private Implication reverseImplication;
    private Map<UnorderedPair<String>, Double> replacementCosts;
    private Set<Pair<String, String>> replacementCostTableExceptions;

    private TextSplitter textSplitter;
    private Aggregator aggregator;

    private boolean isDamerau;

    private double transpositionMatchThreshold;

    public LevenshteinSimilarityBuilder() {
        this.insertionDeletionCost = LevenshteinEditDistance.DEFAULT_INSERTION_DELETION_COST;
        this.replacementCost = LevenshteinEditDistance.DEFAULT_REPLACEMENT_COST;
        this.transpositionCost = LevenshteinDamerauEditDistance.DEFAULT_TRANSPOSITION_COST;

        this.implication = new DefaultImplication();
        this.reverseImplication = new DefaultImplication();
        this.replacementCosts = new HashMap<>();
        this.replacementCostTableExceptions = new HashSet<>();

        this.textSplitter = new WordSplitter();
        this.aggregator = new MaxAggregator();

        this.isDamerau = false;

        this.transpositionMatchThreshold = LevenshteinDamerauEditDistance.DEFAULT_TRANSPOSITION_MATCH_THRESHOLD;
    }

    public LevenshteinSimilarityBuilder setInsertionDeletionCost(double cost) {
        this.insertionDeletionCost = cost;
        return this;
    }

    public LevenshteinSimilarityBuilder setReplacementCost(double cost) {
        this.replacementCost = cost;
        return this;
    }

    public LevenshteinSimilarityBuilder setTranspositionCost(double cost) {
        this.transpositionCost = cost;
        return this;
    }

    public LevenshteinSimilarityBuilder setImplication(Implication implication) {
        this.implication = implication;
        return this;
    }

    public LevenshteinSimilarityBuilder setReverseImplication(Implication implication) {
        this.reverseImplication = implication;
        return this;
    }

    public LevenshteinSimilarityBuilder setReplacementCosts(Map<UnorderedPair<String>, Double> replacementCosts) {
        this.replacementCosts = replacementCosts;
        return this;
    }

    public LevenshteinSimilarityBuilder setReplacementCostTableExceptions(Set<Pair<String, String>> replacementCostTableExceptions) {
        this.replacementCostTableExceptions = replacementCostTableExceptions;
        return this;
    }

    public LevenshteinSimilarityBuilder setTextSplitter(TextSplitter textSplitter) {
        this.textSplitter = textSplitter;
        return this;
    }

    public LevenshteinSimilarityBuilder setAggregator(Aggregator aggregator) {
        this.aggregator = aggregator;
        return this;
    }

    public LevenshteinSimilarityBuilder isDamerau(boolean isDamerau) {
        this.isDamerau = isDamerau;
        return this;
    }

    public LevenshteinSimilarityBuilder setTranspositionMatchThreshold(double transpositionMatchThreshold) {
        this.transpositionMatchThreshold = transpositionMatchThreshold;
        return this;
    }

    public LevenshteinSimilarity build() {
        LevenshteinEditDistance distance = isDamerau ?
                new LevenshteinDamerauEditDistance(this.insertionDeletionCost, this.replacementCost, this.implication
                        , this.replacementCosts, this.replacementCostTableExceptions, this.transpositionCost
                        , this.reverseImplication, this.transpositionMatchThreshold) :
                new LevenshteinEditDistance(this.insertionDeletionCost, this.replacementCost, this.implication
                        , this.replacementCosts, this.replacementCostTableExceptions, this.reverseImplication);

        return new LevenshteinSimilarity(distance, this.textSplitter, this.aggregator);
    }
}
