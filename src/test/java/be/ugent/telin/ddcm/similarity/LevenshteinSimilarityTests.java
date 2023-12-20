package be.ugent.telin.ddcm.similarity;

import be.ugent.telin.ddcm.implication.ResidualImplication;
import be.ugent.telin.ddcm.splitter.SentenceSplitter;
import be.ugent.telin.ddcm.util.objects.UnorderedPair;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class LevenshteinSimilarityTests {

    @Test
    public void simpleTests() {
        Similarity similarity = new LevenshteinSimilarityBuilder().build();
        String check = "test";

        Assert.assertEquals(1.0, similarity.apply(check, "test"), 0.001);
        Assert.assertEquals(0.75, similarity.apply(check, "tost"), 0.001);
        Assert.assertEquals(0.75, similarity.apply(check, "tst"), 0.001);
        Assert.assertEquals(0.8, similarity.apply(check, "testt"), 0.001);
        Assert.assertEquals(0.625, similarity.apply("elephant", "relevant"), 0.001);

        Assert.assertEquals(0.0, similarity.apply(check, ""), 0.0001);
    }

    @Test
    public void costsTests() {
        LevenshteinSimilarity similarity = new LevenshteinSimilarityBuilder()
                .setInsertionDeletionCost(0.9)
                .setReplacementCost(0.7)
                .build();
        String check = "test";

        Assert.assertEquals(1.0, similarity.apply(check, "test"), 0.001);
        Assert.assertEquals(0.825, similarity.apply(check, "tost"), 0.001);
        Assert.assertEquals(0.775, similarity.apply(check, "tst"), 0.001);
        Assert.assertEquals(0.82, similarity.apply(check, "testt"), 0.001);
        Assert.assertEquals(0.6875, similarity.apply("elephant", "relevant"), 0.001);

        Assert.assertEquals(0.1, similarity.apply(check, ""), 0.0001);
    }

    @Test
    public void replacementCostsTests() {
        Map<UnorderedPair<String>, Double> costs = new HashMap<>();
        costs.put(UnorderedPair.of("e", "a"), 0.0);
        costs.put(UnorderedPair.of("e", "i"), 0.5);

        LevenshteinSimilarity similarity = new LevenshteinSimilarityBuilder()
                .setReplacementCosts(costs)
                .build();
        String check = "test";

        Assert.assertEquals(1.0, similarity.apply(check, "test"), 0.001);
        Assert.assertEquals(0.75, similarity.apply(check, "tost"), 0.001);
        Assert.assertEquals(1.0, similarity.apply(check, "tast"), 0.001);
        Assert.assertEquals(0.875, similarity.apply(check, "tist"), 0.001);
        Assert.assertEquals(0.7, similarity.apply(check, "tistt"), 0.001);

        similarity.addReplacementCost(UnorderedPair.of("e", "a"), 0.2);

        Assert.assertEquals(0.95, similarity.apply(check, ("tast")), 0.001);
    }

    @Test
    public void implicationTests() {
        Map<UnorderedPair<String>, Double> costs = new HashMap<>();
        costs.put(UnorderedPair.of("e", "a"), 0.1);
        costs.put(UnorderedPair.of("e", "i"), 0.3);

        LevenshteinSimilarity similarity = new LevenshteinSimilarityBuilder()
                .setImplication(new ResidualImplication(0.8))
                .setReplacementCosts(costs)
                .build();
        String check = "test";

        Assert.assertEquals(1.0, similarity.apply(check, "test"), 0.001);
        Assert.assertEquals(0.75, similarity.apply(check, "tost"), 0.001);
        Assert.assertEquals(1.0, similarity.apply(check, "tast"), 0.001);
        Assert.assertEquals(0.925, similarity.apply(check, "tist"), 0.001);
        Assert.assertEquals(0.8, similarity.apply(check, "tastt"), 0.001);
        Assert.assertEquals(0.74, similarity.apply(check, "tistt"), 0.001);
    }

    @Test
    public void simpleVerseTests() {
        LevenshteinSimilarity similarity = new LevenshteinSimilarityBuilder()
                .setTextSplitter(new SentenceSplitter())
                .build();
        String check = "this is a test sentence";

        Assert.assertEquals(1.0, similarity.apply(check, "this is a test sentence"), 0.001);
        Assert.assertEquals(0.8, similarity.apply(check, "this is o test sentence"), 0.001);
        Assert.assertEquals(0.8, similarity.apply(check, "this is a special sentence"), 0.001);
        Assert.assertEquals(0.8333, similarity.apply(check, "this is a special test sentence"), 0.001);
        Assert.assertEquals(0.8, similarity.apply(check, "this is a sentence"), 0.001);
        Assert.assertEquals(0.4, similarity.apply(check, "is o special test sentence"), 0.001);
    }

    @Test
    public void costsVerseTests() {
        LevenshteinSimilarity similarity = new LevenshteinSimilarityBuilder()
                .setInsertionDeletionCost(0.9)
                .setReplacementCost(0.7)
                .setTextSplitter(new SentenceSplitter())
                .build();
        String check = "this is a test sentence";

        Assert.assertEquals(1.0, similarity.apply(check, "this is a test sentence"), 0.001);
        Assert.assertEquals(0.86, similarity.apply(check, "this is o test sentence"), 0.001);
        Assert.assertEquals(0.86, similarity.apply(check, "this is a special sentence"), 0.001);
        Assert.assertEquals(0.85, similarity.apply(check, "this is a special test sentence"), 0.001);
        Assert.assertEquals(0.82, similarity.apply(check, "this is a sentence"), 0.001);
        Assert.assertEquals(0.58, similarity.apply(check, "is o special test sentence"), 0.001);
    }

    @Test
    public void replacementCostsVerseTests() {
        Map<UnorderedPair<String>, Double> costs = new HashMap<>();
        costs.put(UnorderedPair.of("sentence", "verse"), 0.2);

        LevenshteinSimilarity similarity = new LevenshteinSimilarityBuilder()
                .setReplacementCosts(costs)
                .setTextSplitter(new SentenceSplitter())
                .build();
        String check = "this is a test sentence";

        Assert.assertEquals(1.0, similarity.apply(check, "this is a test sentence"), 0.001);
        Assert.assertEquals(0.8, similarity.apply(check, "this is a test line"), 0.001);
        Assert.assertEquals(0.96, similarity.apply(check, "this is a test verse"), 0.001);
        Assert.assertEquals(0.76, similarity.apply(check, "this a test verse"), 0.001);

        similarity.addReplacementCost(UnorderedPair.of("sentence", "line"), 0.8);

        Assert.assertEquals(0.84, similarity.apply(check, "this is a test line"), 0.001);
    }

    @Test
    public void implicationVerseTests() {
        Map<UnorderedPair<String>, Double> costs = new HashMap<>();
        costs.put(UnorderedPair.of("sentence", "verse"), 0.1);
        costs.put(UnorderedPair.of("sentence", "line"), 0.3);

        LevenshteinSimilarity similarity = new LevenshteinSimilarityBuilder()
                .setImplication(new ResidualImplication(0.8))
                .setReplacementCosts(costs)
                .setTextSplitter(new SentenceSplitter())
                .build();
        String check = "this is a test sentence";

        Assert.assertEquals(1.0, similarity.apply(check, "this is a test sentence"), 0.001);
        Assert.assertEquals(0.94, similarity.apply(check, "this is a test line"), 0.001);
        Assert.assertEquals(1.0, similarity.apply(check, "this is a test verse"), 0.001);
        Assert.assertEquals(0.8, similarity.apply(check, "this a test verse"), 0.001);
        Assert.assertEquals(0.74, similarity.apply(check, "this a test line"), 0.001);
    }
}
