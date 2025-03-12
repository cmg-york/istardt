package com.example.objects;

/**
 * Extensions to the NonDecompositionElement class to add functionality needed for XML mapping
 */
public abstract class NonDecompositionElement extends Element {
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