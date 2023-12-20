package be.ugent.telin.ddcm.distance;

import be.ugent.telin.ddcm.implication.Implication;
import be.ugent.telin.ddcm.util.StringUtils;
import be.ugent.telin.ddcm.util.objects.Pair;
import be.ugent.telin.ddcm.util.objects.UnorderedPair;

import java.util.Map;
import java.util.Set;

public class LevenshteinDamerauEditDistance extends LevenshteinEditDistance {

    public static final double DEFAULT_TRANSPOSITION_COST = 1.0;
    public static final double DEFAULT_TRANSPOSITION_MATCH_THRESHOLD = 1.0;

    private final double transpositionCost;
    private final double transpositionMatchThreshold;

    public LevenshteinDamerauEditDistance(double insertionDeletionCost, double replacementCost, Implication implication
            , Map<UnorderedPair<String>, Double> replacementCostTable, Set<Pair<String, String>> replacementCotsTableExceptions
            , double transpositionCost, Implication reverseImplication, double transpositionMatchThreshold) {
        super(insertionDeletionCost, replacementCost, implication, replacementCostTable, replacementCotsTableExceptions
            , reverseImplication);

        if (transpositionCost < 0.0 || transpositionCost > 1.0 || transpositionMatchThreshold < 0.0 || transpositionMatchThreshold > 1.0) { // Illegal costs
            throw new IllegalArgumentException("Transposition cost should be in the interval [0.0, 1.0]");
        }

        this.transpositionCost = transpositionCost;
        this.transpositionMatchThreshold = transpositionMatchThreshold;
    }

    @Override
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
        double[] prevRow = new double[len1 + 1];
        double[] row = new double[len1 + 1];

        // Loop variables
        int idx1;
        int idx2;
        double upper;     // The value to the upper cel of the current location in the previous row.
        double upperLeft; // The value to the upper left cel of the current location in the previous row.
        double upperLeftLeft; // The value to the left of the upper left cel of the current location in the previous row.
        double cost;
        String unit1; // Textual units
        String unit2;
        boolean exception1;
        boolean exception2;

        for (idx1 = 0; idx1 <= len1; idx1++) {
            row[idx1] = idx1 * insertionDeletionCost;
            prevRow[idx1] = 0;
        }

        for (idx2 = 1; idx2 <= len2; idx2++) { // Loop through longest string, constructing each row of the matrix
            upperLeftLeft = -1;
            upperLeft = prevRow[0];
            unit2 = s2[idx2 - 1];
            prevRow[0] = row[0];

            row[0] = idx2 * insertionDeletionCost;

            for (idx1 = 1; idx1 <= len1; idx1++) { // Loop through each cel in the matrix row
                upper = prevRow[idx1];
                prevRow[idx1] = row[idx1];
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
                                prevRow[idx1] + insertionDeletionCost
                        ), prevRow[idx1 - 1] + cost
                );

                exception1 = (idx2 > 2 && replacementCostTableExceptions.contains(Pair.of(s2[idx2 - 3], s2[idx2 - 2])));
                exception2 = (idx1 > 2 && replacementCostTableExceptions.contains(Pair.of(s1[idx1 - 3], s1[idx1 - 2])));

                //Damerau
                if (idx1 > 1 && idx2 > 1
                        && (unit1.equals(s2[idx2 - 2]) || (!exception1 && Math.abs(getReplacementCost(unit1, s2[idx2 - 2])) < ((1.0 - transpositionMatchThreshold) + 0.0001)))
                        && (unit2.equals(s1[idx1 - 2]) || (!exception2 && Math.abs(getReplacementCost(unit2, s1[idx1 - 2 ])) < ((1.0 - transpositionMatchThreshold) + 0.0001)))) {
                    row[idx1] = Math.min(row[idx1], upperLeftLeft + transpositionCost);
                }

                upperLeftLeft = upperLeft;
                upperLeft = upper;
            }
        }

        return row[len1];
    }
}
