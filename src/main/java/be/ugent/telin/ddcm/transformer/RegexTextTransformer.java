package be.ugent.telin.ddcm.transformer;

public class RegexTextTransformer implements TextTransformer {

    private final String match;
    private final String replace;

    public RegexTextTransformer(String match, String replace) {
        this.match = match;
        this.replace = replace;
    }

    @Override
    public String apply(String s) {
        return s.replaceAll(match, replace);
    }
}
