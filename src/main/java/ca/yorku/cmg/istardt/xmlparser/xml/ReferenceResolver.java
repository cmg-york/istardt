package ca.yorku.cmg.istardt.xmlparser.xml;

import ca.yorku.cmg.istardt.xmlparser.objects.Element;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Reference resolver for managing object references during deserialization.
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
            LOGGER.warning("Element with ID or name '" + id + "' not found in reference resolver");
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
     * Gets all element IDs registered in the resolver.
     *
     * @return A set of all element IDs
     */
    public Set<String> getAllElementIds() {
        return elementsById.keySet();
    }

    /**
     * Clears all registered elements.
     */
    public void clear() {
        elementsById.clear();
        elementsByName.clear();
    }
}