package ca.yorku.cmg.istardt.xmlparser.objects;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * NonDecompositionElement class representing an element that cannot be further decomposed.
 * Modified to support Jackson XML unmarshalling.
 */
public abstract class NonDecompositionElement extends Element {
    @JacksonXmlProperty(localName = "formula")
    private Formula valueFormula;

    private Boolean previous;

    public void setValueFormula(Formula valueFormula) {
        this.valueFormula = valueFormula;
    }

    public Formula getFormula() {
        return valueFormula;
    }

    public Boolean getPrevious() {
        return previous;
    }

    public void setPrevious(Boolean previous) {
        this.previous = previous;
    }
}