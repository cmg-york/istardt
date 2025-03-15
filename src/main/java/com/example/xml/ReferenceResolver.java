package com.example.xml;

import com.example.objects.Element;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Central reference resolver for managing object references during deserialization.
 * Implemented as a singleton to provide global access during the unmarshalling process.
 */
public class ReferenceResolver {
    private static final Logger LOGGER = Logger.getLogger(ReferenceResolver.class.getName());
    private static final ReferenceResolver INSTANCE = new ReferenceResolver();

    // Map of elements by ID for quick lookup
    private final Map<String, Element> elementsById = new HashMap<>();

    /**
     * Private constructor for singleton pattern
     */
    private ReferenceResolver() {
        // Private constructor to prevent instantiation
    }

    /**
     * Gets the singleton instance of the reference resolver.
     *
     * @return The shared ReferenceResolver instance
     */
    public static ReferenceResolver getInstance() {
        return INSTANCE;
    }

    /**
     * Registers an element with its ID for later reference resolution.
     *
     * @param id The unique identifier for the element
     * @param element The element to register
     */
    public void registerElement(String id, Element element) {
        if (id != null && element != null) {
            elementsById.put(id, element);
        }
    }

    /**
     * Retrieves an element by its ID.
     *
     * @param id The unique identifier of the element to retrieve
     * @return The element with the given ID, or null if not found
     */
    public Element getElementById(String id) {
        Element element = elementsById.get(id);
        if (element == null) {
            LOGGER.warning("Element with ID '" + id + "' not found in reference resolver");
        }
        return element;
    }

    /**
     * Retrieves an element by its ID and casts it to the specified type.
     *
     * @param <T> The expected type of the element
     * @param id The unique identifier of the element to retrieve
     * @param clazz The class representing the expected type
     * @return The element cast to the specified type, or null if not found or of wrong type
     */
    @SuppressWarnings("unchecked")
    public <T extends Element> T getElementById(String id, Class<T> clazz) {
        Element element = getElementById(id);
        if (element != null && clazz.isInstance(element)) {
            return (T) element;
        }
        return null;
    }

    /**
     * Clears all registered elements.
     * This should be called before processing a new XML file.
     */
    public void clear() {
        elementsById.clear();
    }

    /**
     * Gets the number of registered elements.
     * Useful for debugging and testing.
     *
     * @return The number of elements registered
     */
    public int getElementCount() {
        return elementsById.size();
    }
}