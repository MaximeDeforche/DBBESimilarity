package be.ugent.telin.ddcm.similarity;

import be.ugent.telin.ddcm.dbbe.splitter.GreekWordSplitter;
import be.ugent.telin.ddcm.distance.LevenshteinDamerauEditDistance;
import be.ugent.telin.ddcm.distance.LevenshteinEditDistance;
import be.ugent.telin.ddcm.splitter.TextSplitter;
import be.ugent.telin.ddcm.splitter.WordSplitter;
import be.ugent.telin.ddcm.util.objects.Pair;
import be.ugent.telin.ddcm.util.objects.UnorderedPair;
import org.neo4j.procedure.*;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class LevenshteinDamerauSimilarityCypher {

    @UserFunction(value="ddcm.similarity.levenshteindamerau")
    @Description("")
    public double levenshteinDamerauSimilarity(@Name(value = "s1", defaultValue = "") String s1,
           @Name(value = "s2", defaultValue = "") String s2, @Name(value = "config", defaultValue = "{}") Map<String, Object> config) {

        double indelCost = (double) config.getOrDefault("indelCost", LevenshteinEditDistance.DEFAULT_INSERTION_DELETION_COST);
        double replacementCost = (double) config.getOrDefault("replacementcost", LevenshteinEditDistance.DEFAULT_REPLACEMENT_COST);
        double transpositionCost = (double) config.getOrDefault("transpositionCost", LevenshteinDamerauEditDistance.DEFAULT_TRANSPOSITION_COST);
        String textType = (String) config.getOrDefault("text", "normal");
        TextSplitter wordSplitter = textType.equals("greek") ? new GreekWordSplitter() : new WordSplitter();
        Map<UnorderedPair<String>, Double> replacementCosts = textType.equals("greek") ? GreekWordSplitter.replacementCosts : Collections.emptyMap();
        Set<Pair<String, String>> replacementCostsExceptions = textType.equals("greek") ? GreekWordSplitter.exceptions : Collections.emptySet();

        LevenshteinSimilarity sim = new LevenshteinSimilarityBuilder()
                .isDamerau(true)
                .setInsertionDeletionCost(indelCost)
                .setReplacementCost(replacementCost)
                .setTranspositionCost(transpositionCost)
                .setReplacementCosts(replacementCosts)
                .setReplacementCostTableExceptions(replacementCostsExceptions)
                .setTextSplitter(wordSplitter)
                .build();

        return sim.apply(s1, s2);
    }
}