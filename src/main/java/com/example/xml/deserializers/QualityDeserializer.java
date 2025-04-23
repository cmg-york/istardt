package com.example.xml.deserializers;

import com.example.objects.Formula;
import com.example.objects.Quality;
import com.example.xml.utils.DeserializerUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Deserializer for Quality objects.
 */
public class QualityDeserializer extends BaseDeserializer<Quality> {
    private static final Logger LOGGER = Logger.getLogger(QualityDeserializer.class.getName());

    public QualityDeserializer() {
        super(Quality.class);
    }

    @Override
    protected Quality createNewElement() {
        return new Quality();
    }

    @Override
    protected void handleSpecificAttributes(Quality quality, JsonNode node, JsonParser p, DeserializationContext ctxt) throws IOException {
        // Get specific attributes
        boolean root = DeserializerUtils.getBooleanAttribute(node, "root", false);
        boolean exported = DeserializerUtils.getBooleanAttribute(node, "exported", false);

        quality.setRoot(root);
        quality.setExported(exported);

        // Process formula
        try {
            if (node.has("formula")) {
                Formula formula = ctxt.readValue(node.get("formula").traverse(p.getCodec()), Formula.class);
                quality.setValueFormula(formula);
            }
        } catch (IOException e) {
            DeserializerUtils.handleDeserializationError(LOGGER,
                    "Error processing formula for quality " + quality.getAtom().getTitleText(), e);
        }
    }
}