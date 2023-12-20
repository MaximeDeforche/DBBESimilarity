package be.ugent.telin.ddcm.transformer;

import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;

public class CypherTransformers {

    private static final LowercaseTextTransformer lowercaseTextTransformer = new LowercaseTextTransformer();
    private static final SingleSpaceTextTransformer singleSpaceTransformer = new SingleSpaceTextTransformer();
    private static final NoNewLineTextTransformer noNewLineTransformer = new NoNewLineTextTransformer();
    private static final NoPunctuationTextTransformer noPunctuationTransformer = new NoPunctuationTextTransformer();
    private static final NoSpaceTextTransformer noSpaceTransformer = new NoSpaceTextTransformer();

    @UserFunction("ddcm.transform.lower")
    @Description("")
    public String toLowerCase(@Name("string") String s) {
        return lowercaseTextTransformer.apply(s);
    }

    @UserFunction("ddcm.transform.singleSpace")
    @Description("")
    public String toSingleSpace(@Name("string") String s) {
        return singleSpaceTransformer.apply(s);
    }

    @UserFunction("ddcm.transform.noNewLine")
    @Description("")
    public String noNewLine(@Name("string") String s) {
        return noNewLineTransformer.apply(s);
    }

    @UserFunction("ddcm.transform.noPunctuation")
    @Description("")
    public String noPunctuation(@Name("string") String s) {
        return noPunctuationTransformer.apply(s);
    }

    @UserFunction("ddcm.transform.noSpace")
    @Description("")
    public String noSpace(@Name("string") String s) {
        return noSpaceTransformer.apply(s);
    }

    @UserFunction("ddcm.transform")
    @Description("")
    public String transform(@Name("string") String s, @Name("pattern") String pattern, @Name("replace") String replace) {
        return new RegexTextTransformer(pattern, replace).apply(s);
    }
}
