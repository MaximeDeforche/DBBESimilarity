package be.ugent.telin.ddcm.splitter;

import org.junit.Assert;
import org.junit.Test;

public class SplitterTests {

    @Test
    public void wordSplitterTests() {
        TextSplitter wordSplitter = new WordSplitter();
        String s = "Bananenbrood";
        String t = "Dit is een test";

        Assert.assertArrayEquals(new String[] {"B", "a", "n", "a", "n", "e", "n", "b", "r", "o", "o", "d"}, wordSplitter.apply(s));
        Assert.assertArrayEquals(new String[] {"D", "i", "t", " ", "i", "s", " ", "e", "e", "n", " ", "t", "e", "s", "t"}, wordSplitter.apply(t));

        String empty = "";
        String[] split = wordSplitter.apply(empty);

        Assert.assertEquals(0, split.length);
        Assert.assertArrayEquals(split, new String[] {});
    }

    @Test
    public void sentenceSplitterTests() {
        TextSplitter sentenceSplitter = new SentenceSplitter();
        String s = "Bananenbrood";
        String t = "Dit is een test";

        Assert.assertArrayEquals(new String[] {"Bananenbrood"}, sentenceSplitter.apply(s));
        Assert.assertArrayEquals(new String[] {"Dit", "is", "een", "test"}, sentenceSplitter.apply(t));

        String empty = "";
        String[] split = sentenceSplitter.apply(empty);

        Assert.assertEquals(0, split.length);
        Assert.assertArrayEquals(split, new String[] {});
    }
}
