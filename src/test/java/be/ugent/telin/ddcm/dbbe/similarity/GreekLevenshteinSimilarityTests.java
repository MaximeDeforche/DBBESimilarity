package be.ugent.telin.ddcm.dbbe.similarity;

import be.ugent.telin.ddcm.dbbe.splitter.GreekWordSplitter;
import be.ugent.telin.ddcm.distance.LevenshteinEditDistance;
import be.ugent.telin.ddcm.similarity.LevenshteinSimilarity;
import be.ugent.telin.ddcm.similarity.LevenshteinSimilarityBuilder;
import org.junit.Assert;
import org.junit.Test;

public class GreekLevenshteinSimilarityTests {

    @Test
    public void greekWordSplitterLevenshteinSimilarityTests() {
        LevenshteinSimilarity similarity = new LevenshteinSimilarityBuilder()
                .setTextSplitter(new GreekWordSplitter())
                .setReplacementCosts(GreekWordSplitter.replacementCosts)
                .build();

        //Assert.assertEquals(1.0, similarity.apply("ωσπερ", "οσπερ"), 0.001);
        Assert.assertEquals(1.0, similarity.apply("ιδειν", "ηδειν"), 0.001);
        Assert.assertEquals(1.0, similarity.apply("ιδειν", "ιδην"), 0.001);
        Assert.assertEquals(1.0, similarity.apply("ιδειν", "ηδην"), 0.001);
        Assert.assertEquals(1.0, similarity.apply("ιδην", "ηδοιν"), 0.001);

        Assert.assertEquals(0.8, similarity.apply("ιδεινσ", "ιδην"), 0.001);
    }
}
