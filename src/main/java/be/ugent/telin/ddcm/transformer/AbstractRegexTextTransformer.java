package be.ugent.telin.ddcm.transformer;

import be.ugent.telin.ddcm.util.objects.Pair;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRegexTextTransformer implements TextTransformer {

    private final List<Pair<String, String>> regexReplacements;

    public AbstractRegexTextTransformer() {
        this.regexReplacements = new ArrayList<>();
        fillRegexReplacements(regexReplacements);
    }

    @Override
    public String apply(String s) {
        String res = s;

        for (Pair<String, String> p : regexReplacements) {
            res = res.replaceAll(p.getFirst(), p.getSecond());
        }

        return res;
    }

    protected abstract void fillRegexReplacements(List<Pair<String, String>> regexReplacements);



}
