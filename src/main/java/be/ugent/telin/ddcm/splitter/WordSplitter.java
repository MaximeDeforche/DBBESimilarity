package be.ugent.telin.ddcm.splitter;

public class WordSplitter implements TextSplitter {

    @Override
    public String[] apply(String s) {
        String[] arr = s.split("");

        if (arr.length == 1 && arr[0].equals("")) {
            return new String[] {};
        }

        return arr;
    }
}
