package com.example.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Extensions to the DecompositionElement class to add functionality needed for XML mapping
 */
public class DecompositionElement extends Element {
    public List<DecompositionElement> children;
    public DecompType decompType;
    public Formula preFormula;
    public Formula nprFormula;
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

    public List<DecompositionElement> getSiblings() {
        if (parent == null) {
            return new ArrayList<>();
        }

        List<DecompositionElement> siblings = new ArrayList<>(parent.children);
        siblings.remove(this);
        return siblings;
    }

    public boolean isSiblings() {
        return !getSiblings().isEmpty();
    }

    public boolean isRoot() {
        return parent == null;
    }

    public void addANDChild(DecompositionElement child) {
        if (decompType == null || decompType == DecompType.AND) {
            decompType = DecompType.AND;
            children.add(child);
            child.setParent(this);
        } else {
            throw new IllegalStateException("Cannot add AND child to a decomposition with type " + decompType);
        }
    }

    public void addORChild(DecompositionElement child) {
        if (decompType == null || decompType == DecompType.OR) {
            decompType = DecompType.OR;
            children.add(child);
            child.setParent(this);
        } else {
            throw new IllegalStateException("Cannot add OR child to a decomposition with type " + decompType);
        }
    }
}