package com.example.xml.deserializers;

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

        // Extract common attributes (id, name, description, atom)
        extractCommonAttributes(condition, node);

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
            DeserializerUtils.handleDeserializationError(LOGGER,
                    "Error processing formula for condition " + condition.getAtom().getTitleText(), e);
        }

        // Set the formula if found
        if (formula != null) {
            condition.setValueFormula(formula);
        }

        return condition;
    }
}