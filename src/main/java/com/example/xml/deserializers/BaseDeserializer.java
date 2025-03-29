package com.example.xml.deserializers;

import com.example.objects.Atom;
import com.example.objects.Element;
import com.example.xml.ReferenceResolver;
import com.example.xml.utils.DeserializerUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;
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
     * Handles name, description, and ID generation in a consistent way.
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
     * Processes common string list properties that appear in multiple element types.
     *
     * @param element The element to set properties on
     * @param node The JSON node to extract from
     * @param propertyConfigs Map of property names to setter methods
     */
    protected void processStringListProperties(T element, JsonNode node,
                                               Map<String, BiConsumer<T, String>> propertyConfigs) {
        for (Map.Entry<String, BiConsumer<T, String>> entry : propertyConfigs.entrySet()) {
            String propertyName = entry.getKey();
            BiConsumer<T, String> setter = entry.getValue();

            List<String> values = DeserializerUtils.getStringList(node, propertyName);
            for (String value : values) {
                setter.accept(element, value);
            }
        }
    }

    /**
     * Processes a list property with a standard setter taking a List.
     *
     * @param element The element to set properties on
     * @param node The JSON node to extract from
     * @param propertyName The name of the property in the XML
     * @param setter The setter method that accepts a List
     */
    protected <V> void processListProperty(T element, JsonNode node,
                                           String propertyName,
                                           BiConsumer<T, List<V>> setter,
                                           Function<JsonNode, V> itemConverter) {
        if (node.has(propertyName)) {
            JsonNode propertyNode = node.get(propertyName);
            List<V> items = new ArrayList<>();

            if (propertyNode.isArray()) {
                for (JsonNode itemNode : propertyNode) {
                    items.add(itemConverter.apply(itemNode));
                }
            } else {
                items.add(itemConverter.apply(propertyNode));
            }

            setter.accept(element, items);
        }
    }

    /**
     * Processes a nested element that needs to be deserialized with a custom deserializer.
     *
     * @param node The node containing the nested element
     * @param fieldName The field name of the nested element
     * @param parser The original parser
     * @param context The deserialization context
     * @param elementType The type of the nested element
     * @return The deserialized element or null if not found
     */
    protected <E> E processNestedElement(JsonNode node, String fieldName,
                                         JsonParser parser, DeserializationContext context,
                                         Class<E> elementType) throws IOException {
        if (node.has(fieldName)) {
            return context.readValue(node.get(fieldName).traverse(parser.getCodec()), elementType);
        }
        return null;
    }

    /**
     * Processes a list of nested elements that need to be deserialized.
     *
     * @param node The node containing the nested elements
     * @param fieldName The field name of the nested elements
     * @param parser The original parser
     * @param context The deserialization context
     * @param elementType The type of the nested elements
     * @return The list of deserialized elements
     */
    protected <E> List<E> processNestedElementList(JsonNode node, String fieldName,
                                                   JsonParser parser, DeserializationContext context,
                                                   Class<E> elementType) throws IOException {
        List<E> result = new ArrayList<>();
        if (node.has(fieldName)) {
            JsonNode elementsNode = node.get(fieldName);
            return DeserializerUtils.deserializeList(elementsNode, parser, context, elementType);
        }
        return result;
    }

    /**
     * Creates an Atom object from a name and description.
     * IMPORTANT: The name should be used as the titleText for proper content-based comparison.
     *
     * @param name The ID for the atom (should be the element's name attribute from XML)
     * @param description The description for the atom
     * @return A new Atom object
     */
    protected Atom createAtom(String name, String description) {
        Atom atom = new Atom();
        atom.setId(UUID.randomUUID().toString());

        // IMPORTANT: Set the titleText to the name/id for proper content comparison
        // This ensures that the atom's titleText contains the actual name from the XML
        atom.setTitleText(name);

        // Set the description as both description and HTML text if provided
        if (description != null && !description.isEmpty()) {
            atom.setDescription(description); // Set the new description field
            atom.setTitleHTMLText(description); // Keep setting titleHTMLText for backward compatibility
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