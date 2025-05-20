package ca.yorku.cmg.istardt.xmlparser.objects;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

public class DecompositionElement extends Element {

    protected List<DecompositionElement> children;
    protected DecompType decompType;
    protected Formula preFormula;
    protected Formula nprFormula;
    protected DecompositionElement parent;
    protected JsonNode rawPreFormulaNode;
    protected JsonNode rawNprFormulaNode;

    public JsonNode getRawPreFormulaNode() {
        return rawPreFormulaNode;
    }

    public void setRawPreFormulaNode(JsonNode rawPreFormulaNode) {
        this.rawPreFormulaNode = rawPreFormulaNode;
    }

    public JsonNode getRawNprFormulaNode() {
        return rawNprFormulaNode;
    }

    public void setRawNprFormulaNode(JsonNode rawNprFormulaNode) {
        this.rawNprFormulaNode = rawNprFormulaNode;
    }

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
    public boolean isSibling(DecompositionElement e) {

        if (parent == null || e == null || e.getParent() == null) {
            return false;
        }
        return parent.equals(e.getParent());
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

    @Override
    public String toString() {
        return "DecompositionElement{id=" + getId() +
                ", decompType=" + decompType +
                ", childCount=" + (children != null ? children.size() : 0) +
                ", hasParent=" + (parent != null) + "}";
    }
}