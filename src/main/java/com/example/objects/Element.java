package com.example.objects;

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
}