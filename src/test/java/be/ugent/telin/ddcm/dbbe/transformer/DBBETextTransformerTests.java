package be.ugent.telin.ddcm.dbbe.transformer;

import be.ugent.telin.ddcm.transformer.LowercaseTextTransformer;
import be.ugent.telin.ddcm.transformer.NoPunctuationTextTransformer;
import be.ugent.telin.ddcm.transformer.SingleSpaceTextTransformer;
import be.ugent.telin.ddcm.transformer.TextTransformer;
import be.ugent.telin.ddcm.util.TextTransformerUtils;
import org.junit.Assert;
import org.junit.Test;

public class DBBETextTransformerTests {

    @Test
    public void lowerCaseGreekTransformerTests() {
        TextTransformer lowercaseTextTransformer = new LowercaseTextTransformer();
        String s = "Τῷ σὺντελεστῆ τῶν καλῶν Θ(ε)ῶ χάρις:–";

        Assert.assertEquals("τῷ σὺντελεστῆ τῶν καλῶν θ(ε)ῶ χάρις:–", lowercaseTextTransformer.apply(s));
    }

    @Test
    public void specialCharacterTransformerTests() {
        TextTransformer specialCharacterTextTransformer = new SpecialCharacterTextTransformer();
        String s = "τῷ σὺντελεστῆ τῶν καλῶν θ(ε)ῶ χάρις:–";

        Assert.assertEquals("τῷ σὺντελεστῆ τῶν καλῶν θ(ε)ῶ χάρις", specialCharacterTextTransformer.apply(s));
    }

    @Test
    public void uncertaintyTransformerTests() {
        TextTransformer uncertaintyTextTransformer = new UncertaintyTextTransformer();
        String s = "τῷ σὺντελεστῆ τῶν καλῶν θ(ε)ῶ χάρις:–";

        Assert.assertEquals("τῷ σὺντελεστῆ τῶν καλῶν θεῶ χάρις:–", uncertaintyTextTransformer.apply(s));
    }

    @Test
    public void uncertaintyTransformer2Tests() {
        TextTransformer uncertaintyTextTransformer = new UncertaintyTextTransformer2();
        String s1 = "κτή(το)ρι";
        String s2 = "(...)";
        String s3 = "κτή(...)ρι";

        Assert.assertEquals("κτήτορι", uncertaintyTextTransformer.apply(s1));
        Assert.assertEquals("(...)", uncertaintyTextTransformer.apply(s2));
        Assert.assertEquals("κτή(...)ρι", uncertaintyTextTransformer.apply(s3));
    }

    @Test
    public void greekAccentTransformerTests() {
        TextTransformer greekAccentTextTransformer = new GreekAccentTextTransformer();
        String s = "τῷ σὺντελεστῆ τῶν καλῶν θ(ε)ῶ χάρις:–";

        Assert.assertEquals("τῳ συντελεστη τῳν καλῳν θ(ε)ῳ χαρις:–", greekAccentTextTransformer.apply(s));
    }

    @Test
    public void greekTextTransformersTests() {
        String s = "Τῷ σὺντελεστῆ τῶν καλῶν Θ(ε)ῶ χάρις:–";

        Assert.assertEquals("τῳ συντελεστη τῳν καλῳν θεῳ χαρις", TextTransformerUtils.applyTransformations(s,
                new LowercaseTextTransformer(),
                new SingleSpaceTextTransformer(),
                new NoPunctuationTextTransformer(),
                new SpecialCharacterTextTransformer(),
                new UncertaintyTextTransformer(),
                new GreekAccentTextTransformer()
        ));
    }

    @Test
    public void itacismTransformTests() {
        TextTransformer itacismTransformer = new ItacismTransformer();
        String s1 = "ιδειν";
        String s2 = "ιδην";
        String s3 = "ξενοι";
        String s4 = "ηδυν";
        String s5 = "ιδῃν";
        String s6 = "χαιροισιν";

        Assert.assertEquals("ιδιν", itacismTransformer.apply(s1));
        Assert.assertEquals("ιδιν", itacismTransformer.apply(s2));
        Assert.assertEquals("ξενι", itacismTransformer.apply(s3));
        Assert.assertEquals("ιδιν", itacismTransformer.apply(s4));
        Assert.assertEquals("ιδιν", itacismTransformer.apply(s5));
        Assert.assertEquals("χαιρισιν", itacismTransformer.apply(s6));
    }
}
