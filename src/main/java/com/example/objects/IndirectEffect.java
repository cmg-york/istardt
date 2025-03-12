package com.example.objects;

/**
 * Implementation of the IndirectEffect class
 * An IndirectEffect represents a non-decomposition element that describes
 * an outcome that is not directly caused by a task
 */
public class IndirectEffect extends NonDecompositionElement {
    private boolean exported = false;

    /**
     * Default constructor
     */
    public IndirectEffect() {
        super();
    }

    /**
     * Constructor with ID and description
     * @param id The unique identifier for this indirect effect
     * @param description A human-readable description of this indirect effect
     */
    public IndirectEffect(String id, String description) {
        super();
        this.setId(id);

        Atom atom = new Atom();
        atom.setId(id);
        atom.setTitleText(description);
        this.setAtom(atom);
    }

    /**
     * Check if this indirect effect is exported (visible to other actors)
     * @return True if this indirect effect is exported, false otherwise
     */
    public boolean isExported() {
        return exported;
    }

    /**
     * Set whether this indirect effect is exported (visible to other actors)
     * @param exported True if this indirect effect is exported, false otherwise
     */
    public void setExported(boolean exported) {
        this.exported = exported;
    }

    @Override
    public Formula getFormula() {
        return super.getFormula();
    }
}