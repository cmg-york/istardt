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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Deserializer for Formula objects using the Visitor pattern.
 * Enhanced with detailed debug logging.
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
            LOGGER.info("Deserializing formula node: " + node.toString());

            // Create visitor to handle the formula node
            FormulaNodeVisitor visitor = new FormulaNodeVisitorImpl(p, ctxt);

            // Check for each formula type and delegate to the appropriate visitor
            if (node.has("const")) {
                LOGGER.info("Found constant formula: " + node.get("const").asText());
                return visitor.visitConstant(node.get("const"));
            }

            if (node.has("numAtom")) {
                LOGGER.info("Found numeric atom formula: " + node.get("numAtom").asText());
                return visitor.visitNumAtom(node.get("numAtom"));
            }

            if (node.has("boolConst")) {
                LOGGER.info("Found boolean constant formula: " + node.get("boolConst").asText());
                return visitor.visitBoolConst(node.get("boolConst"));
            }

            if (node.has("boolAtom")) {
                LOGGER.info("Found boolean atom formula: " + node.get("boolAtom").asText());
                return visitor.visitBoolAtom(node.get("boolAtom"));
            }

            if (node.has("add")) {
                LOGGER.info("Found add operator formula");
                return visitor.visitAdd(node.get("add"));
            }

            if (node.has("subtract")) {
                LOGGER.info("Found subtract operator formula");
                return visitor.visitSubtract(node.get("subtract"));
            }

            if (node.has("multiply")) {
                LOGGER.info("Found multiply operator formula");
                return visitor.visitMultiply(node.get("multiply"));
            }

            if (node.has("divide")) {
                LOGGER.info("Found divide operator formula");
                return visitor.visitDivide(node.get("divide"));
            }

            if (node.has("gt")) {
                LOGGER.info("Found greater than operator formula");
                return visitor.visitGreaterThan(node.get("gt"));
            }

            // New operators
            if (node.has("gte")) {
                LOGGER.info("Found greater than or equals operator formula");
                return visitor.visitGreaterThanEquals(node.get("gte"));
            }

            if (node.has("lt")) {
                LOGGER.info("Found less than operator formula");
                return visitor.visitLessThan(node.get("lt"));
            }

            if (node.has("lte")) {
                LOGGER.info("Found less than or equals operator formula");
                return visitor.visitLessThanEquals(node.get("lte"));
            }

            if (node.has("eq")) {
                LOGGER.info("Found equals operator formula");
                return visitor.visitEquals(node.get("eq"));
            }

            if (node.has("neq")) {
                LOGGER.info("Found not equals operator formula");
                return visitor.visitNotEquals(node.get("neq"));
            }

            if (node.has("and")) {
                LOGGER.info("Found AND operator formula");
                return visitor.visitAnd(node.get("and"));
            }

            if (node.has("or")) {
                LOGGER.info("Found OR operator formula");
                return visitor.visitOr(node.get("or"));
            }

            if (node.has("not")) {
                LOGGER.info("Found NOT operator formula");
                return visitor.visitNot(node.get("not"));
            }

            // Default case - return a simple constant formula
            LOGGER.warning("Unknown formula type encountered: " + node.toString());
            return Formula.createConstantFormula("Unknown Formula");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error deserializing formula: " + e.getMessage(), e);
            throw e;
        }
    }
}