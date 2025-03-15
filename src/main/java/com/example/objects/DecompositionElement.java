package com.example.objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * DecompositionElement class representing an element that can be decomposed into child elements.
 * Modified to support Jackson XML unmarshalling and prevent circular reference issues.
 */
public class DecompositionElement extends Element {

    private List<DecompositionElement> children;

    @JacksonXmlProperty(isAttribute = true)
    private DecompType decompType;

    private Formula preFormula;
    private Formula nprFormula;

    @JsonIgnore
    private DecompositionElement parent;

    public DecompositionElement() {
        this.children = new ArrayList<>();
    }

    public List<DecompositionElement> getChildren() {
        return children;
    }

    public void setChildren(List<DecompositionElement> children) {
        this.children = children;
    }

    public DecompType getDecompType() {
        return decompType;
    }

    public void setDecompType(DecompType decompType) {
        this.decompType = decompType;
    }

    public Formula getPreFormula() {
        return preFormula;
    }

    public void setPreFormula(Formula preFormula) {
        this.preFormula = preFormula;
    }

    public Formula getNprFormula() {
        return nprFormula;
    }

    public void setNprFormula(Formula nprFormula) {
        this.nprFormula = nprFormula;
    }

    public DecompositionElement getParent() {
        return parent;
    }

    public void setParent(DecompositionElement parent) {
        this.parent = parent;
    }

    /**
     * Get the siblings of this element.
     *
     * @return The list of sibling elements
     */
    public List<DecompositionElement> getSiblings() {
        if (parent == null) {
            return new ArrayList<>();
        }

        List<DecompositionElement> siblings = new ArrayList<>(parent.children);
        siblings.remove(this);
        return siblings;
    }

    /**
     * Check if this element has siblings.
     *
     * @return True if this element has siblings, false otherwise
     */
    public boolean isSiblings() {
        return !getSiblings().isEmpty();
    }

    /**
     * Check if this element is a root element.
     *
     * @return True if this element is a root element, false otherwise
     */
    public boolean isRoot() {
        return parent == null;
    }

    /**
     * Add a child element using AND decomposition.
     *
     * @param child The child element to add
     */
    public void addANDChild(DecompositionElement child) {
        if (decompType == null || decompType == DecompType.AND) {
            decompType = DecompType.AND;
            children.add(child);
            child.setParent(this);
        } else {
            throw new IllegalStateException("Cannot add AND child to a decomposition with type " + decompType);
        }
    }

    /**
     * Add a child element using OR decomposition.
     *
     * @param child The child element to add
     */
    public void addORChild(DecompositionElement child) {
        if (decompType == null || decompType == DecompType.OR) {
            decompType = DecompType.OR;
            children.add(child);
            child.setParent(this);
        } else {
            throw new IllegalStateException("Cannot add OR child to a decomposition with type " + decompType);
        }
    }

    /**
     * Override toString to prevent infinite recursion from parent-child circular references.
     * Only includes essential information without traversing the object graph.
     */
    @Override
    public String toString() {
        return "DecompositionElement{id=" + getId() +
                ", decompType=" + decompType +
                ", childCount=" + (children != null ? children.size() : 0) +
                ", hasParent=" + (parent != null) + "}";
    }
}