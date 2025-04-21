package com.example.xml.deserializers;

import com.example.objects.Effect;
import com.example.objects.Formula;
import com.example.xml.utils.DeserializerUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.logging.Logger;

/**
 * Deserializer for Effect objects with support for formula-based pre/npr.
 */
public class EffectDeserializer extends BaseDeserializer<Effect> {
    private static final Logger LOGGER = Logger.getLogger(EffectDeserializer.class.getName());

    public EffectDeserializer() {
        super(Effect.class);
    }

    @Override
    protected Effect createNewElement() {
        return new Effect();
    }

    @Override
    protected void handleSpecificAttributes(Effect effect, JsonNode node, JsonParser p, DeserializationContext ctxt) throws IOException {
        // Set specific attributes
        boolean satisfying = DeserializerUtils.getBooleanAttribute(node, "satisfying", true);
        float probability = DeserializerUtils.getFloatAttribute(node, "probability", 1.0f);

        effect.setSatisfying(satisfying);
        effect.setProbability(probability);

        // Process string list properties for turnsTrue and turnsFalse
        Map<String, BiConsumer<Effect, List<String>>> listSetters = new HashMap<>();
        listSetters.put("turnsTrue", Effect::setTurnsTrue);
        listSetters.put("turnsFalse", Effect::setTurnsFalse);

        // Apply list-based properties
        for (Map.Entry<String, BiConsumer<Effect, List<String>>> entry : listSetters.entrySet()) {
            List<String> values = DeserializerUtils.getStringList(node, entry.getKey());
            entry.getValue().accept(effect, values);
        }

        // Process pre formula
        processPreFormula(effect, node, p, ctxt);

        // Process npr formula
        processNprFormula(effect, node, p, ctxt);
    }

    /**
     * Process the pre formula element for an effect.
     *
     * @param effect The effect to set the pre formula on
     * @param node The parent JSON node
     * @param p The JSON parser
     * @param ctxt The deserialization context
     */
    private void processPreFormula(Effect effect, JsonNode node, JsonParser p, DeserializationContext ctxt) throws IOException {
        Formula formula = DeserializerUtils.processFormula("pre", node, p, ctxt, LOGGER);
        if (formula != null) {
            effect.setPreFormula(formula);
            DeserializerUtils.logInfo(LOGGER, "Set pre formula for effect: " + effect.getId());
        }
    }

    /**
     * Process the npr formula element for an effect.
     *
     * @param effect The effect to set the npr formula on
     * @param node The parent JSON node
     * @param p The JSON parser
     * @param ctxt The deserialization context
     */
    private void processNprFormula(Effect effect, JsonNode node, JsonParser p, DeserializationContext ctxt) throws IOException {
        Formula formula = DeserializerUtils.processFormula("npr", node, p, ctxt, LOGGER);
        if (formula != null) {
            effect.setNprFormula(formula);
            DeserializerUtils.logInfo(LOGGER, "Set npr formula for effect: " + effect.getId());
        }
    }
}