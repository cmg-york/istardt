package ca.yorku.cmg.istardt.xmlparser.objects;

import ca.yorku.cmg.istardt.xmlparser.xml.deserializers.QualityDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = QualityDeserializer.class)
public class Quality extends NonDecompositionElement {
    private boolean root = false;
    public boolean isRoot() {
        return root;
    }
    public void setRoot(boolean isRoot) {
        this.root = isRoot;
    }
    @Override
    public Formula getFormula() {
        return super.getFormula();
    }
}