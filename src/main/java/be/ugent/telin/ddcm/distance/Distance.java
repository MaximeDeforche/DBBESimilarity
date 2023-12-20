package be.ugent.telin.ddcm.distance;

/**
 * Generic class for representing edit distance calculators between two strings. Editing distances extending this
 * interface should be capable of calculating an edit distance on more than only text characters as textual units. E.g.
 * the calculation based on words, verses, sentences etc should also be possible. See the apply function for more
 * details.
 *
 * @author Maxime Deforche
 */
public interface Distance {

    /**
     * Calculate the edit distance between two {@link String} arrays.
     * The use of {@link String} arrays allows the calculation of edit distance based on other textual units, like words
     * or verses, unlike the traditional edit distance that is calculated based on individual characters.
     *
     * @param a The first text.
     * @param b The other text.
     * @return A {@link Double} value representing the edit distance between both strings.
     */
    double apply(String[] a, String[] b);
}
