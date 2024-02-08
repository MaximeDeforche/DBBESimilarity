package be.ugent.telin.ddcm.dbbe.transformer;

import be.ugent.telin.ddcm.transformer.AbstractRegexTextTransformer;
import be.ugent.telin.ddcm.util.objects.Pair;

import java.util.List;

public class ItacismTransformer extends AbstractRegexTextTransformer
{
    @Override
    protected void fillRegexReplacements(List<Pair<String, String>> regexReplacements) {
        regexReplacements.add(Pair.of("η", "ι"));
        regexReplacements.add(Pair.of("ει", "ι"));
        regexReplacements.add(Pair.of("οι", "ι"));
        regexReplacements.add(Pair.of("([^αοε])υ", "$1ι"));
        regexReplacements.add(Pair.of("ῃ", "ι"));
    }
}
