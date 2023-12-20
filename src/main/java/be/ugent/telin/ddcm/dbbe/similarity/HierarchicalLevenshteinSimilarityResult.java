package be.ugent.telin.ddcm.dbbe.similarity;

public class HierarchicalLevenshteinSimilarityResult {

    public long wordSimilarities;
    public long verseSimilarities;
    public long epigramSimilarities;
    public long transactions;
    public long updates;
    public long runtime;
    public boolean wasTerminated;

    public HierarchicalLevenshteinSimilarityResult(long wordSimilarities, long verseSimilarities, long epigramSimilarities, long transactions, long updates, long runtime, boolean wasTerminated) {
        this.wordSimilarities = wordSimilarities;
        this.verseSimilarities = verseSimilarities;
        this.epigramSimilarities = epigramSimilarities;
        this.transactions = transactions;
        this.updates = updates;
        this.runtime = runtime;
        this.wasTerminated = wasTerminated;
    }

    public HierarchicalLevenshteinSimilarityResult() {
        this(0, 0, 0, 0, 0, 0, false);
    }

    public void addIntermediateResult(IntermediateResult intermediateResult) {
        this.transactions += intermediateResult.transactions;
        this.updates += intermediateResult.computedSimilarities;
        this.wasTerminated |= intermediateResult.wasTerminated;
    }

    public static class IntermediateResult {
        public long computedSimilarities;
        public long transactions;
        public long runtime;
        public boolean wasTerminated;

        public IntermediateResult(long computedSimilarities, long transactions, long runtime, boolean wasTerminated) {
            this.computedSimilarities = computedSimilarities;
            this.transactions = transactions;
            this.runtime = runtime;
            this.wasTerminated = wasTerminated;
        }

        public IntermediateResult() {
            this(0, 0, 0, false);
        }
    }
}
