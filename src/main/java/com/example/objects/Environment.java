package com.example.objects;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
     * Compares this environment to the specified object.
     * Environments are considered equal if they have similar non-decomposition elements
     * based on their names/titleTexts.
     *
     * @param obj The object to compare this environment to
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Environment that = (Environment) obj;

        // Compare elements by their names/titleTexts to avoid circular references
        List<String> thisElementNames = nonDecompElements != null ?
                nonDecompElements.stream()
                        .map(e -> e.getAtom() != null ? e.getAtom().getTitleText() : e.getId())
                        .sorted()
                        .collect(Collectors.toList()) :
                new ArrayList<>();

        List<String> thatElementNames = that.nonDecompElements != null ?
                that.nonDecompElements.stream()
                        .map(e -> e.getAtom() != null ? e.getAtom().getTitleText() : e.getId())
                        .sorted()
                        .collect(Collectors.toList()) :
                new ArrayList<>();

        return thisElementNames.equals(thatElementNames);
    }

    /**
     * Returns a hash code value for this environment.
     * The hash code is computed based on the element names/titleTexts
     * to ensure it satisfies the contract with equals().
     *
     * @return A hash code value for this environment
     */
    @Override
    public int hashCode() {
        List<String> elementNames = nonDecompElements != null ?
                nonDecompElements.stream()
                        .map(e -> e.getAtom() != null ? e.getAtom().getTitleText() : e.getId())
                        .sorted()
                        .collect(Collectors.toList()) :
                new ArrayList<>();

        return Objects.hash(elementNames);
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