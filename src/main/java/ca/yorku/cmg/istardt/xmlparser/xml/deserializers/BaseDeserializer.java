package ca.yorku.cmg.istardt.xmlparser.xml.deserializers;

import ca.yorku.cmg.istardt.xmlparser.objects.Atom;
import ca.yorku.cmg.istardt.xmlparser.objects.Element;
import ca.yorku.cmg.istardt.xmlparser.xml.ReferenceResolver;
import ca.yorku.cmg.istardt.xmlparser.xml.utils.DeserializerUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Enhanced base deserializer with expanded common functionality for all iStar-T elements.
 * Provides standardized helpers for extracting attributes, processing nodes, and handling lists.
 *
 * @param <T> The type of element being deserialized, must extend Element
 */
public abstract class BaseDeserializer<T extends Element> extends StdDeserializer<T> {
    private static final Logger LOGGER = Logger.getLogger(BaseDeserializer.class.getName());

    protected BaseDeserializer(Class<T> vc) {
        super(vc);
    }

    /**
     * Main deserialize method that sets up the standard deserializing process.
     * Subclasses should override handleSpecificAttributes to add their customizations.
     */
    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        // Create and initialize element
        T element = createNewElement();

        // Extract common attributes
        extractCommonAttributes(element, node);

        // Handle specific attributes based on element type
        handleSpecificAttributes(element, node, p, ctxt);

        return element;
    }

    /**
     * Create a new instance of the element being deserialized.
     * Subclasses must implement this to provide their specific element type.
     */
    protected abstract T createNewElement();

    /**
     * Handle specific attributes for each element type.
     * Subclasses should override this to add element-specific logic.
     */
    protected abstract void handleSpecificAttributes(T element, JsonNode node, JsonParser p, DeserializationContext ctxt) throws IOException;

    /**
     * Extracts common attributes from a node and sets them on an element.
     * Handles name, description, and ID generation.
     *
     * @param element The element to set attributes on
     * @param node The JSON node to extract from
     * @return The element with attributes set
     */
    protected T extractCommonAttributes(T element, JsonNode node) {
        // Generate a UUID for the element ID if not provided
        String id = DeserializerUtils.getStringAttribute(node, "id", UUID.randomUUID().toString());
        element.setId(id);

        // Get the attributes from XML
        String name = DeserializerUtils.getStringAttribute(node, "name", null);
        String description = DeserializerUtils.getStringAttribute(node, "description", null);

        // Create and set the atom
        Atom atom = createAtom(name, description);
        element.setRepresentation(atom);

        // Register the element
        registerElement(element);

        return element;
    }

    /**
     * Creates an Atom object from a name and description.
     *
     * @param name The ID for the atom
     * @param description The description for the atom
     * @return A new Atom object
     */
    protected Atom createAtom(String name, String description) {
        Atom atom = new Atom();
        atom.setId(UUID.randomUUID().toString()); // atom's id != element's id

        atom.setTitleText(name);
        atom.setTitleHTMLText("<h>" + name + "</h>");

        if (description != null && !description.isEmpty()) {
            atom.setDescription(description);
        }
        return atom;
    }

    /**
     * Registers the element in the reference resolver.
     *
     * @param element The element to register
     */
    protected void registerElement(T element) {
        if (element != null && element.getId() != null) {
            ReferenceResolver.getInstance().registerElement(element.getId(), element);
        }
    }

    /**
     * Gets all child nodes with a specific field name.
     *
     * @param node The parent JSON node
     * @param fieldName The field name to look for
     * @return A list of matching child nodes
     */
    protected List<JsonNode> getChildNodes(JsonNode node, String fieldName) {
        List<JsonNode> result = new ArrayList<>();
        if (node.has(fieldName)) {
            JsonNode fieldNode = node.get(fieldName);
            if (fieldNode.isArray()) {
                fieldNode.forEach(result::add);
            } else {
                result.add(fieldNode);
            }
        }
        return result;
    }

    /**
     * Gets the value of a specific child node.
     *
     * @param node The parent JSON node
     * @param fieldName The field name to look for
     * @return The child node, or null if not found
     */
    protected JsonNode getChildNode(JsonNode node, String fieldName) {
        return node.has(fieldName) ? node.get(fieldName) : null;
    }

    /**
     * Extract references from nodes that may be single or array.
     *
     * @param node The node containing references
     * @return List of reference strings
     */
    protected List<String> extractReferences(JsonNode node) {
        List<String> refs = new ArrayList<>();

        if (node.isArray()) {
            for (JsonNode childNode : node) {
                if (childNode.has("ref")) {
                    refs.add(childNode.get("ref").asText());
                }
            }
        } else if (node.has("ref")) {
            refs.add(node.get("ref").asText());
        }
        return refs;
    }
}