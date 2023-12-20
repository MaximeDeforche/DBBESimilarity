package be.ugent.telin.ddcm.transformer;

import be.ugent.telin.ddcm.util.TextTransformerUtils;
import org.junit.Assert;
import org.junit.Test;

public class TextTransformerTests {

    @Test
    public void singleSpaceTransformerTests() {
        TextTransformer singleSpaceTextTransformer = new SingleSpaceTextTransformer();
        String s = "This is  a test      sentence.";

        Assert.assertEquals("This is a test sentence.", singleSpaceTextTransformer.apply(s));
    }

    @Test
    public void lowerCaseTransformerTests() {
        TextTransformer lowercaseTextTransformer = new LowercaseTextTransformer();
        String s = "This is a tEsT sentence.";

        Assert.assertEquals("this is a test sentence.", lowercaseTextTransformer.apply(s));
    }

    @Test
    public void noNewlineTransformerTests() {
        TextTransformer noNewLineTextTransformer = new NoNewLineTextTransformer();
        String s = "This is\n a test";

        Assert.assertEquals("This is  a test", noNewLineTextTransformer.apply(s));
    }

    @Test
    public void noPunctuationTransformerTests() {
        TextTransformer noPunctuationTextTransformer = new NoPunctuationTextTransformer();
        String s = "This. is? a! test, sentence' without\" punctuation";

        Assert.assertEquals("This is a test sentence without punctuation", noPunctuationTextTransformer.apply(s));
    }

    @Test
    public void noSpaceTransformerTests() {
        TextTransformer noSpaceTextTransformer = new NoSpaceTextTransformer();
        String s = "This is a test";

        Assert.assertEquals("Thisisatest", noSpaceTextTransformer.apply(s));
    }

    @Test
    public void regexTransformerTests() {
        TextTransformer regexTextTransformer = new RegexTextTransformer("a", "b");
        String s = "This is a test a";

        Assert.assertEquals("This is b test b", regexTextTransformer.apply(s));
    }

    @Test
    public void allTransformersTests() {
        TextTransformer singleSpaceTextTransformer = new SingleSpaceTextTransformer();
        TextTransformer lowercaseTextTransformer = new LowercaseTextTransformer();

        String s = "This is  a tEsT      sentence.";
        Assert.assertEquals("this is a test sentence.", TextTransformerUtils.applyTransformations(s,
                singleSpaceTextTransformer, lowercaseTextTransformer));
    }

}
