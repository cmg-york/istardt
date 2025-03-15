package com.example.xml.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class providing common methods for deserializers.
 */
public class DeserializerUtils {
    private static final Logger LOGGER = Logger.getLogger(DeserializerUtils.class.getName());

    /**
     * Deserializes a node or array of nodes into a list of objects.
     * Handles both single items and arrays transparently.
     */
    public static <T> List<T> deserializeList(JsonNode nodes, JsonParser parser,
                                              DeserializationContext ctxt, Class<T> type) throws IOException {
        List<T> result = new ArrayList<>();

        if (nodes == null) {
            return result;
        }

        try {
            if (nodes.isArray()) {
                for (JsonNode node : nodes) {
                    T item = ctxt.readValue(node.traverse(parser.getCodec()), type);
                    if (item != null) {
                        result.add(item);
                    }
                }
            } else {
                T item = ctxt.readValue(nodes.traverse(parser.getCodec()), type);
                if (item != null) {
                    result.add(item);
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error deserializing list of " + type.getSimpleName(), e);
            throw e;
        }

        return result;
    }

    /**
     * Safely extracts a string attribute from a node.
     */
    public static String getStringAttribute(JsonNode node, String attributeName, String defaultValue) {
        if (node == null || !node.has(attributeName)) {
            return defaultValue;
        }
        return node.get(attributeName).asText(defaultValue);
    }

    /**
     * Safely extracts a boolean attribute from a node.
     */
    public static boolean getBooleanAttribute(JsonNode node, String attributeName, boolean defaultValue) {
        if (node == null || !node.has(attributeName)) {
            return defaultValue;
        }
        return node.get(attributeName).asBoolean(defaultValue);
    }

    /**
     * Safely extracts a float attribute from a node.
     */
    public static float getFloatAttribute(JsonNode node, String attributeName, float defaultValue) {
        if (node == null || !node.has(attributeName)) {
            return defaultValue;
        }
        return (float) node.get(attributeName).asDouble(defaultValue);
    }

    /**
     * Standardized error handling for deserialization.
     */
    public static void handleDeserializationError(Logger logger, String message, IOException e) throws IOException {
        logger.log(Level.SEVERE, message, e);
        throw new IOException(message + ": " + e.getMessage(), e);
    }

    /**
     * Safely extracts a list of string values from child nodes with a specific name.
     */
    public static List<String> getStringList(JsonNode node, String childName) {
        List<String> result = new ArrayList<>();

        if (node == null || !node.has(childName)) {
            return result;
        }

        JsonNode childNode = node.get(childName);
        if (childNode.isArray()) {
            for (JsonNode item : childNode) {
                result.add(item.asText());
            }
        } else {
            result.add(childNode.asText());
        }

        return result;
    }
}