package com.example.objects;

/**
 * Implementation of the Condition class
 * A Condition represents a non-decomposition element that describes a state or constraint
 * In the XML schema, Conditions correspond to PreBox elements
 */
public class Condition extends NonDecompositionElement {

    /**
     * Default constructor
     */
    public Condition() {
        super();
    }

    /**
     * Constructor with ID and description
     * @param id The unique identifier for this condition
     * @param description A human-readable description of this condition
     */
    public Condition(String id, String description) {
        super();
        this.setId(id);

        Atom atom = new Atom();
        atom.setId(id);
        atom.setTitleText(description);
        this.setAtom(atom);
    }

    @Override
    public Formula getFormula() {
        return super.getFormula();
    }
}