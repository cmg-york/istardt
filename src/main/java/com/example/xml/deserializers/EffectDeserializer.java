package com.example.xml.deserializers;

import com.example.objects.Atom;
import com.example.objects.Effect;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Deserializer for Effect objects.
 */
public class EffectDeserializer extends BaseDeserializer<Effect> {
    private static final Logger LOGGER = Logger.getLogger(EffectDeserializer.class.getName());

    public EffectDeserializer() {
        super(Effect.class);
    }

    @Override
    public Effect deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        // Create new Effect
        Effect effect = new Effect();

        // Set basic properties
        String name = getName(node);
        String description = getDescription(node);
        boolean satisfying = getBooleanAttribute(node, "satisfying", true);
        float probability = getFloatAttribute(node, "probability", 1.0f);

        effect.setId(name);
        effect.setSatisfying(satisfying);
        effect.setProbability(probability);

        // Create an atom for the effect
        Atom atom = createAtom(name, description);
        effect.setAtom(atom);

        // Process turnsTrue
        List<String> turnsTrue = getStringList(node, "turnsTrue");
        for (String predicate : turnsTrue) {
            effect.addTurnsTrue(predicate);
        }

        // Process turnsFalse
        List<String> turnsFalse = getStringList(node, "turnsFalse");
        for (String predicate : turnsFalse) {
            effect.addTurnsFalse(predicate);
        }

        // Process preconditions
        List<String> preconditions = getStringList(node, "pre");
        for (String pre : preconditions) {
            effect.addPrecondition(pre);
        }

        // Process negative preconditions
        List<String> negPreconditions = getStringList(node, "npr");
        for (String npr : negPreconditions) {
            effect.addNegPrecondition(npr);
        }

        // Register the effect for reference resolution
        registerElement(effect);

        return effect;
    }
}