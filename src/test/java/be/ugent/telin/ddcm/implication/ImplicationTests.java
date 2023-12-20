package be.ugent.telin.ddcm.implication;

import org.junit.Assert;
import org.junit.Test;

public class ImplicationTests {

    @Test
    public void defaultImplicationTests() {
        Implication implication = new DefaultImplication();

        Assert.assertEquals(0.2, implication.apply(0.2), 0.0001);
        Assert.assertEquals(0.7, implication.apply(0.7), 0.0001);

        Assert.assertEquals(0.2, implication.applyInverse(0.2), 0.0001);
        Assert.assertEquals(0.7, implication.applyInverse(0.7), 0.0001);
    }

    @Test
    public void residualImplicationTests() {
        Implication implication = new ResidualImplication(0.85);

        Assert.assertEquals(0.6, implication.apply(0.6), 0.0001);
        Assert.assertEquals(1.0, implication.apply(0.9), 0.0001);

        Assert.assertEquals(0.4, implication.applyInverse(0.4), 0.0001);
        Assert.assertEquals(0.0, implication.applyInverse(0.1), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalArgumentExceptionTest() {
        new ResidualImplication(1.5);
    }
}
