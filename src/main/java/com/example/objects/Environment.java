package com.example.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Extensions to the Environment class to add functionality needed for XML mapping
 */
public class Environment {
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
     * Add a non-decomposition element to the environment
     */
    public void addNonDecompElement(NonDecompositionElement element) {
        if (nonDecompElements == null) {
            nonDecompElements = new ArrayList<>();
        }
        nonDecompElements.add(element);
    }

    /**
     * Get a non-decomposition element by ID
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
}