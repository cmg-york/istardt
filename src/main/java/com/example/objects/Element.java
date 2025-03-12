package com.example.objects;

/**
 * Extensions to the Element class to add functionality needed for XML mapping
 */
public abstract class Element {
    private String id;
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