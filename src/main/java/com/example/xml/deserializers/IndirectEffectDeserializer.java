package com.example.xml.deserializers;

import com.example.objects.Atom;
import com.example.objects.Formula;
import com.example.objects.IndirectEffect;
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

        // Set basic properties
        String name = getName(node);
        String description = getDescription(node);
        boolean exported = getBooleanAttribute(node, "exported", false);

        indirectEffect.setId(name);
        indirectEffect.setExported(exported);

        // Create an atom for the indirect effect
        Atom atom = createAtom(name, description);
        indirectEffect.setAtom(atom);

        // Process formula
        if (node.has("formula")) {
            Formula formula = ctxt.readValue(node.get("formula").traverse(p.getCodec()), Formula.class);
            indirectEffect.setValueFormula(formula);
        }

        // Register the indirect effect for reference resolution
        registerElement(indirectEffect);

        return indirectEffect;
    }
}