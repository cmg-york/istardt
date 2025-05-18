package ca.yorku.cmg.istardt.xmlparser.xml.deserializers;

import ca.yorku.cmg.istardt.xmlparser.xml.formula.FormulaNodeVisitor;
import ca.yorku.cmg.istardt.xmlparser.xml.formula.FormulaNodeVisitorImpl;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import ca.yorku.cmg.istardt.xmlparser.objects.Formula;

import java.io.IOException;
import java.util.logging.Level;
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
            LOGGER.info("Deserializing formula node: " + node.toString());

            // Create visitor to handle the formula node
            FormulaNodeVisitor visitor = new FormulaNodeVisitorImpl(p, ctxt);

            // Check for each formula type and delegate to the appropriate visitor
            if (node.has("numConst")) {
                LOGGER.info("Found constant formula: " + node.get("numConst").asText());
                return visitor.visitNumConst(node.get("numConst"));
            }

            if (node.has("boolConst")) {
                LOGGER.info("Found boolean constant formula: " + node.get("boolConst").asText());
                return visitor.visitBoolConst(node.get("boolConst"));
            }

            if (node.has("predicateID")) {
                LOGGER.info("Found predicate ID atom: " + node.get("predicateID").asText());
                return visitor.visitPredicateID(node.get("predicateID"));
            }

            if (node.has("goalID")) {
                LOGGER.info("Found goal ID atom: " + node.get("goalID").asText());
                return visitor.visitGoalID(node.get("goalID"));
            }

            if (node.has("taskID")) {
                LOGGER.info("Found task ID atom: " + node.get("taskID").asText());
                return visitor.visitTaskID(node.get("taskID"));
            }

            if (node.has("variableID")) {
                LOGGER.info("Found variable ID atom: " + node.get("variableID").asText());
                return visitor.visitVariableID(node.get("variableID"));
            }

            if (node.has("qualID")) {
                LOGGER.info("Found quality ID atom: " + node.get("qualID").asText());
                return visitor.visitQualID(node.get("qualID"));
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

            if (node.has("previous")) {
                LOGGER.info("Found previous operator formula");
                return visitor.visitPrevious(node.get("previous"));
            }

            if (node.has("negate")) {
                LOGGER.info("Found negate operator formula");
                return visitor.visitNegate(node.get("negate"));
            }

            if (node.has("gt")) {
                LOGGER.info("Found greater than operator formula");
                return visitor.visitGreaterThan(node.get("gt"));
            }

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

            // Default case
            LOGGER.warning("Unknown formula type encountered: " + node.toString());
            return Formula.createConstantFormula("Unknown Formula");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error deserializing formula: " + e.getMessage(), e);
            throw e;
        }
    }
}