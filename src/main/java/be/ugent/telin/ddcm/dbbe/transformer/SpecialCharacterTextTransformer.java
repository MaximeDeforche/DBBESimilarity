package be.ugent.telin.ddcm.dbbe.transformer;

import be.ugent.telin.ddcm.transformer.AbstractRegexTextTransformer;
import be.ugent.telin.ddcm.util.objects.Pair;

import java.util.List;

public class SpecialCharacterTextTransformer extends AbstractRegexTextTransformer {

    @Override
    protected void fillRegexReplacements(List<Pair<String, String>> regexReplacements) {
        //regexReplacements.add(Pair.of("[+|:\\--··`'’ǁ‧⁘÷‖.–]", ""));
        regexReplacements.add(Pair.of("\\[|\\]|\\+|\\||:|-|-|·|·|`|'|’|ǁ|‧|⁘|÷|‖|<|>|–|~|⁜|༶|˙|᾽|…|\\\\|;|†|=|΄|´|※|\\{|\\}|‒|∙|ʹ|∙", ""));
    }
}
