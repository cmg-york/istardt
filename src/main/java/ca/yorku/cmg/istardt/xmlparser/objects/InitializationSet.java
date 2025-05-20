package ca.yorku.cmg.istardt.xmlparser.objects;

import java.util.ArrayList;
import java.util.List;

public class InitializationSet {
    List<Initialization> initializations = new ArrayList<>();

    public List<Initialization> getInitializations() {
        return initializations;
    }

    public void addInitialization(Initialization initialization) {
        initializations.add(initialization);
    }
    public void setInitializations(List<Initialization> initializations) {
        this.initializations = initializations;
    }
}
