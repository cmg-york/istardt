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
import java.util.logging.Logger;

/**
 * Base deserializer with common functionality for all iStar-T elements.
 * Provides helper methods for extracting common attributes and processing nodes.
 *
 * @param <T> The type of element being deserialized, must extend Element
 */
public abstract class BaseDeserializer<T extends Element> extends StdDeserializer<T> {
    private static final Logger LOGGER = Logger.getLogger(BaseDeserializer.class.getName());

    protected BaseDeserializer(Class<T> vc) {
        super(vc);
    }

    /**
     * Extracts common attributes from a node and sets them on an element.
     * Handles name, description, and ID generation in a consistent way.
     *
     * @param element The element to set attributes on
     * @param node The JSON node to extract from
     * @return The element with attributes set
     */
    protected T extractCommonAttributes(T element, JsonNode node) {
        // Generate a UUID for the element ID
        String uuid = UUID.randomUUID().toString();
        element.setId(uuid);

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
     * Extracts the name attribute from a node.
     * Using this method for backward compatibility and cases where we want defaults
     * other than null.
     *
     * @param node The JSON node to extract from
     * @return The name/ID attribute, or null if not found
     */
    protected String getName(JsonNode node) {
        return DeserializerUtils.getStringAttribute(node, "name", null);
    }

    /**
     * Extracts the description attribute from a node.
     * Using this method for backward compatibility and cases where we want defaults
     * other than null.
     *
     * @param node The JSON node to extract from
     * @return The description attribute, or null if not found
     */
    protected String getDescription(JsonNode node) {
        return DeserializerUtils.getStringAttribute(node, "description", null);
    }

    /**
     * Extracts a boolean attribute from a node.
     * Using this method for backward compatibility.
     *
     * @param node The JSON node to extract from
     * @param attributeName The name of the attribute
     * @param defaultValue The default value if the attribute is not found
     * @return The boolean value of the attribute
     */
    protected boolean getBooleanAttribute(JsonNode node, String attributeName, boolean defaultValue) {
        return DeserializerUtils.getBooleanAttribute(node, attributeName, defaultValue);
    }

    /**
     * Extracts a float attribute from a node.
     * Using this method for backward compatibility.
     *
     * @param node The JSON node to extract from
     * @param attributeName The name of the attribute
     * @param defaultValue The default value if the attribute is not found
     * @return The float value of the attribute
     */
    protected float getFloatAttribute(JsonNode node, String attributeName, float defaultValue) {
        return DeserializerUtils.getFloatAttribute(node, attributeName, defaultValue);
    }

    /**
     * Creates an Atom object from a name and description.
     * IMPORTANT: The name should be used as the titleText for proper content-based comparison.
     *
     * @param id The ID for the atom (should be the element's name attribute from XML)
     * @param description The description for the atom
     * @return A new Atom object
     */
    protected Atom createAtom(String id, String description) {
        Atom atom = new Atom();
        atom.setId(UUID.randomUUID().toString());

        // IMPORTANT: Set the titleText to the name/id for proper content comparison
        // This ensures that the atom's titleText contains the actual name from the XML
        atom.setTitleText(id);  // Using id (which should be the name from XML) as titleText

        // Set the description as HTML text if provided
        if (description != null && !description.isEmpty()) {
            atom.setTitleHTMLText(description);
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
     * Extracts a list of string values from child nodes with a specific name.
     * Using this method for backward compatibility.
     *
     * @param node The parent JSON node
     * @param childName The name of the child nodes
     * @return A list of string values
     */
    protected List<String> getStringList(JsonNode node, String childName) {
        return DeserializerUtils.getStringList(node, childName);
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
}