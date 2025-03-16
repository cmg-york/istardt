package com.example.objects;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.Objects;

/**
 * Base class for all domain elements with ID and representation.
 * Modified to support Jackson XML unmarshalling.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public abstract class Element {

    @JacksonXmlProperty(isAttribute = true, localName = "name")
    private String id;

    @JsonIgnore
    private Atom representation;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Atom getAtom() {
        return representation;
    }

    public void setRepresentation(Atom representation) {
        this.representation = representation;
    }

    /**
     * Returns a string representation of this element.
     * Includes the ID and class name to identify the element.
     *
     * @return A string representation of this element
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{id='" + id + "'}";
    }

    /**
     * Compares this element to the specified object.
     * Elements are considered equal if they are of the same class and have equivalent content,
     * primarily based on the atom's titleText which should contain the name.
     *
     * @param obj The object to compare this element to
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Element element = (Element) obj;

        // Compare using atom's titleText (name) if available
        if (representation != null && element.representation != null) {
            return Objects.equals(representation.getTitleText(), element.representation.getTitleText());
        }

        // Otherwise use the ID which may represent the name
        return Objects.equals(id, element.id);
    }

    /**
     * Returns a hash code value for this element.
     * The hash code is computed based on the class and the atom's titleText (name)
     * to ensure it satisfies the contract with equals().
     *
     * @return A hash code value for this element
     */
    @Override
    public int hashCode() {
        String titleText = representation != null ? representation.getTitleText() : id;
        return Objects.hash(getClass(), titleText);
    }
}