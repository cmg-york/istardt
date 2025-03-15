package com.example.xml.deserializers;

import com.example.objects.Atom;
import com.example.objects.Element;
import com.example.xml.ReferenceResolver;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Base deserializer with common functionality for all iStar-T elements.
 * Provides helper methods for extracting common attributes and processing nodes.
 *
 * @param <T> The type of element being deserialized, must extend Element
 */
public abstract class BaseDeserializer<T extends Element> extends StdDeserializer<T> {

    protected BaseDeserializer(Class<T> vc) {
        super(vc);
    }

    /**
     * Extracts the name/ID attribute from a node.
     *
     * @param node The JSON node to extract from
     * @return The name/ID attribute, or null if not found
     */
    protected String getName(JsonNode node) {
        return node.has("name") ? node.get("name").asText() : null;
    }

    /**
     * Extracts the description attribute from a node.
     *
     * @param node The JSON node to extract from
     * @return The description attribute, or null if not found
     */
    protected String getDescription(JsonNode node) {
        return node.has("description") ? node.get("description").asText() : null;
    }

    /**
     * Extracts a boolean attribute from a node.
     *
     * @param node The JSON node to extract from
     * @param attributeName The name of the attribute
     * @param defaultValue The default value if the attribute is not found
     * @return The boolean value of the attribute
     */
    protected boolean getBooleanAttribute(JsonNode node, String attributeName, boolean defaultValue) {
        return node.has(attributeName) ? node.get(attributeName).asBoolean() : defaultValue;
    }

    /**
     * Extracts a string attribute from a node.
     *
     * @param node The JSON node to extract from
     * @param attributeName The name of the attribute
     * @return The string value of the attribute, or null if not found
     */
    protected String getStringAttribute(JsonNode node, String attributeName) {
        return node.has(attributeName) ? node.get(attributeName).asText() : null;
    }

    /**
     * Extracts a float attribute from a node.
     *
     * @param node The JSON node to extract from
     * @param attributeName The name of the attribute
     * @param defaultValue The default value if the attribute is not found
     * @return The float value of the attribute
     */
    protected float getFloatAttribute(JsonNode node, String attributeName, float defaultValue) {
        return node.has(attributeName) ? (float) node.get(attributeName).asDouble() : defaultValue;
    }

    /**
     * Creates an Atom object from a name and description.
     *
     * @param id The ID for the atom
     * @param description The description for the atom
     * @return A new Atom object
     */
    protected Atom createAtom(String id, String description) {
        Atom atom = new Atom();
        atom.setId(id);
        atom.setTitleText(description);
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
     *
     * @param node The parent JSON node
     * @param childName The name of the child nodes
     * @return A list of string values
     */
    protected List<String> getStringList(JsonNode node, String childName) {
        List<String> result = new ArrayList<>();
        if (node.has(childName)) {
            JsonNode childNode = node.get(childName);
            if (childNode.isArray()) {
                for (JsonNode item : childNode) {
                    result.add(item.asText());
                }
            } else {
                result.add(childNode.asText());
            }
        }
        return result;
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