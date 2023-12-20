package be.ugent.telin.ddcm.similarity;

public interface Similarity {

    double apply(String a, String b);

    double apply(String[] a, String[] b);
}
