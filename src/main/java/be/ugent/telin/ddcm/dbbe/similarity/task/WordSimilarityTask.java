package be.ugent.telin.ddcm.dbbe.similarity.task;

import be.ugent.telin.ddcm.similarity.LevenshteinSimilarity;
import org.neo4j.graphdb.*;

import java.util.Map;

import static be.ugent.telin.ddcm.dbbe.DBBEConstants.*;

public class WordSimilarityTask extends GenericTextSimilarityTask {

    public WordSimilarityTask(GraphDatabaseService service, String statement, LevenshteinSimilarity similarity) {
        super(service, statement, similarity);
    }

    @Override
    public Long doTransaction(Transaction tx) {
        Map<String, Object> row;
        Node n1;
        Node n2;
        double sim;
        long sims = 0;
        Relationship newRel;

        Result res = tx.execute("MATCH (n1:Word), (n2:Word)" +
                                    "WHERE ID(n1) IN " + statement + " AND ID(n1) < ID(n2)" +
                                    "RETURN n1, n2");

        while (res.hasNext()) {
            row = res.next();
            n1 = (Node) row.get("n1");
            n2 = (Node) row.get("n2");

            sim = getTextSimpleAndCompare(n1, n2);
            newRel = n1.createRelationshipTo(n2, IS_SIMILAR_TO_TYPE);
            newRel.setProperty(SIMILARITY_PROPERTY, sim);

            sims += 1;
        }

        res.close();

        return sims;
    }
}
