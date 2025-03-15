package com.example.xml.deserializers;

import com.example.objects.*;
import com.example.xml.formula.FormulaNodeVisitor;
import com.example.xml.formula.FormulaNodeVisitorImpl;
import com.example.xml.utils.DeserializerUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Deserializer for Formula objects using the Visitor pattern.
 */
public class FormulaDeserializer extends StdDeserializer<Formula> {
    private static final Logger LOGGER = Logger.getLogger(FormulaDeserializer.class.getName());

    public FormulaDeserializer() {
        super(Formula.class);
    }

    @Override
    public Formula deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        try {
            JsonNode node = p.getCodec().readTree(p);

            // Create visitor to handle the formula node
            FormulaNodeVisitor visitor = new FormulaNodeVisitorImpl(p, ctxt);

            // Dispatch to appropriate visitor method based on node content
            if (node.has("const")) {
                return visitor.visitConstant(node.get("const"));
            }

            if (node.has("numAtom")) {
                return visitor.visitNumAtom(node.get("numAtom"));
            }

            if (node.has("boolConst")) {
                return visitor.visitBoolConst(node.get("boolConst"));
            }

            if (node.has("boolAtom")) {
                return visitor.visitBoolAtom(node.get("boolAtom"));
            }

            if (node.has("add")) {
                return visitor.visitAdd(node.get("add"));
            }

            if (node.has("subtract")) {
                return visitor.visitSubtract(node.get("subtract"));
            }

            if (node.has("multiply")) {
                return visitor.visitMultiply(node.get("multiply"));
            }

            if (node.has("divide")) {
                return visitor.visitDivide(node.get("divide"));
            }

            if (node.has("gt")) {
                return visitor.visitGreaterThan(node.get("gt"));
            }

            if (node.has("and")) {
                return visitor.visitAnd(node.get("and"));
            }

            if (node.has("or")) {
                return visitor.visitOr(node.get("or"));
            }

            if (node.has("not")) {
                return visitor.visitNot(node.get("not"));
            }

            // Default case - return a simple constant formula
            LOGGER.warning("Unknown formula type encountered: " + node.toString());
            return Formula.createConstantFormula("Unknown Formula");
        } catch (IOException e) {
            DeserializerUtils.handleDeserializationError(LOGGER, "Error deserializing formula", e);
            return null; // This line won't be reached
        }
    }
}