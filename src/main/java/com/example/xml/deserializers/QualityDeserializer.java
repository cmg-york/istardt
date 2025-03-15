package com.example.xml.deserializers;

import com.example.objects.Atom;
import com.example.objects.Formula;
import com.example.objects.Quality;
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
    public Quality deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        // Create new Quality
        Quality quality = new Quality();

        // Set basic properties
        String name = getName(node);
        String description = getDescription(node);
        boolean root = getBooleanAttribute(node, "root", false);
        boolean exported = getBooleanAttribute(node, "exported", false);

        quality.setId(name);
        quality.setRoot(root);
        quality.setExported(exported);

        // Create an atom for the quality
        Atom atom = createAtom(name, description);
        quality.setAtom(atom);

        // Process formula
        if (node.has("formula")) {
            Formula formula = ctxt.readValue(node.get("formula").traverse(p.getCodec()), Formula.class);
            quality.setValueFormula(formula);
        }

        // Register the quality for reference resolution
        registerElement(quality);

        return quality;
    }
}