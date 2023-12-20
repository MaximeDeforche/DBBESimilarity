package be.ugent.telin.ddcm.distance;

import be.ugent.telin.ddcm.implication.ResidualImplication;
import be.ugent.telin.ddcm.similarity.LevenshteinSimilarityBuilder;
import be.ugent.telin.ddcm.splitter.SentenceSplitter;
import be.ugent.telin.ddcm.splitter.TextSplitter;
import be.ugent.telin.ddcm.splitter.WordSplitter;
import be.ugent.telin.ddcm.util.objects.Pair;
import be.ugent.telin.ddcm.util.objects.UnorderedPair;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LevenshteinDamerauEditDistanceTests {

    @Test
    public void simpleTests() {
        Distance distance = new LevenshteinSimilarityBuilder()
                .isDamerau(true)
                .build().getDistance();
        TextSplitter splitter = new WordSplitter();

        Distance distanceNormal = new LevenshteinSimilarityBuilder()
                .isDamerau(false)
                .build().getDistance();

        String[] check = splitter.apply("test");

        //Normal Levenshtein
        Assert.assertEquals(0.0, distance.apply(check, splitter.apply("test")), 0.001);
        Assert.assertEquals(1.0, distance.apply(check, splitter.apply("tost")), 0.001);
        Assert.assertEquals(1.0, distance.apply(check, splitter.apply("tst")), 0.001);
        Assert.assertEquals(1.0, distance.apply(check, splitter.apply("testt")), 0.001);
        Assert.assertEquals(3.0, distance.apply(splitter.apply("elephant"), splitter.apply("relevant")), 0.001);

        Assert.assertEquals(1.0, distance.apply(check, splitter.apply("tets")), 0.001);
        Assert.assertEquals(2.0, distanceNormal.apply(check, splitter.apply("tets")), 0.001);

        Assert.assertEquals(4.0, distance.apply(check, splitter.apply("")), 0.0001);
    }

    @Test
    public void costsTests() {
        Distance distance = new LevenshteinSimilarityBuilder()
                .isDamerau(true)
                .setInsertionDeletionCost(0.9)
                .setReplacementCost(0.7)
                .setTranspositionCost(0.8)
                .build().getDistance();
        TextSplitter splitter = new WordSplitter();

        String[] check = splitter.apply("test");

        Assert.assertEquals(0.0, distance.apply(check, splitter.apply("test")), 0.001);
        Assert.assertEquals(0.7, distance.apply(check, splitter.apply("tost")), 0.001);
        Assert.assertEquals(0.9, distance.apply(check, splitter.apply("tst")), 0.001);
        Assert.assertEquals(0.9, distance.apply(check, splitter.apply("testt")), 0.001);
        Assert.assertEquals(2.5, distance.apply(splitter.apply("elephant"), splitter.apply("relevant")), 0.001);

        Assert.assertEquals(0.8, distance.apply(check, splitter.apply("tets")), 0.001);
        Assert.assertEquals(3.3, distance.apply(splitter.apply("elephant"), splitter.apply("relevatn")), 0.001);

        Assert.assertEquals(3.6, distance.apply(check, splitter.apply("")), 0.0001);
    }

    @Test
    public void replacementCostsTests() {
        Map<UnorderedPair<String>, Double> costs = new HashMap<>();
        costs.put(UnorderedPair.of("e", "a"), 0.0);
        costs.put(UnorderedPair.of("e", "i"), 0.5);

        LevenshteinEditDistance distance = new LevenshteinSimilarityBuilder()
                .isDamerau(true)
                .setReplacementCosts(costs)
                .build().getDistance();
        TextSplitter splitter = new WordSplitter();

        String[] check = splitter.apply("test");

        Assert.assertEquals(0.0, distance.apply(check, splitter.apply("test")), 0.001);
        Assert.assertEquals(1.0, distance.apply(check, splitter.apply("tost")), 0.001);
        Assert.assertEquals(0.0, distance.apply(check, splitter.apply("tast")), 0.001);
        Assert.assertEquals(0.5, distance.apply(check, splitter.apply("tist")), 0.001);
        Assert.assertEquals(1.5, distance.apply(check, splitter.apply("tistt")), 0.001);

        Assert.assertEquals(1.0, distance.apply(check, splitter.apply("tset")), 0.001);
        Assert.assertEquals(1.0, distance.apply(check, splitter.apply("tsat")), 0.001);

        distance.addReplacementCost(UnorderedPair.of("e", "a"), 0.2);

        Assert.assertEquals(0.2, distance.apply(check, splitter.apply("tast")), 0.001);
    }

    @Test
    public void replacementCostsExceptionsTests() {
        Map<UnorderedPair<String>, Double> costs = new HashMap<>();
        costs.put(UnorderedPair.of("e", "a"), 0.0);
        costs.put(UnorderedPair.of("e", "i"), 0.5);

        Set<Pair<String, String>> exceptions = new HashSet<>();
        exceptions.add(Pair.of("z", "e"));

        LevenshteinEditDistance distance = new LevenshteinSimilarityBuilder()
                .isDamerau(true)
                .setReplacementCosts(costs)
                .setReplacementCostTableExceptions(exceptions)
                .build().getDistance();
        TextSplitter splitter = new WordSplitter();

        String[] check = splitter.apply("test");
        String[] check2 = splitter.apply("zest");

        Assert.assertEquals(0.0, distance.apply(check, splitter.apply("test")), 0.001);
        Assert.assertEquals(1.0, distance.apply(check, splitter.apply("tost")), 0.001);
        Assert.assertEquals(0.0, distance.apply(check, splitter.apply("tast")), 0.001);
        Assert.assertEquals(0.5, distance.apply(check, splitter.apply("tist")), 0.001);
        Assert.assertEquals(1.5, distance.apply(check, splitter.apply("tistt")), 0.001);

        Assert.assertEquals(1.0, distance.apply(check, splitter.apply("tset")), 0.001);
        Assert.assertEquals(1.0, distance.apply(check, splitter.apply("tsat")), 0.001);

        Assert.assertEquals(0.0, distance.apply(check2, splitter.apply("zest")), 0.001);
        Assert.assertEquals(1.0, distance.apply(check2, splitter.apply("zost")), 0.001);
        Assert.assertEquals(1.0, distance.apply(check2, splitter.apply("zast")), 0.001);
        Assert.assertEquals(1.0, distance.apply(check2, splitter.apply("zist")), 0.001);
        Assert.assertEquals(2.0, distance.apply(check2, splitter.apply("zistt")), 0.001);

        Assert.assertEquals(1.0, distance.apply(check2, splitter.apply("zset")), 0.001);
        Assert.assertEquals(2.0, distance.apply(check2, splitter.apply("zsat")), 0.001);

        distance.addReplacementCost(UnorderedPair.of("e", "a"), 0.2);

        Assert.assertEquals(0.2, distance.apply(check, splitter.apply("tast")), 0.001);
    }

    @Test
    public void implicationTests() {
        Map<UnorderedPair<String>, Double> costs = new HashMap<>();
        costs.put(UnorderedPair.of("e", "a"), 0.1);
        costs.put(UnorderedPair.of("e", "i"), 0.3);

        Distance distance = new LevenshteinSimilarityBuilder()
                .isDamerau(true)
                .setImplication(new ResidualImplication(0.8))
                .setReplacementCosts(costs)
                .build().getDistance();
        TextSplitter splitter = new WordSplitter();

        String[] check = splitter.apply("test");

        Assert.assertEquals(0.0, distance.apply(check, splitter.apply("test")), 0.001);
        Assert.assertEquals(1.0, distance.apply(check, splitter.apply("tost")), 0.001);
        Assert.assertEquals(0.0, distance.apply(check, splitter.apply("tast")), 0.001);
        Assert.assertEquals(0.3, distance.apply(check, splitter.apply("tist")), 0.001);
        Assert.assertEquals(1.0, distance.apply(check, splitter.apply("tastt")), 0.001);
        Assert.assertEquals(1.3, distance.apply(check, splitter.apply("tistt")), 0.001);
    }

    @Test
    public void transpositionMatchThresholdTests() {
        Map<UnorderedPair<String>, Double> costs = new HashMap<>();
        costs.put(UnorderedPair.of("s", "z"), 0.25);

        Distance distance = new LevenshteinSimilarityBuilder()
                .isDamerau(true)
                .setTranspositionMatchThreshold(0.75)
                .setReplacementCosts(costs)
                .build()
                .getDistance();
        TextSplitter splitter = new WordSplitter();

        String[] check = splitter.apply("test");

        Assert.assertEquals(0.0, distance.apply(check, splitter.apply("test")), 0.001);
        Assert.assertEquals(1.0, distance.apply(check, splitter.apply("tset")), 0.001);
        Assert.assertEquals(1.0, distance.apply(check, splitter.apply("tetz")), 0.001);
    }

    @Test
    public void simpleVerseTests() {
        Distance distance = new LevenshteinSimilarityBuilder()
                .isDamerau(true)
                .build().getDistance();
        TextSplitter splitter = new SentenceSplitter();

        String[] check = splitter.apply("this is a test sentence");

        Assert.assertEquals(0.0, distance.apply(check, splitter.apply("this is a test sentence")), 0.001);
        Assert.assertEquals(1.0, distance.apply(check, splitter.apply("this is o test sentence")), 0.001);
        Assert.assertEquals(1.0, distance.apply(check, splitter.apply("this is a special sentence")), 0.001);
        Assert.assertEquals(1.0, distance.apply(check, splitter.apply("this is a special test sentence")), 0.001);
        Assert.assertEquals(1.0, distance.apply(check, splitter.apply("this is a sentence")), 0.001);
        Assert.assertEquals(3.0, distance.apply(check, splitter.apply("is o special test sentence")), 0.001);

        Assert.assertEquals(1.0, distance.apply(check, splitter.apply("this a is test sentence")), 0.001);
        Assert.assertEquals(4.0, distance.apply(check, splitter.apply("is o special sentence test")), 0.001);
    }

    @Test
    public void costsVerseTests() {
        Distance distance = new LevenshteinSimilarityBuilder()
                .isDamerau(true)
                .setInsertionDeletionCost(0.9)
                .setReplacementCost(0.7)
                .setTranspositionCost(0.8)
                .build().getDistance();
        TextSplitter splitter = new SentenceSplitter();

        String[] check = splitter.apply("this is a test sentence");

        Assert.assertEquals(0.0, distance.apply(check, splitter.apply("this is a test sentence")), 0.001);
        Assert.assertEquals(0.7, distance.apply(check, splitter.apply("this is o test sentence")), 0.001);
        Assert.assertEquals(0.7, distance.apply(check, splitter.apply("this is a special sentence")), 0.001);
        Assert.assertEquals(0.9, distance.apply(check, splitter.apply("this is a special test sentence")), 0.001);
        Assert.assertEquals(0.9, distance.apply(check, splitter.apply("this is a sentence")), 0.001);
        Assert.assertEquals(2.5, distance.apply(splitter.apply("e l e p h a n t"), splitter.apply("r e l e v a n t")), 0.001);

        Assert.assertEquals(0.8, distance.apply(check, splitter.apply("this a is test sentence")), 0.001);
        Assert.assertEquals(3.3, distance.apply(splitter.apply("e l e p h a n t"), splitter.apply("r e l e v a t n")), 0.001);
    }

    @Test
    public void replacementCostsVerseTests() {
        Map<UnorderedPair<String>, Double> costs = new HashMap<>();
        costs.put(UnorderedPair.of("sentence", "verse"), 0.2);

        LevenshteinEditDistance distance = new LevenshteinSimilarityBuilder()
                .isDamerau(true)
                .setReplacementCosts(costs)
                .build().getDistance();
        TextSplitter splitter = new SentenceSplitter();

        String[] check = splitter.apply("this is a test sentence");

        Assert.assertEquals(0.0, distance.apply(check, splitter.apply("this is a test sentence")), 0.001);
        Assert.assertEquals(1.0, distance.apply(check, splitter.apply("this is a test line")), 0.001);
        Assert.assertEquals(0.2, distance.apply(check, splitter.apply("this is a test verse")), 0.001);
        Assert.assertEquals(1.2, distance.apply(check, splitter.apply("this a test verse")), 0.001);

        distance.addReplacementCost(UnorderedPair.of("sentence", "line"), 0.8);

        Assert.assertEquals(0.8, distance.apply(check, splitter.apply("this is a test line")), 0.001);

        distance.addReplacementCost(UnorderedPair.of("sentence", "line"), 0.0);

        Assert.assertEquals(1.0, distance.apply(check, splitter.apply("this is a line test")), 0.001);
    }

    @Test
    public void implicationVerseTests() {
        Map<UnorderedPair<String>, Double> costs = new HashMap<>();
        costs.put(UnorderedPair.of("sentence", "verse"), 0.1);
        costs.put(UnorderedPair.of("sentence", "line"), 0.3);

        LevenshteinEditDistance distance = new LevenshteinSimilarityBuilder()
                .isDamerau(true)
                .setImplication(new ResidualImplication(0.8))
                .setReplacementCosts(costs)
                .build().getDistance();
        TextSplitter splitter = new SentenceSplitter();

        String[] check = splitter.apply("this is a test sentence");

        Assert.assertEquals(0.0, distance.apply(check, splitter.apply("this is a test sentence")), 0.001);
        Assert.assertEquals(0.3, distance.apply(check, splitter.apply("this is a test line")), 0.001);
        Assert.assertEquals(0.0, distance.apply(check, splitter.apply("this is a test verse")), 0.001);
        Assert.assertEquals(1.0, distance.apply(check, splitter.apply("this a test verse")), 0.001);
        Assert.assertEquals(1.3, distance.apply(check, splitter.apply("this a test line")), 0.001);
    }

    @Test
    public void transpositionMatchThresholdVerseTests() {
        Map<UnorderedPair<String>, Double> costs = new HashMap<>();
        costs.put(UnorderedPair.of("test", "tets"), 0.25);

        Distance distance = new LevenshteinSimilarityBuilder()
                .isDamerau(true)
                .setTranspositionMatchThreshold(0.75)
                .setReplacementCosts(costs)
                .build()
                .getDistance();
        TextSplitter splitter = new SentenceSplitter();

        String[] check = splitter.apply("this is a test");

        Assert.assertEquals(0.0, distance.apply(check, splitter.apply("this is a test")), 0.001);
        Assert.assertEquals(1.0, distance.apply(check, splitter.apply("this is test a")), 0.001);
        Assert.assertEquals(1.0, distance.apply(check, splitter.apply("this is tets a")), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalArgumentTest1() {
        LevenshteinEditDistance distance = new LevenshteinSimilarityBuilder()
                .isDamerau(true)
                .build().getDistance();

        distance.apply(null, new WordSplitter().apply("test"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalArgumentTest2() {
        new LevenshteinSimilarityBuilder()
                .isDamerau(true)
                .setInsertionDeletionCost(0.5)
                .setReplacementCost(0.5)
                .setTranspositionCost(1.5)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalArgumentTest3() {
        LevenshteinEditDistance distance = new LevenshteinSimilarityBuilder().isDamerau(true).build().getDistance();
        distance.addReplacementCost(UnorderedPair.of("test", "pair"), 1.5);
    }
}
