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
        ArrayList<Long> ranks1;
        ArrayList<Long> ranks2;
        ArrayList<String> verseList1;
        ArrayList<String> verseList2;
        String[] verses1;
        String[] verses2;
        Node n1;
        Node n2;
        long sims = 0;
        double sim;
        Relationship newRel;

        Result res = tx.execute("MATCH (n1:Occurrence), (n2:Occurrence)" +
                "WHERE ID(n1) IN " + statement + " AND ID(n1) < ID(n2)" +
                "MATCH (n1)<-[r1:IN_OCCURRENCE]-(v1:Verse)" +
                "MATCH (v2:Verse)-[r2:IN_OCCURRENCE]->(n2)" +
                "OPTIONAL MATCH (v1)-[r:IS_SIMILAR_TO]-(v2)" +
                "RETURN n1, n2, collect(r) as rels, collect(distinct r1.rank) as ranks1, collect(distinct v1.text) as verses1, collect(distinct r2.rank) as ranks2, collect(distinct v2.text) as verses2"
        );

        while (res.hasNext()) {
            row = res.next();
            n1 = (Node) row.get("n1");
            n2 = (Node) row.get("n2");
            verseRels = (ArrayList<Relationship>) row.get("rels");
            ranks1 = (ArrayList<Long>) row.get("ranks1");
            ranks2 = (ArrayList<Long>) row.get("ranks2");
            verseList1 = (ArrayList<String>) row.get("verses1");
            verseList2 = (ArrayList<String>) row.get("verses2");
            verses1 = new String[ranks1.size()];
            verses2 = new String[ranks2.size()];

            for (Relationship r : verseRels) {
                versePair = UnorderedPair.of((String) r.getEndNode().getProperty(TEXT_PROPERTY), (String) r.getStartNode().getProperty(TEXT_PROPERTY));
                similarity.addReplacementCost(versePair, 1.0 - (Double) r.getProperty(SIMILARITY_PROPERTY));
            }

            for (int i = 0; i < ranks1.size(); i++) {
                verses1[((Number) ranks1.get(i)).intValue()] = verseList1.get(i);
            }

            for (int i = 0; i < ranks2.size(); i++) {
                verses2[((Number) ranks2.get(i)).intValue()] = verseList2.get(i);
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
