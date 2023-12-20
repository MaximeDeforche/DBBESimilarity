package be.ugent.telin.ddcm.distance;

import be.ugent.telin.ddcm.implication.Implication;
import be.ugent.telin.ddcm.util.StringUtils;
import be.ugent.telin.ddcm.util.objects.Pair;
import be.ugent.telin.ddcm.util.objects.UnorderedPair;

import java.util.Map;
import java.util.Set;

/**
 * A class used to calculate the Levenshtein edit distance between a pair of two {@link String}s. This class supports
 * a large amount of customizations. The indel and default replacement costs can be specified, specific pairs of strings
 * can have their own specific replacements costs and the additional result can be adapted by means of an implication.
 * This implementation allows calculating an edit distance on more than only text characters as textual units. E.g.
 * the calculation based on words, verses, sentences etc is also possible.
 *
 * Note that this implementation does only allow for costs in the [0.0,1.0] interval.
 *
 * Implements the {@link Distance} interface.
 *
 * @author Maxime Deforche
 */
public class LevenshteinEditDistance implements Distance {

    // Default costs
    public static final double DEFAULT_INSERTION_DELETION_COST = 1.0;
    public static final double DEFAULT_REPLACEMENT_COST = 1.0;

    // The insertion cost, set for this instance
    protected final double insertionDeletionCost;
    // The replacement cost, set for this instance
    protected final double replacementCost;
    // The implication class used for changing the calculated Levenshtein edit distance
    protected final Implication implication;
    protected final Implication reverseImplication;
    // Table holding the replacement costs for specific pairs of Strings
    protected final Map<UnorderedPair<String>, Double> replacementCostTable;

    protected final Set<Pair<String, String>> replacementCostTableExceptions;

    /**
     * Full-option Levenshtein Edit Distance constructor.
     *
     * @param insertionDeletionCost The (default) cost for insertions and deletions of textual units.
     * @param replacementCost The default cost for replacement between two textual units.
     * @param implication An implication (extending {@link Implication}) that is performed on dissimilarity between two
     *                    Strings when a replacement occurs.
     * @param reverseImplication An implication (extending {@link Implication}) that is inversly performed on dissimilarity between two
     *                           Strings when a replacement occurs.
     * @param replacementCostTable A default table of String pairs alongside their replacements costs when they differ
     *                             from the default replacement cost.
     * @param replacementCostTableExceptions A set of pairs indicating when the second text unit of the pair should not
     *                                       use the replacement value in the replacement values map, but instead the default
     *                                       replacement value is used.
     */
    public LevenshteinEditDistance(double insertionDeletionCost, double replacementCost, Implication implication
            , Map<UnorderedPair<String>, Double> replacementCostTable, Set<Pair<String, String>> replacementCostTableExceptions
            , Implication reverseImplication) {
        if (insertionDeletionCost < 0.0 || insertionDeletionCost > 1.0 || replacementCost < 0.0 || replacementCost > 1.0) { // Illegal costs
            throw new IllegalArgumentException("Insertion, Deletion or replacement costs should be in the interval [0.0, 1.0]");
        }

        this.insertionDeletionCost = insertionDeletionCost;
        this.replacementCost = replacementCost;
        this.implication = implication;
        this.replacementCostTable = replacementCostTable;
        this.replacementCostTableExceptions = replacementCostTableExceptions;
        this.reverseImplication = reverseImplication;

    }

    /**
     * Calculate the Levenshtein edit distance between two {@link String} arrays.
     * The use of {@link String} arrays allows the calculation of edit distance based on other textual units, like words
     * or verses, unlike the traditional edit distance that is calculated based on individual characters.
     *
     * @param a The first text.
     * @param b The other text.
     * @return The Levenshtein edit distance (double) between both text, based on the replacement values in this object.
     *
     * @throws IllegalArgumentException When one of the two texts are null, an exception is thrown
     */
    @Override
    public double apply(String[] a, String[] b) {
        if (a == null || b == null) { // Must be an array that is not null
            throw new IllegalArgumentException("Strings cannot be null");
        }

        return compare(a, b);
    }

    /**
     * Compare both texts and return the Levenshtein edit distance based on the attribute values of this class.
     * This calculation is based on the dynamic programming approach for calculating the Levenshtein edit distance.
     * In order to reduce the memory used in this function, only one row (n) of the nxm matrix is kept in memory, where
     * n < m in order to ensure that the smallest amount of memory is used. With |s1| as n and |s2| as m.
     *
     * @param s1 The first text.
     * @param s2 The second text.
     * @return The Levenshtein edit distance (double) between both texts s1 and s2.
     */
    protected double compare(String[] s1, String[] s2) {
        if (s1.length > s2.length) { //Reduce space by using the length of the shortest string
            String[] temp = s1;
            s1 = s2;
            s2 = temp;
        }

        int len1 = s1.length;
        int len2 = s2.length;

        // Return max cost based on the length when one of both texts are empty
        if (len1 == 0) return len2 * insertionDeletionCost;
        if (len2 == 0) return len1 * insertionDeletionCost;

        // Only keep one row of the nxm matrix.
        double[] row = new double[len1 + 1];

        // Loop variables
        int idx1;
        int idx2;
        double upperLeft; // The value to the upperleft cel of the current location in the row.
        double upper;     // The value to the upper cel of the current location in the row.
        double cost;
        String unit1; // Textual units
        String unit2;

        // Insert default values for the first row
        for (idx1 = 0; idx1 <= len1; idx1++) {
            row[idx1] = idx1 * insertionDeletionCost;
        }

        for (idx2 = 1; idx2 <= len2; idx2++) { // Loop through longest string, constructing each row of the matrix
            upperLeft = row[0];
            unit2 = s2[idx2 - 1];

            row[0] = idx2 * insertionDeletionCost;

            for (idx1 = 1; idx1 <= len1; idx1++) { // Loop through each cel in the matrix row
                upper = row[idx1];
                unit1 = s1[idx1 - 1];
                cost = replacementCost;

                // Get the replacement cost when the strings are not equal
                if (unit1.equals(unit2)) {
                    cost = 0.0;
                } else if (!(idx1 > 1 && replacementCostTableExceptions.contains(Pair.of(s1[idx1 - 2], unit1)))
                        && !(idx2 > 1 && replacementCostTableExceptions.contains(Pair.of(s2[idx2 - 2], unit2)))) {
                    cost = getReplacementCost(unit1, unit2);
                }

                cost = implication.applyInverse(cost); // Apply the implication to the dissimilarity/cost between strings
                cost = reverseImplication.apply(cost);

                // Get actual lowest edit distance
                row[idx1] = Math.min(
                        Math.min(
                            row[idx1 - 1] + insertionDeletionCost,
                            upper + insertionDeletionCost
                        ), upperLeft + cost
                );

                upperLeft = upper;
            }
        }

        return row[len1];
    }

    /**
     * Add a replacement cost to the replacement cost table for a specific pair of strings.
     *
     * @param pair The {@link UnorderedPair<String>} of strings that should get a specific replacement cost.
     * @param cost The replacement cost between the specific/given pair of strings.
     *
     * @throws IllegalArgumentException is thrown when the cost takes a value smaller than 0 or larger than 1.
     */
    public void addReplacementCost(UnorderedPair<String> pair, double cost) {
        if (cost < 0.0 || cost > 1.0) { // Illegal costs
            throw new IllegalArgumentException("Replacement costs should be in the interval [0.0, 1.0]");
        }

        replacementCostTable.put(pair, cost);
    }

    /**
     * Get the replacement cost from the replacement cost table for a given pair of {@link String}s.
     *
     * @param a A first {@link String} in the pair.
     * @param b The second {@link String} in the pair.
     * @return A {@link Double} value, representing the replacement cost
     */
    protected double getReplacementCost(String a, String b) {
        UnorderedPair<String> p = UnorderedPair.of(a, b);

        return replacementCostTable.getOrDefault(p, replacementCost);
    }

    /**
     * Clear the replacement cost table.
     */
    public void clearReplacementCostTable() {
        this.replacementCostTable.clear();
    }

    /**
     * Check if the replacement cost table already has a cost for a given pair
     *
     * @param pair The {@link UnorderedPair} to check for
     * @return A {@link Boolean} value which is true if for the given {@link UnorderedPair}, a cost is already stored
     * in this object and false if no cost is stored.
     */
    public boolean hasCost(UnorderedPair<String> pair) {
        return replacementCostTable.containsKey(pair);
    }
}
