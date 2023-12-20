package be.ugent.telin.ddcm.dbbe.splitter;

import be.ugent.telin.ddcm.splitter.TextSplitter;
import be.ugent.telin.ddcm.util.objects.Pair;
import be.ugent.telin.ddcm.util.objects.UnorderedPair;

import java.util.*;

public class GreekWordSplitter implements TextSplitter {

    private static final Map<Character, Character> combinations;
    public static final Map<UnorderedPair<String>, Double> replacementCosts;
    public static final Set<Pair<String, String>> exceptions;

    static {
        combinations = new HashMap<>();

        combinations.put('ε', 'ι');
        combinations.put('Ε', 'ι');

        combinations.put('ο', 'ι');
        combinations.put('Ο', 'ι');

        replacementCosts = new HashMap<>();

        //Iotacisme
        replacementCosts.put(UnorderedPair.of("ι", "η"), 0.0);
        replacementCosts.put(UnorderedPair.of("ι", "υ"), 0.0);
        replacementCosts.put(UnorderedPair.of("ι", "ει"), 0.0);
        replacementCosts.put(UnorderedPair.of("ι", "οι"), 0.0);
        replacementCosts.put(UnorderedPair.of("ι", "ῃ"), 0.0);
        replacementCosts.put(UnorderedPair.of("η", "υ"), 0.0);
        replacementCosts.put(UnorderedPair.of("η", "ει"), 0.0);
        replacementCosts.put(UnorderedPair.of("η", "οι"), 0.0);
        replacementCosts.put(UnorderedPair.of("η", "ῃ"), 0.0);
        replacementCosts.put(UnorderedPair.of("υ", "ει"), 0.0);
        replacementCosts.put(UnorderedPair.of("υ", "οι"), 0.0);
        replacementCosts.put(UnorderedPair.of("υ", "ῃ"), 0.0);
        replacementCosts.put(UnorderedPair.of("ει", "οι"), 0.0);
        replacementCosts.put(UnorderedPair.of("ει", "ῃ"), 0.0);
        replacementCosts.put(UnorderedPair.of("οι", "ῃ"), 0.0);

        exceptions = new HashSet<>();

        exceptions.add(Pair.of("υ", "ο"));
        exceptions.add(Pair.of("α", "ο"));
        exceptions.add(Pair.of("ε", "ο"));
    }

    @Override
    public String[] apply(String s) {
        List<String> res = new ArrayList<>();

        char[] chars = s.toCharArray();
        int i = 0;

        while (i < chars.length) {
            if (combinations.containsKey(chars[i]) && i + 1 < chars.length && combinations.get(chars[i]).equals(chars[i + 1])) {
                res.add("" + chars[i] + chars[i + 1]);
                i++;
            } else {
                res.add("" + chars[i]);
            }

            i++;
        }

        return res.toArray(new String[0]);
    }
}
