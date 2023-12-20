package be.ugent.telin.ddcm.dbbe.similarity.task;

import be.ugent.telin.ddcm.similarity.LevenshteinSimilarity;
import be.ugent.telin.ddcm.util.objects.UnorderedPair;
import org.neo4j.graphdb.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static be.ugent.telin.ddcm.dbbe.DBBEConstants.*;

public class VerseSimilarityTask extends GenericTextSimilarityTask {

    public VerseSimilarityTask(GraphDatabaseService service, String statement, LevenshteinSimilarity similarity) {
        super(service, statement, similarity);
    }

    @Override
    public Long doTransaction(Transaction tx) {
        Map<String, Object> row;
        UnorderedPair<String> wordPair;
        List<Relationship> wordRels;
        Node n1;
        Node n2;
        double sim;
        long sims = 0;
        Relationship newRel;

        Result res = tx.execute("MATCH (n1:Verse)" +
                "WHERE ID(n1) IN " + statement +
                "MATCH (n2:Verse)" +
                "WHERE ID(n1) < ID(n2)" +
                "MATCH (n1:Verse)<-[:IN_VERSE]-(w1:Word)-[r:IS_SIMILAR_TO]-(w2:Word)-[:IN_VERSE]->(n2:Verse)" +
                "RETURN n1, n2, collect(r) as rels"
        );

        while (res.hasNext()) {
            row = res.next();
            n1 = (Node) row.get("n1");
            n2 = (Node) row.get("n2");
            wordRels = (ArrayList<Relationship>) row.get("rels");

            for (Relationship r : wordRels) {
                wordPair = UnorderedPair.of((String) r.getEndNode().getProperty(TEXT_PROPERTY), (String) r.getStartNode().getProperty(TEXT_PROPERTY));
                similarity.addReplacementCost(wordPair, 1.0 - (Double) r.getProperty(SIMILARITY_PROPERTY));
            }

            sim = getTextSimpleAndCompare(n1, n2);
            newRel = n1.createRelationshipTo(n2, IS_SIMILAR_TO_TYPE);
            newRel.setProperty(SIMILARITY_PROPERTY, sim);

            sims += 1;
        }

        res.close();

        return sims;
    }
}
