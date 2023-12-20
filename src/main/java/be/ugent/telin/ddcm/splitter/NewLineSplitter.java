package be.ugent.telin.ddcm.splitter;

public class NewLineSplitter implements TextSplitter {
    @Override
    public String[] apply(String s) {
        String[] arr =  s.split("\\n");

        if (arr.length == 1 && arr[0].equals("")) {
            return new String[] {};
        }

        return arr;
    }
}
