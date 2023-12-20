package be.ugent.telin.ddcm.dbbe.distance;

import be.ugent.telin.ddcm.dbbe.splitter.GreekWordSplitter;
import be.ugent.telin.ddcm.distance.Distance;
import be.ugent.telin.ddcm.distance.LevenshteinEditDistance;
import be.ugent.telin.ddcm.similarity.LevenshteinSimilarityBuilder;
import be.ugent.telin.ddcm.splitter.TextSplitter;
import org.junit.Assert;
import org.junit.Test;

public class GreekLevenshteinEditDistanceTests {

    @Test
    public void greekWordSplitterLevenshteinEditDistanceTests() {
        Distance distance = new LevenshteinSimilarityBuilder()
                .setReplacementCosts(GreekWordSplitter.replacementCosts)
                .build().getDistance();
        TextSplitter splitter = new GreekWordSplitter();

        //Assert.assertEquals(0.0, distance.apply(splitter.apply("ωσπερ"), splitter.apply("οσπερ")), 0.001);
        Assert.assertEquals(0.0, distance.apply(splitter.apply("ιδειν"), splitter.apply("ηδειν")), 0.001);
        Assert.assertEquals(0.0, distance.apply(splitter.apply("ιδειν"), splitter.apply("ιδην")), 0.001);
        Assert.assertEquals(0.0, distance.apply(splitter.apply("ιδειν"), splitter.apply("ηδην")), 0.001);
        Assert.assertEquals(0.0, distance.apply(splitter.apply("ιδην"), splitter.apply("ηδοιν")), 0.001);
    }
}
