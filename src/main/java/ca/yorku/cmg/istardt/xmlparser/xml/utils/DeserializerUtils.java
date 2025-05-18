package ca.yorku.cmg.istardt.xmlparser.xml.utils;

import ca.yorku.cmg.istardt.xmlparser.objects.Formula;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Enhanced utility class providing common methods for deserializers.
 */
public class DeserializerUtils {
    private static final Logger LOGGER = Logger.getLogger(DeserializerUtils.class.getName());

    /**
     * Centralized logging with appropriate level checking.
     * Logs at INFO level for normal processing information.
     */
    public static void logInfo(Logger logger, String message) {
        if (logger.isLoggable(Level.INFO)) {
            logger.info(message);
        }
    }

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
     * Process a node that directly contains a formula expression.
     * @param node The node containing the formula expression
     * @param p The JsonParser
     * @param ctxt The DeserializationContext
     * @param logger Logger for messages
     * @return The deserialized Formula, or null if no expression found
     */
    public static Formula processDirectFormula(JsonNode node, JsonParser p, DeserializationContext ctxt, Logger logger) {
        try {
            // Check if the node has any formula expression
            if (containsFormulaExpression(node)) {
                return ctxt.readValue(node.traverse(p.getCodec()), Formula.class);
            }

            logger.warning("No formula expression found in node: " + node);
            return null;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error processing direct formula: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Check if a node contains any formula expression element.
     */
    private static boolean containsFormulaExpression(JsonNode node) {
        String[] expressions = {
                "boolConst", "numConst", "predicateID", "goalID", "taskID", "variableID", "qualID",
                "previous", "add", "subtract", "multiply", "divide", "negate",
                "gt", "gte", "lt", "lte", "eq", "neq", "and", "or", "not"
        };
        for (String expr : expressions) {
            if (node.has(expr)) return true;
        }
        return false;
    }

    public static Formula processFormula(String elementName, JsonNode node, JsonParser p, DeserializationContext ctxt, Logger logger) throws IOException {
        if (node.has(elementName)) {
            JsonNode formulaNode = node.get(elementName);
            if (formulaNode.has("formula")) {
                try {
                    return ctxt.readValue(formulaNode.get("formula").traverse(p.getCodec()), Formula.class);
                } catch (Exception e) {
                    logger.warning("Error processing " + elementName + " formula: " + e.getMessage());
                }
            }
        }
        return null;
    }

    /**
     * Deserializes a list of nodes using a custom converter function.
     *
     * @param nodes The JSON nodes to deserialize
     * @param converter The function to convert each node
     * @return A list of converted objects
     */
    public static <T> List<T> deserializeWithConverter(JsonNode nodes, Function<JsonNode, T> converter) {
        List<T> result = new ArrayList<>();

        if (nodes == null) {
            return result;
        }

        if (nodes.isArray()) {
            for (JsonNode node : nodes) {
                T item = converter.apply(node);
                if (item != null) {
                    result.add(item);
                }
            }
        } else {
            T item = converter.apply(nodes);
            if (item != null) {
                result.add(item);
            }
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
     * Safely extracts an integer attribute from a node.
     */
    public static int getIntAttribute(JsonNode node, String attributeName, int defaultValue) {
        if (node == null || !node.has(attributeName)) {
            return defaultValue;
        }
        return node.get(attributeName).asInt(defaultValue);
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

    /**
     * Checks if a node has a specific field.
     */
    public static boolean hasField(JsonNode node, String fieldName) {
        return node != null && node.has(fieldName);
    }

    /**
     * Gets a child node if it exists, or null if it doesn't.
     */
    public static JsonNode getChildNode(JsonNode node, String fieldName) {
        if (node != null && node.has(fieldName)) {
            return node.get(fieldName);
        }
        return null;
    }

    /**
     * Extracts references from nodes that may contain "ref" attributes.
     *
     * @param node The node containing references
     * @return List of reference strings
     */
    public static List<String> extractReferences(JsonNode node) {
        List<String> refs = new ArrayList<>();

        if (node == null) {
            return refs;
        }

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

    /**
     * Processes a node that may contain an array of items or a single item.
     *
     * @param node The node to process
     * @param processor The function to process each item
     * @return List of processed items
     */
    public static <T> List<T> processNodeItems(JsonNode node, Function<JsonNode, T> processor) {
        List<T> results = new ArrayList<>();

        if (node == null) {
            return results;
        }

        if (node.isArray()) {
            for (JsonNode item : node) {
                T result = processor.apply(item);
                if (result != null) {
                    results.add(result);
                }
            }
        } else {
            T result = processor.apply(node);
            if (result != null) {
                results.add(result);
            }
        }

        return results;
    }
}