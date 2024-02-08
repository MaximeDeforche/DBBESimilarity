package be.ugent.telin.ddcm.dbbe.transformer;

import be.ugent.telin.ddcm.transformer.AbstractRegexTextTransformer;
import be.ugent.telin.ddcm.util.objects.Pair;

import java.util.List;

public class GreekAccentTextTransformer extends AbstractRegexTextTransformer {

    @Override
    protected void fillRegexReplacements(List<Pair<String, String>> regexReplacements) {
        regexReplacements.add(Pair.of("ώ|ὠ|ὡ|ὢ|ὣ|ὤ|ὥ|ὦ|ὧ|ὼ|ώ", "ω"));
        regexReplacements.add(Pair.of("ᾠ|ᾡ|ᾢ|ᾣ|ᾤ|ᾥ|ᾦ|ᾧ|ῲ|ῴ|ῶ|ῷ|ῷ", "ῳ"));
        regexReplacements.add(Pair.of("έ|ἐ|ἑ|ἒ|ἓ|ἔ|ἕ|ὲ|έ|ε̃", "ε"));
        regexReplacements.add(Pair.of("ά|ἀ|ἁ|ἂ|ἃ|ἄ|ἅ|ἆ|ἇ|ά|ᾶ|ὰ", "α"));
        regexReplacements.add(Pair.of("ά|ἀ|ἁ|ἂ|ἃ|ἄ|ἅ|ἆ|ἇ|ά|ᾶ|ὰ", "α"));
        regexReplacements.add(Pair.of("ΰ|ϋ|ύ|ὐ|ὑ|ὒ|ὓ|ὔ|ὕ|ὖ|ὗ|ὺ|ύ|ῠ|ῡ|ῢ|ΰ|ῦ|ῧ", "υ"));
        regexReplacements.add(Pair.of("ή|ἠ|ἡ|ἢ|ἣ|ἤ|ἥ|ἦ|ἧ|ὴ|ή|ῆ", "η"));
        regexReplacements.add(Pair.of("ᾐ|ᾑ|ᾒ|ᾓ|ᾔ|ᾕ|ᾖ|ᾗ|ῂ|ῃ|ῄ|ῇ", "ῃ"));
        regexReplacements.add(Pair.of("ί|ΐ|ἰ|ἱ|ἲ|ἳ|ἴ|ἵ|ἶ|ἷ|ὶ|ί|ῐ|ῑ|ῒ|ΐ|ῖ|ῗ|ϊ", "ι"));
        regexReplacements.add(Pair.of("ό|ὀ|ὁ|ὂ|ὃ|ὄ|ὅ|ὸ|ό", "ο"));
        regexReplacements.add(Pair.of("ῤ|ῥ", "ρ"));
    }
}
