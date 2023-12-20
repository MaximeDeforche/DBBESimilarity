package be.ugent.telin.ddcm.aggregator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class AggregatorTests {

    private List<Double> list1;
    private List<Double> list2;

    @Before
    public void setup() {
        list1 = new ArrayList<>();
        list2 = new ArrayList<>();

        list1.add(1.0);
        list1.add(2.0);
        list1.add(3.0);

        list2.add(10.0);
        list2.add(15.0);
        list2.add(20.0);
        list2.add(0.5);
        list2.add(100.0);
    }

    @Test
    public void maxAggregatorTests() {
        Aggregator maxAggregator = new MaxAggregator();
        Assert.assertEquals(3.0, maxAggregator.apply(list1), 0.0001);
        Assert.assertEquals(100.0, maxAggregator.apply(list2), 0.0001);
    }

    @Test
    public void minAggregatorTests() {
        Aggregator minAggregator = new MinAggregator();
        Assert.assertEquals(1.0, minAggregator.apply(list1), 0.0001);
        Assert.assertEquals(0.5, minAggregator.apply(list2), 0.0001);
    }

    @Test
    public void avgAggregatorTests() {
        Aggregator avgAggregator = new AvgAggregator();
        Assert.assertEquals(2.0, avgAggregator.apply(list1), 0.0001);
        Assert.assertEquals(29.1, avgAggregator.apply(list2), 0.0001);
    }
}
