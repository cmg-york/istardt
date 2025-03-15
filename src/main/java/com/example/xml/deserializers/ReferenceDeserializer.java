package com.example.xml.deserializers;

import com.example.objects.Element;
import com.example.xml.ReferenceResolver;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Deserializer for references to other elements by ID.
 * This deserializer handles resolving references between objects during deserialization.
 *
 * @param <T> The type of element being referenced
 */
public class ReferenceDeserializer<T extends Element> extends BaseDeserializer<T> {
    private static final Logger LOGGER = Logger.getLogger(ReferenceDeserializer.class.getName());

    private final Class<T> targetClass;

    /**
     * Constructor specifying the target class.
     *
     * @param targetClass The class representing the expected type of the reference
     */
    public ReferenceDeserializer(Class<T> targetClass) {
        super(targetClass);
        this.targetClass = targetClass;
    }

    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        // If it's a direct reference (just the ID as a string)
        if (node.isTextual()) {
            String id = node.asText();
            return resolveReference(id);
        }

        // If it's a reference node with a "ref" attribute
        if (node.has("ref")) {
            String id = node.get("ref").asText();
            return resolveReference(id);
        }

        LOGGER.warning("Unable to resolve reference from node: " + node);
        return null;
    }

    /**
     * Resolves a reference by ID and casts it to the target type.
     *
     * @param id The ID of the referenced element
     * @return The referenced element cast to the target type, or null if not found or wrong type
     */
    @SuppressWarnings("unchecked")
    private T resolveReference(String id) {
        Element element = ReferenceResolver.getInstance().getElementById(id);

        if (element != null) {
            if (targetClass.isInstance(element)) {
                return (T) element;
            } else {
                LOGGER.warning("Element with ID '" + id + "' is not of expected type " + targetClass.getSimpleName());
            }
        } else {
            LOGGER.warning("No element found with ID: " + id);
        }

        return null;
    }
}