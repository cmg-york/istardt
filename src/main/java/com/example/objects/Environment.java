package com.example.objects;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Environment class representing the context in which actors operate.
 * Modified to support Jackson XML unmarshalling.
 */
public class Environment {

    @JacksonXmlElementWrapper(localName = "elements")
    @JacksonXmlProperty(localName = "element")
    @JsonManagedReference("environment-elements")
    private List<NonDecompositionElement> nonDecompElements;

    public Environment() {
        this.nonDecompElements = new ArrayList<>();
    }

    public List<NonDecompositionElement> getNonDecompElements() {
        return nonDecompElements;
    }

    public void setNonDecompElements(List<NonDecompositionElement> nonDecompElements) {
        this.nonDecompElements = nonDecompElements;
    }

    /**
     * Add a non-decomposition element to the environment.
     *
     * @param element The element to add
     */
    public void addNonDecompElement(NonDecompositionElement element) {
        if (nonDecompElements == null) {
            nonDecompElements = new ArrayList<>();
        }
        nonDecompElements.add(element);
    }

    /**
     * Get a non-decomposition element by ID.
     *
     * @param id The ID of the element to find
     * @return The element with the given ID, or null if not found
     */
    public NonDecompositionElement getElementById(String id) {
        if (nonDecompElements != null) {
            for (NonDecompositionElement element : nonDecompElements) {
                if (element.getId().equals(id)) {
                    return element;
                }
            }
        }
        return null;
    }

    /**
     * Returns a string representation of this environment.
     * Includes count of non-decomposition elements.
     *
     * @return A string representation of this environment
     */
    @Override
    public String toString() {
        int elementCount = nonDecompElements != null ? nonDecompElements.size() : 0;

        return "Environment{nonDecompElements=" + elementCount + "}";
    }
}