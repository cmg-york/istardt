package com.example.xml.deserializers;

import com.example.objects.Atom;
import com.example.objects.Condition;
import com.example.objects.Formula;
import com.example.xml.utils.DeserializerUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Deserializer for Condition objects.
 */
public class ConditionDeserializer extends BaseDeserializer<Condition> {
    private static final Logger LOGGER = Logger.getLogger(ConditionDeserializer.class.getName());

    public ConditionDeserializer() {
        super(Condition.class);
    }

    @Override
    public Condition deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        // Create new Condition
        Condition condition = new Condition();

        // Set basic properties
        String name = DeserializerUtils.getStringAttribute(node, "name", null);
        String description = DeserializerUtils.getStringAttribute(node, "description", null);

        condition.setId(name);

        // Create an atom for the condition
        Atom atom = createAtom(name, description);
        condition.setAtom(atom);

        // Process formula - this could be a direct formula element or various boolean expressions
        Formula formula = null;

        try {
            if (node.has("formula")) {
                formula = ctxt.readValue(node.get("formula").traverse(p.getCodec()), Formula.class);
            } else {
                // Check for various boolean expressions that may be directly in the condition node
                if (node.has("boolConst")) {
                    String boolVal = node.get("boolConst").asText();
                    formula = Formula.createBooleanFormula(Boolean.parseBoolean(boolVal));
                } else if (node.has("boolAtom")) {
                    String atom_id = node.get("boolAtom").asText();
                    formula = Formula.createConstantFormula(atom_id);
                } else if (node.has("gt")) {
                    formula = ctxt.readValue(node.get("gt").traverse(p.getCodec()), Formula.class);
                } else if (node.has("and")) {
                    formula = ctxt.readValue(node.get("and").traverse(p.getCodec()), Formula.class);
                } else if (node.has("or")) {
                    formula = ctxt.readValue(node.get("or").traverse(p.getCodec()), Formula.class);
                } else if (node.has("not")) {
                    formula = ctxt.readValue(node.get("not").traverse(p.getCodec()), Formula.class);
                }
            }
        } catch (IOException e) {
            DeserializerUtils.handleDeserializationError(LOGGER, "Error processing formula for condition " + name, e);
        }

        // Set the formula if found
        if (formula != null) {
            condition.setValueFormula(formula);
        }

        // Register the condition for reference resolution
        registerElement(condition);

        return condition;
    }
}