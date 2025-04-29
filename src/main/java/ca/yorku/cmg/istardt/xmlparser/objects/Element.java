package ca.yorku.cmg.istardt.xmlparser.objects;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * Base class for all domain elements with ID and representation.
 * Modified to support Jackson XML unmarshalling.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public abstract class Element {

    @JacksonXmlProperty(isAttribute = true, localName = "id")
    private String id;

    @JsonIgnore
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
     * This is a convenience method equivalent to getAtom().getTitleText().
     * This method is also annotated for XML serialization/deserialization.
     *
     * @return The name of this element, or null if the atom is not set
     */
    @JacksonXmlProperty(isAttribute = true, localName = "name")
    public String getName() {
        return representation != null ? representation.getTitleText() : null;
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
}