package be.ugent.telin.ddcm.dbbe;

import org.neo4j.graphdb.RelationshipType;

public final class DBBEConstants {

    public static final String TEXT_PROPERTY = "text";
    public static final String SIMILARITY_PROPERTY = "similarity";

    public static final RelationshipType IS_SIMILAR_TO_TYPE = RelationshipType.withName("IS_SIMILAR_TO");

}
