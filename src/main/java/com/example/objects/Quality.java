package com.example.objects;

/**
 * Implementation of the Quality class
 * A Quality represents a non-decomposition element that describes a measurable property
 */
public class Quality extends NonDecompositionElement {
    private boolean isRoot = false;

    /**
     * Check if this quality is a root quality
     * @return True if this is a root quality, false otherwise
     */
    public boolean isRoot() {
        return isRoot;
    }

    /**
     * Set whether this quality is a root quality
     * @param isRoot True if this is a root quality, false otherwise
     */
    public void setRoot(boolean isRoot) {
        this.isRoot = isRoot;
    }

    @Override
    public Formula getFormula() {
        return super.getFormula();
    }
}