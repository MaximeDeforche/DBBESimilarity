package be.ugent.telin.ddcm.util;

import be.ugent.telin.ddcm.util.objects.UnorderedPair;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class UnorderedPairTests {

    @Test
    public void testInts() {
        UnorderedPair<Integer> p1 = new UnorderedPair<>(5, 10);
        Set<Integer> s1 = new HashSet<>(Arrays.asList(5, 10));

        Assert.assertEquals(s1, p1.getElements());

        UnorderedPair<Integer> p2 = UnorderedPair.of(10, 5);
        Set<Integer> s2 = new HashSet<>(Arrays.asList(10, 5));

        Assert.assertEquals(s2, p2.getElements());

        Assert.assertEquals(p1, p2);

        UnorderedPair<Integer> p3 = new UnorderedPair<>(5, 8);
        Set<Integer> s3 = new HashSet<>(Arrays.asList(5, 8));

        Assert.assertEquals(s3, p3.getElements());

        Assert.assertNotEquals(p1, p3);
        Assert.assertNotEquals(p2, p3);

        Set<UnorderedPair<Integer>> s = new HashSet<>();

        s.add(p1);

        Assert.assertEquals(1, s.size());
        Assert.assertTrue(s.contains(p1));
        Assert.assertTrue(s.contains(p2));

        s.add(p2);

        Assert.assertEquals(1, s.size());
        Assert.assertTrue(s.contains(p1));
        Assert.assertTrue(s.contains(p2));

        s.add(p3);

        Assert.assertEquals(2, s.size());
        Assert.assertTrue(s.contains(p1));
        Assert.assertTrue(s.contains(p2));
        Assert.assertTrue(s.contains(p3));

        Assert.assertTrue("UnorderedPair{elements={5, 10}}".equals(p1.toString())
                || "UnorderedPair{elements={10, 5}}".equals(p1.toString()));
    }

    @Test
    public void testStrings() {
        UnorderedPair<String> p1 = new UnorderedPair<>("cat", "dog");
        Set<String> s1 = new HashSet<>(Arrays.asList("cat", "dog"));

        Assert.assertEquals(s1, p1.getElements());

        UnorderedPair<String> p2 = UnorderedPair.of("dog", "cat");
        Set<String> s2 = new HashSet<>(Arrays.asList("dog", "cat"));

        Assert.assertEquals(s2, p2.getElements());

        Assert.assertEquals(p1, p2);

        UnorderedPair<String> p3 = new UnorderedPair<>("cat", "cow");
        Set<String> s3 = new HashSet<>(Arrays.asList("cat", "cow"));

        Assert.assertEquals(s3, p3.getElements());

        Assert.assertNotEquals(p1, p3);
        Assert.assertNotEquals(p2, p3);

        Set<UnorderedPair<String>> s = new HashSet<>();

        s.add(p1);

        Assert.assertEquals(1, s.size());
        Assert.assertTrue(s.contains(p1));
        Assert.assertTrue(s.contains(p2));

        s.add(p2);

        Assert.assertEquals(1, s.size());
        Assert.assertTrue(s.contains(p1));
        Assert.assertTrue(s.contains(p2));

        s.add(p3);

        Assert.assertEquals(2, s.size());
        Assert.assertTrue(s.contains(p1));
        Assert.assertTrue(s.contains(p2));
        Assert.assertTrue(s.contains(p3));

        Assert.assertTrue("UnorderedPair{elements={dog, cat}}".equals(p1.toString())
                || "UnorderedPair{elements={cat, dog}}".equals(p1.toString()));
    }
}
