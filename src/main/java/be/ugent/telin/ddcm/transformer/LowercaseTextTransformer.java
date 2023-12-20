package be.ugent.telin.ddcm.transformer;

public class LowercaseTextTransformer implements TextTransformer {

    @Override
    public String apply(String s) {
        return s.toLowerCase();
    }
}
