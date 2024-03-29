package be.ugent.telin.ddcm.dbbe.similarity.task;

import be.ugent.telin.ddcm.similarity.LevenshteinSimilarity;
import be.ugent.telin.ddcm.util.objects.UnorderedPair;
import org.neo4j.graphdb.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static be.ugent.telin.ddcm.dbbe.DBBEConstants.*;

public class EpigramSimilarityTask extends GenericTextSimilarityTask {

    public EpigramSimilarityTask(GraphDatabaseService service, String statement, LevenshteinSimilarity similarity) {
        super(service, statement, similarity);
    }

    @Override
    public Long doTransaction(Transaction tx) {
        Map<String, Object> row;
        UnorderedPair<String> versePair;
        List<Relationship> verseRels;
        ArrayList<String> verseList1;
        ArrayList<String> verseList2;
        String[] verses1;
        String[] verses2;
        Node n1;
        Node n2;
        long sims = 0;
        double sim;
        Relationship newRel;

        Result res = tx.execute("MATCH (n1:Occurrence) " +
                "WHERE ID(n1) IN" + statement + " " +
                "WITH n1, COLLECT { WITH n1 as occ MATCH (occ)<-[r:IN_OCCURRENCE]-(v) RETURN v.text ORDER BY r.rank } as verses1 " +
                "MATCH (n2:Occurrence) " +
                "WHERE ID(n1) < ID(n2) " +
                "WITH n1, verses1, n2, COLLECT { WITH n2 as occ MATCH (occ)<-[r:IN_OCCURRENCE]-(v) RETURN v.text ORDER BY r.rank } as verses2 " +
                "MATCH (n1)<-[r1:IN_OCCURRENCE]-(v1) " +
                "MATCH (v2)-[r2:IN_OCCURRENCE]->(n2) " +
                "OPTIONAL MATCH (v1)-[r:IS_SIMILAR_TO]-(v2) " +
                "RETURN n1, n2, verses1, verses2, collect(r) as rels"
        );

        while (res.hasNext()) {
            row = res.next();
            n1 = (Node) row.get("n1");
            n2 = (Node) row.get("n2");
            verseRels = (ArrayList<Relationship>) row.get("rels");
            verseList1 = (ArrayList<String>) row.get("verses1");
            verseList2 = (ArrayList<String>) row.get("verses2");
            verses1 = verseList1.toArray(new String[0]);
            verses2 = verseList2.toArray(new String[0]);

            for (Relationship r : verseRels) {
                versePair = UnorderedPair.of((String) r.getEndNode().getProperty(TEXT_PROPERTY), (String) r.getStartNode().getProperty(TEXT_PROPERTY));
                similarity.addReplacementCost(versePair, 1.0 - (Double) r.getProperty(SIMILARITY_PROPERTY));
            }

            sim = similarity.apply(verses1, verses2);

            newRel = n1.createRelationshipTo(n2, IS_SIMILAR_TO_TYPE);
            newRel.setProperty(SIMILARITY_PROPERTY, sim);

            sims += 1;
        }

        res.close();

        return sims;
    }
}
