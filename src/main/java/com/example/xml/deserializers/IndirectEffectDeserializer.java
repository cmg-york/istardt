package com.example.xml.deserializers;

import com.example.objects.Formula;
import com.example.objects.IndirectEffect;
import com.example.xml.utils.DeserializerUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Deserializer for IndirectEffect objects.
 */
public class IndirectEffectDeserializer extends BaseDeserializer<IndirectEffect> {
    private static final Logger LOGGER = Logger.getLogger(IndirectEffectDeserializer.class.getName());

    public IndirectEffectDeserializer() {
        super(IndirectEffect.class);
    }

    @Override
    public IndirectEffect deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        // Create new IndirectEffect
        IndirectEffect indirectEffect = new IndirectEffect();

        // Extract common attributes (id, name, description, atom)
        extractCommonAttributes(indirectEffect, node);

        // Get specific attributes
        boolean exported = DeserializerUtils.getBooleanAttribute(node, "exported", false);
        indirectEffect.setExported(exported);

        // Process formula
        try {
            if (node.has("formula")) {
                Formula formula = ctxt.readValue(node.get("formula").traverse(p.getCodec()), Formula.class);
                indirectEffect.setValueFormula(formula);
            }
        } catch (IOException e) {
            DeserializerUtils.handleDeserializationError(LOGGER,
                    "Error processing formula for indirect effect " + indirectEffect.getAtom().getTitleText(), e);
        }

        return indirectEffect;
    }
}