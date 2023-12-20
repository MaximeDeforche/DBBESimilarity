package be.ugent.telin.ddcm.dbbe.similarity.task;

import be.ugent.telin.ddcm.similarity.LevenshteinSimilarity;
import be.ugent.telin.ddcm.util.CypherUtils;
import org.neo4j.graphdb.*;

import java.util.concurrent.Callable;

import static be.ugent.telin.ddcm.dbbe.DBBEConstants.*;

public abstract class GenericTextSimilarityTask implements Callable<Long> {

    protected final String statement;
    protected final LevenshteinSimilarity similarity;
    private final GraphDatabaseService service;

    public GenericTextSimilarityTask(GraphDatabaseService service, String statement, LevenshteinSimilarity similarity) {
        this.service = service;
        this.statement = statement;
        this.similarity = similarity;
    }

    @Override
    public Long call() throws Exception {
        return CypherUtils.getTransactionally(service, this::doTransaction);
    }

    public abstract Long doTransaction(Transaction tx);

    protected double getTextSimpleAndCompare(Node n1, Node n2) {
        String s1 = (String) n1.getProperty(TEXT_PROPERTY);
        String s2 = (String) n2.getProperty(TEXT_PROPERTY);
        return similarity.apply(s1, s2);
    }
}
