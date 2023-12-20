package be.ugent.telin.ddcm.util;

import be.ugent.telin.ddcm.transformer.TextTransformer;

import java.util.List;

public class TextTransformerUtils {

    public static String applyTransformations(String s, TextTransformer... transformers) {
        String res = s;

        for (TextTransformer t : transformers) {
            res = t.apply(res);
        }

        return res;
    }

    public static String applyTransformations(String s, List<TextTransformer> transformers) {
        String res = s;

        for (TextTransformer t : transformers) {
            res = t.apply(res);
        }

        return res;
    }

}
