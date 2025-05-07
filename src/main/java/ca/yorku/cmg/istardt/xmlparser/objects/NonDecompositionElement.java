package ca.yorku.cmg.istardt.xmlparser.objects;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public abstract class NonDecompositionElement extends Element {
    @JacksonXmlProperty(localName = "formula")
    private Formula valueFormula;

    public void setValueFormula(Formula valueFormula) {
        this.valueFormula = valueFormula;
    }

    public Formula getFormula() {
        return valueFormula;
    }
}