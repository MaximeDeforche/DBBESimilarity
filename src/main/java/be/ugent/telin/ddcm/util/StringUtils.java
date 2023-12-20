package be.ugent.telin.ddcm.util;

import be.ugent.telin.ddcm.transformer.NoSpaceTextTransformer;
import be.ugent.telin.ddcm.transformer.TextTransformer;

public class StringUtils {

    private static TextTransformer noSpaceTransformer = new NoSpaceTextTransformer();

    public static String getNormalizedFlattenedString(String[] text) {
        return noSpaceTransformer.apply(String.join("", text));
    }

    public static String getNormalizedString(String text) {
        return noSpaceTransformer.apply(text);
    }
}
