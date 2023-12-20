package be.ugent.telin.ddcm.dbbe.splitter;

import be.ugent.telin.ddcm.splitter.TextSplitter;
import org.junit.Assert;
import org.junit.Test;

public class DBBETextSplitterTests {

    @Test
    public void greekWordSplitterTests() {
        TextSplitter wordSplitter = new GreekWordSplitter();

        String s1 = "ιδειν";

        Assert.assertArrayEquals(new String[] {"ι", "δ", "ει", "ν"}, wordSplitter.apply(s1));

        String empty = "";
        String[] split = wordSplitter.apply(empty);

        Assert.assertEquals(0, split.length);
        Assert.assertArrayEquals(split, new String[] {});
    }
}
