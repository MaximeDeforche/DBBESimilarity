package be.ugent.telin.ddcm.transformer;

import be.ugent.telin.ddcm.util.objects.Pair;

import java.util.List;

public class SingleSpaceTextTransformer extends AbstractRegexTextTransformer {

    @Override
    protected void fillRegexReplacements(List<Pair<String, String>> regexReplacements) {
        regexReplacements.add(Pair.of("\\s+", " "));
    }
}
