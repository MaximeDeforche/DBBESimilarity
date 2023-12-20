package be.ugent.telin.ddcm.dbbe.transformer;

import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;

public class CypherTransformations {

    private static final SpecialCharacterTextTransformer specialCharacterTransformer = new SpecialCharacterTextTransformer();
    private static final UncertaintyTextTransformer uncertaintyTransformer = new UncertaintyTextTransformer();
    private static final GreekAccentTextTransformer greekAccentTransformer = new GreekAccentTextTransformer();
    private static final ItacismTransformer itacismTransformer = new ItacismTransformer();

    @UserFunction("ddcm.dbbe.transform.removeSpecialChars")
    @Description("")
    public String removeSpecialChars(@Name("text") String s) {
        return specialCharacterTransformer.apply(s);
    }

    @UserFunction("ddcm.dbbe.transform.removeUncertainty")
    @Description("")
    public String removeUncertainty(@Name("text") String s) {
        return uncertaintyTransformer.apply(s);
    }

    @UserFunction("ddcm.dbbe.transform.removeGreekAccents")
    @Description("")
    public String removeGreekAccents(@Name("text") String s) {
        return greekAccentTransformer.apply(s);
    }

    @UserFunction("ddcm.dbbe.transform.removeItacism")
    @Description("")
    public String removeItacism(@Name("text") String s) {
        return itacismTransformer.apply(s);
    }
}
