package be.ugent.telin.ddcm.dbbe.similarity;

import be.ugent.telin.ddcm.dbbe.similarity.task.EpigramSimilarityTask;
import be.ugent.telin.ddcm.dbbe.similarity.task.GenericTextSimilarityTask;
import be.ugent.telin.ddcm.dbbe.similarity.task.VerseSimilarityTask;
import be.ugent.telin.ddcm.dbbe.similarity.task.WordSimilarityTask;
import be.ugent.telin.ddcm.dbbe.splitter.GreekWordSplitter;
import be.ugent.telin.ddcm.distance.LevenshteinDamerauEditDistance;
import be.ugent.telin.ddcm.distance.LevenshteinEditDistance;
import be.ugent.telin.ddcm.implication.DefaultImplication;
import be.ugent.telin.ddcm.implication.ResidualImplication;
import be.ugent.telin.ddcm.similarity.LevenshteinSimilarityBuilder;
import be.ugent.telin.ddcm.splitter.SentenceSplitter;
import be.ugent.telin.ddcm.splitter.TextSplitter;
import be.ugent.telin.ddcm.splitter.WordSplitter;
import be.ugent.telin.ddcm.util.CypherUtils;
import be.ugent.telin.ddcm.util.ThreadUtils;
import be.ugent.telin.ddcm.util.TimeUtils;
import be.ugent.telin.ddcm.util.objects.Pair;
import be.ugent.telin.ddcm.util.objects.UnorderedPair;
import org.neo4j.graphdb.*;
import org.neo4j.logging.Log;
import org.neo4j.procedure.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static be.ugent.telin.ddcm.dbbe.similarity.HierarchicalLevenshteinSimilarityResult.IntermediateResult;

//TODO: simply call tasks when parallel is set to false
//TODO: documentation and descriptions
public class HierarchicalLevenshteinSimilarityCypher {

    private static final double WORD_IMPLICATION_THRESHOLD = 1.0;
    private static final double VERSE_IMPLICATION_THRESHOLD = 1.0;

    private static final int WORD_IDS_MAX_TRANSACTION_SIZE = 10;
    private static final int VERSE_IDS_MAX_TRANSACTION_SIZE = 5;

    private static final int EPIGRAM_IDS_MAX_TRANSACTION_SIZE = 5;

    private static final int LOG_TASK_INTERVAL = 10;

    private static final int NUM_CORES = 4;

    @Context
    public GraphDatabaseService service;

    @Context
    public TerminationGuard guard;

    @Context
    public Log logger;

    @Procedure(value="ddcm.dbbe.epigram.similarity.hierarchicalLevenshtein.words", mode=Mode.WRITE)
    @Description("")
    public Stream<IntermediateResult> hierarchicalLevenshteinSimilarityWords(@Name(value = "config", defaultValue = "{}") Map<String, Object> config) {
        String textType = (String) config.getOrDefault("text", "normal");
        int wordAmount = ((Number) config.getOrDefault("wordTransSize", WORD_IDS_MAX_TRANSACTION_SIZE)).intValue();
        double indelCost = (double) config.getOrDefault("wordIndel", LevenshteinEditDistance.DEFAULT_INSERTION_DELETION_COST);
        double replacementCost = (double) config.getOrDefault("wordReplace", LevenshteinEditDistance.DEFAULT_REPLACEMENT_COST);
        boolean damerau = (boolean) config.getOrDefault("damerau", false);
        double transpositionCost = (double) config.getOrDefault("wordTransposition", LevenshteinDamerauEditDistance.DEFAULT_TRANSPOSITION_COST);
        double transpositionThreshold =  (double) config.getOrDefault("wordTranspositionThreshold", LevenshteinDamerauEditDistance.DEFAULT_TRANSPOSITION_MATCH_THRESHOLD);
        TextSplitter wordSplitter = textType.equals("greek") ? new GreekWordSplitter() : new WordSplitter();
        Map<UnorderedPair<String>, Double> replacementCosts = textType.equals("greek") ? GreekWordSplitter.replacementCosts : Collections.emptyMap();
        Set<Pair<String, String>> replacementCostsExceptions = textType.equals("greek") ? GreekWordSplitter.exceptions : Collections.emptySet();

        IntermediateResult result = performSimilarityCalculation("Words", "Word", config, wordAmount, statement ->
                new WordSimilarityTask(service, statement, new LevenshteinSimilarityBuilder()
                        .isDamerau(damerau)
                        .setInsertionDeletionCost(indelCost)
                        .setReplacementCost(replacementCost)
                        .setTranspositionCost(transpositionCost)
                        .setTranspositionMatchThreshold(transpositionThreshold)
                        .setImplication(new DefaultImplication())
                        .setReplacementCosts(replacementCosts)
                        .setReplacementCostTableExceptions(replacementCostsExceptions)
                        .setTextSplitter(wordSplitter)
                        .build()
                )
        );

        return Stream.of(result);
    }

    @Procedure(value="ddcm.dbbe.epigram.similarity.hierarchicalLevenshtein.verses", mode=Mode.WRITE)
    @Description("")
    public Stream<IntermediateResult> hierarchicalLevenshteinSimilarityVerses(@Name(value = "config", defaultValue = "{}") Map<String, Object> config) {
        int verseAmount = ((Number) config.getOrDefault("verseTransSize", VERSE_IDS_MAX_TRANSACTION_SIZE)).intValue();
        double indelCost = (double) config.getOrDefault("verseIndel", LevenshteinEditDistance.DEFAULT_INSERTION_DELETION_COST);
        double replacementCost = (double) config.getOrDefault("verseReplace", LevenshteinEditDistance.DEFAULT_REPLACEMENT_COST);
        double wordSimilarityThreshold = (double) config.getOrDefault("wordSimThreshold", WORD_IMPLICATION_THRESHOLD);
        double reverseWordSimilarityThreshold = (double) config.getOrDefault("reverseWordSimThreshold", WORD_IMPLICATION_THRESHOLD);
        boolean damerau = (boolean) config.getOrDefault("damerau", false);
        double transpositionCost = (double) config.getOrDefault("verseTransposition", LevenshteinDamerauEditDistance.DEFAULT_TRANSPOSITION_COST);
        double transpositionThreshold =  (double) config.getOrDefault("verseTranspositionThreshold", LevenshteinDamerauEditDistance.DEFAULT_TRANSPOSITION_MATCH_THRESHOLD);

        IntermediateResult result = performSimilarityCalculation("Verses", "Verse", config, verseAmount, statement ->
                new VerseSimilarityTask(service, statement, new LevenshteinSimilarityBuilder()
                        .isDamerau(damerau)
                        .setInsertionDeletionCost(indelCost)
                        .setReplacementCost(replacementCost)
                        .setTranspositionCost(transpositionCost)
                        .setTranspositionMatchThreshold(transpositionThreshold)
                        .setImplication(new ResidualImplication(wordSimilarityThreshold))
                        .setTextSplitter(new SentenceSplitter()) // TODO: remove when text is no longer stored in verses
                        .setReverseImplication(new ResidualImplication(reverseWordSimilarityThreshold))
                        .build()
                )
        );

        return Stream.of(result);
    }

    @Procedure(value="ddcm.dbbe.epigram.similarity.hierarchicalLevenshtein.epigrams", mode=Mode.WRITE)
    @Description("")
    public Stream<IntermediateResult> hierarchicalLevenshteinSimilarityEpigrams(@Name(value = "config", defaultValue = "{}") Map<String, Object> config) {
        int epigramAmount = ((Number) config.getOrDefault("epigramTransSize", EPIGRAM_IDS_MAX_TRANSACTION_SIZE)).intValue();
        double indelCost = (double) config.getOrDefault("epigramIndel", LevenshteinEditDistance.DEFAULT_INSERTION_DELETION_COST);
        double replacementCost = (double) config.getOrDefault("epigramReplace", LevenshteinEditDistance.DEFAULT_REPLACEMENT_COST);
        double verseSimilarityThreshold = (double) config.getOrDefault("verseSimThreshold", VERSE_IMPLICATION_THRESHOLD);
        double reverseVerseSimilarityThreshold = (double) config.getOrDefault("reverseVerseSimThreshold", WORD_IMPLICATION_THRESHOLD);
        boolean damerau = (boolean) config.getOrDefault("damerau", false);
        double transpositionCost = (double) config.getOrDefault("epigramTransposition", LevenshteinDamerauEditDistance.DEFAULT_TRANSPOSITION_COST);
        double transpositionThreshold =  (double) config.getOrDefault("epigramTranspositionThreshold", LevenshteinDamerauEditDistance.DEFAULT_TRANSPOSITION_MATCH_THRESHOLD);

        IntermediateResult result = performSimilarityCalculation("Epigrams", "Occurrence", config, epigramAmount, statement ->
                new EpigramSimilarityTask(service, statement, new LevenshteinSimilarityBuilder()
                        .isDamerau(damerau)
                        .setInsertionDeletionCost(indelCost)
                        .setReplacementCost(replacementCost)
                        .setTranspositionCost(transpositionCost)
                        .setTranspositionMatchThreshold(transpositionThreshold)
                        .setImplication(new ResidualImplication(verseSimilarityThreshold))
                        .setReverseImplication(new ResidualImplication(reverseVerseSimilarityThreshold))
                        .build()
                )
        );

        return Stream.of(result);
    }

    @Procedure(value="ddcm.dbbe.epigram.similarity.hierarchicalLevenshtein", mode=Mode.WRITE)
    @Description("")
    public Stream<HierarchicalLevenshteinSimilarityResult> hierarchicalLevenshteinSimilarity(@Name(value = "config", defaultValue = "{}") Map<String, Object> config) {
        boolean doLog = (boolean) config.getOrDefault("log", false);
        long start = System.currentTimeMillis();
        HierarchicalLevenshteinSimilarityResult result = new HierarchicalLevenshteinSimilarityResult();

        if (doLog) logger.info("Starting words");

        IntermediateResult wordsResults = hierarchicalLevenshteinSimilarityWords(config)
                .findFirst()
                .orElse(new IntermediateResult());
        result.addIntermediateResult(wordsResults);
        result.wordSimilarities = wordsResults.computedSimilarities;

        if (doLog) logger.info("Finished words. Took (%s). Total time: (%s).", TimeUtils.getTimeString(wordsResults.runtime), TimeUtils.getTimeString(System.currentTimeMillis() - start));

        if (CypherUtils.isQueryTerminated(guard)) {
            result.wasTerminated = true;
            if (doLog) logger.info("Similarity computations are terminated");
            return Stream.of(result);
        }

        if (doLog) logger.info("Starting verses");

        IntermediateResult versesResults = hierarchicalLevenshteinSimilarityVerses(config)
                .findFirst()
                .orElse(new IntermediateResult());
        result.addIntermediateResult(versesResults);
        result.verseSimilarities = versesResults.computedSimilarities;

        if (doLog) logger.info("Finished verses. Took (%s). Total time: (%s).", TimeUtils.getTimeString(versesResults.runtime), TimeUtils.getTimeString(System.currentTimeMillis() - start));

        if (CypherUtils.isQueryTerminated(guard)) {
            result.wasTerminated = true;
            if (doLog) logger.info("Similarity computations are terminated");
            return Stream.of(result);
        }

        if (doLog) logger.info("Starting epigrams");

        IntermediateResult epigramResults = hierarchicalLevenshteinSimilarityEpigrams(config)
                .findFirst()
                .orElse(new IntermediateResult());
        result.addIntermediateResult(epigramResults);
        result.epigramSimilarities = epigramResults.computedSimilarities;

        if (doLog) logger.info("Finished epigrams. Took (%s). Total time: (%s).", TimeUtils.getTimeString(epigramResults.runtime), TimeUtils.getTimeString(System.currentTimeMillis() - start));

        result.runtime = System.currentTimeMillis() - start;

        return Stream.of(result);
    }

    private IntermediateResult performSimilarityCalculation(String type, String label, Map<String, Object> config, int amount, Function<String, GenericTextSimilarityTask> task) {
        int poolSize = Math.min(((Number) config.getOrDefault("cores", NUM_CORES)).intValue(), Runtime.getRuntime().availableProcessors());
        boolean doLog = (boolean) config.getOrDefault("log", false);
        int logInterval = ((Number) config.getOrDefault("logInterval", LOG_TASK_INTERVAL)).intValue();
        boolean parallel = (boolean) config.getOrDefault("parallel", true);

        int submissionCount = 0;
        int finishCount = 0;
        boolean terminationHandled = false;
        long start = System.currentTimeMillis();
        IntermediateResult result = new IntermediateResult();

        List<Long> ids = CypherUtils.getTransactionally(service, tx -> {
            Result res = tx.execute(String.format("MATCH (n:%s) RETURN collect(ID(n)) AS ids", label));
            List<Long> l = (List<Long>) res.next().get("ids");
            res.close();

            return l;
        });

        long computationCount = getTotalComputations(ids.size());
        result.transactions++;
        List<String> statements = getIDStrings(ids, amount);

        poolSize = parallel ? Math.min(statements.size(), poolSize) : 1;
        ExecutorService executor = ThreadUtils.getNameThreadExecutor(poolSize, String.format("dbbe_%s", type.toLowerCase()));
        CompletionService<Long> compService = new ExecutorCompletionService<>(executor);

        while (submissionCount < poolSize && submissionCount < statements.size()) {
            compService.submit(task.apply(statements.get(submissionCount)));
            submissionCount++;
        }

        try {
            while (finishCount < submissionCount){
                Future<Long> f = compService.take();
                finishCount++;

                if (!result.wasTerminated && submissionCount < statements.size()) {
                    compService.submit(task.apply(statements.get(submissionCount)));
                    submissionCount++;
                }

                result.transactions++;
                result.computedSimilarities += f.get();

                if (doLog && (result.transactions - 2) % logInterval == 0) {
                    logDebug(type, result.computedSimilarities, computationCount, System.currentTimeMillis() - start);
                }

                if (result.wasTerminated && !terminationHandled) {
                    if (doLog) logger.info(String.format("%s similarity computations are terminated", type));
                    terminationHandled = true;
                    executor.shutdown();
                }

                result.wasTerminated = CypherUtils.isQueryTerminated(guard);
            }

            if (result.wasTerminated && terminationHandled && !executor.awaitTermination(60, TimeUnit.SECONDS)) {
                logger.error("Failed to terminate some threads");
            } else {
                executor.shutdown();
            }
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getMessage());
        }

        result.runtime = System.currentTimeMillis() - start;

        if (doLog) logDebug(type, result.computedSimilarities, computationCount, result.runtime);

        return result;
    }

    private long getTotalComputations(long amount) {
        return ((amount - 1) * (amount - 1) + (amount - 1)) / 2;
    }

    private void logDebug(String type, long value, long max, long time) {
        logger.info(type + ": " + value + "/" + max + " " + Math.round(((double) value / (double) max) * 10000.0) / 100.0 + "% (" + TimeUtils.getTimeString(time) + ")");
    }

    private List<String> getIDStrings(List<Long> ids, int maxSize) {
        int idsIndex = 0;
        StringBuilder builder;
        List<String> res = new ArrayList<>();

        while (idsIndex < ids.size()) {
            builder = new StringBuilder("[");

            do {
                builder.append(ids.get(idsIndex).toString());
                builder.append(",");
                idsIndex++;
            } while (idsIndex % maxSize != 0 && idsIndex < ids.size());

            builder.deleteCharAt(builder.length() - 1);
            builder.append("]");
            res.add(builder.toString());
        }

        return res;
    }
}
