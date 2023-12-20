package be.ugent.telin.ddcm.util;

import be.ugent.telin.ddcm.util.objects.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class PairTests {

    @Test
    public void testInts() {
        Pair<Integer, Integer> p1 = new Pair<>(1, 2);
        Pair<Integer, Integer> p2 = new Pair<>(2, 1);
        Pair<Integer, Integer> p3 = Pair.of(1, 2);

        Assert.assertEquals(1, (int) p1.getFirst());
        Assert.assertEquals(2, (int) p1.getSecond());

        Assert.assertEquals(2, (int) p2.getFirst());
        Assert.assertEquals(1, (int) p2.getSecond());

        Assert.assertEquals(1, (int) p3.getFirst());
        Assert.assertEquals(2, (int) p3.getSecond());

        Assert.assertNotEquals(p1, p2);
        Assert.assertNotEquals(p2, p3);
        Assert.assertEquals(p1, p3);

        Set<Pair<Integer, Integer>> s = new HashSet<>();

        s.add(p1);
        Assert.assertEquals(1, s.size());
        Assert.assertTrue(s.contains(p1));
        Assert.assertFalse(s.contains(p2));
        Assert.assertTrue(s.contains(p3));

        s.add(p2);
        Assert.assertEquals(2, s.size());
        Assert.assertTrue(s.contains(p2));

        s.add(p3);
        Assert.assertEquals(2, s.size());

        Assert.assertEquals("Pair{first=1,second=2}", p1.toString());
        Assert.assertEquals("Pair{first=2,second=1}", p2.toString());
    }

    @Test
    public void testStrings() {
        Pair<String, String> p1 = new Pair<>("cat", "dog");
        Pair<String, String> p2 = new Pair<>("dog", "cat");
        Pair<String, String> p3 = Pair.of("cat", "dog");

        Assert.assertEquals("cat", p1.getFirst());
        Assert.assertEquals("dog", p1.getSecond());

        Assert.assertEquals("dog", p2.getFirst());
        Assert.assertEquals("cat", p2.getSecond());

        Assert.assertEquals("cat", p3.getFirst());
        Assert.assertEquals("dog", p3.getSecond());

        Assert.assertNotEquals(p1, p2);
        Assert.assertNotEquals(p2, p3);
        Assert.assertEquals(p1, p3);

        Set<Pair<String, String>> s = new HashSet<>();

        s.add(p1);
        Assert.assertEquals(1, s.size());
        Assert.assertTrue(s.contains(p1));
        Assert.assertFalse(s.contains(p2));
        Assert.assertTrue(s.contains(p3));

        s.add(p2);
        Assert.assertEquals(2, s.size());
        Assert.assertTrue(s.contains(p2));

        s.add(p3);
        Assert.assertEquals(2, s.size());

        Assert.assertEquals("Pair{first=cat,second=dog}", p1.toString());
        Assert.assertEquals("Pair{first=dog,second=cat}", p2.toString());
    }

    @Test
    public void testMixed() {
        Pair<Integer, String> p1 = new Pair<>(1, "dog");
        Pair<String, Integer> p2 = new Pair<>("dog", 1);
        Pair<Integer, String> p3 = Pair.of(1, "dog");

        Assert.assertEquals(1, (int) p1.getFirst());
        Assert.assertEquals("dog", p1.getSecond());

        Assert.assertEquals("dog", p2.getFirst());
        Assert.assertEquals(1, (int) p2.getSecond());

        Assert.assertEquals(1, (int) p3.getFirst());
        Assert.assertEquals("dog", p3.getSecond());

        Assert.assertNotEquals(p1, p2);
        Assert.assertNotEquals(p2, p3);
        Assert.assertEquals(p1, p3);

        Set<Pair<?, ?>> s = new HashSet<>();

        s.add(p1);
        Assert.assertEquals(1, s.size());
        Assert.assertTrue(s.contains(p1));
        Assert.assertFalse(s.contains(p2));
        Assert.assertTrue(s.contains(p3));

        s.add(p2);
        Assert.assertEquals(2, s.size());
        Assert.assertTrue(s.contains(p2));

        s.add(p3);
        Assert.assertEquals(2, s.size());

        Assert.assertEquals("Pair{first=1,second=dog}", p1.toString());
        Assert.assertEquals("Pair{first=dog,second=1}", p2.toString());
    }
}
