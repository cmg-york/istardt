package com.example.xml;

import com.example.objects.Element;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Enhanced central reference resolver for managing object references during deserialization.
 * Implemented as a singleton to provide global access during the unmarshalling process.
 * Adds name-based lookup capabilities to support content-based references.
 */
public class ReferenceResolver {
    private static final Logger LOGGER = Logger.getLogger(ReferenceResolver.class.getName());
    private static final ReferenceResolver INSTANCE = new ReferenceResolver();

    // Map of elements by ID for quick lookup
    private final Map<String, Element> elementsById = new HashMap<>();

    // Map of elements by name (titleText) for content-based lookup
    private final Map<String, Element> elementsByName = new HashMap<>();

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
     * Also registers by name (titleText) if available for content-based resolution.
     *
     * @param id The unique identifier for the element
     * @param element The element to register
     */
    public void registerElement(String id, Element element) {
        if (id != null && element != null) {
            elementsById.put(id, element);

            if (element.getAtom() != null && element.getAtom().getTitleText() != null) {
                String name = element.getAtom().getTitleText();
                elementsByName.put(name, element);
            }
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
            // Try looking up by name as a fallback
            element = elementsByName.get(id);
            if (element == null) {
                LOGGER.warning("Element with ID or name '" + id + "' not found in reference resolver");
            }
        }
        return element;
    }

    /**
     * Retrieves an element by its name (titleText).
     * This is useful for content-based resolution.
     *
     * @param name The name (titleText) of the element to retrieve
     * @return The element with the given name, or null if not found
     */
    public Element getElementByName(String name) {
        Element element = elementsByName.get(name);
        if (element == null) {
            LOGGER.warning("Element with name '" + name + "' not found in reference resolver");
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
     * Retrieves an element by its name (titleText) and casts it to the specified type.
     *
     * @param <T> The expected type of the element
     * @param name The name (titleText) of the element to retrieve
     * @param clazz The class representing the expected type
     * @return The element cast to the specified type, or null if not found or of wrong type
     */
    @SuppressWarnings("unchecked")
    public <T extends Element> T getElementByName(String name, Class<T> clazz) {
        Element element = getElementByName(name);
        if (element != null && clazz.isInstance(element)) {
            return (T) element;
        }
        return null;
    }

    /**
     * Gets all element IDs registered in the resolver.
     *
     * @return A set of all element IDs
     */
    public Set<String> getAllElementIds() {
        return elementsById.keySet();
    }

    /**
     * Gets all element names registered in the resolver.
     *
     * @return A set of all element names
     */
    public Set<String> getAllElementNames() {
        return elementsByName.keySet();
    }

    /**
     * Clears all registered elements.
     * This should be called before processing a new XML file.
     */
    public void clear() {
        elementsById.clear();
        elementsByName.clear();
    }

    /**
     * Gets the number of registered elements by ID.
     * Useful for debugging and testing.
     *
     * @return The number of elements registered by ID
     */
    public int getElementCount() {
        return elementsById.size();
    }

    /**
     * Gets the number of registered elements by name.
     * Useful for debugging and testing.
     *
     * @return The number of elements registered by name
     */
    public int getElementByNameCount() {
        return elementsByName.size();
    }
}