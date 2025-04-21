package com.example.objects;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * NonDecompositionElement class representing an element that cannot be further decomposed.
 * Modified to support Jackson XML unmarshalling.
 */
public abstract class NonDecompositionElement extends Element {
    @JacksonXmlProperty(localName = "formula")
    private Formula valueFormula;

    private Atom atom;
    private Boolean previous;

    public Formula getValueFormula() {
        return valueFormula;
    }

    public void setValueFormula(Formula valueFormula) {
        this.valueFormula = valueFormula;
    }

    public Formula getFormula() {
        return valueFormula;
    }

    public void setAtom(Atom atom) {
        this.atom = atom;
    }

    @Override
    public Atom getAtom() {
        return atom != null ? atom : super.getAtom();
    }

    public Boolean getPrevious() {
        return previous;
    }

    public void setPrevious(Boolean previous) {
        this.previous = previous;
    }
}