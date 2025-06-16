package ca.yorku.cmg.istardt.xmlparser.objects;

import java.util.Objects;

public abstract class Element {

    private String id;

    private Atom representation;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get the atom representation of this element.
     */
    public Atom getAtom() {
        return representation;
    }

    /**
     * Set the atom representation of this element.
     */
    public void setRepresentation(Atom representation) {
        this.representation = representation;
    }

    /**
     * Gets the name of this element, which is stored in the atom's titleText.
     *
     * @return The name of this element, or null if the atom is not set
     */
    public String getName() {
        return representation != null ? representation.getTitleText() : null;
    }

    @Override
    public String toString() {
        return "Element{" +
                "id='" + id + '\'' +
                ", representation=" + representation +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Element element)) return false;

        return Objects.equals(representation, element.representation);
    }

    @Override
    public int hashCode() {
        return representation != null ? representation.hashCode() : 0;
    }
}