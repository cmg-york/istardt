package ca.yorku.cmg.istardt.xmlparser.xml.deserializers;

import ca.yorku.cmg.istardt.xmlparser.xml.formula.FormulaNodeVisitor;
import ca.yorku.cmg.istardt.xmlparser.xml.formula.FormulaNodeVisitorImpl;
import ca.yorku.cmg.istardt.xmlparser.xml.utils.CustomLogger;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import ca.yorku.cmg.istardt.xmlparser.objects.Formula;

import java.io.IOException;
import java.util.logging.Level;

/**
 * Deserializer for Formula objects using the Visitor pattern.
 */
public class FormulaDeserializer extends StdDeserializer<Formula> {
    private static final CustomLogger LOGGER = CustomLogger.getInstance();

    public FormulaDeserializer() {
        super(Formula.class);
    }

    @Override
    public Formula deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        try {
            JsonNode node = p.getCodec().readTree(p);
            LOGGER.info(getClass(), "Deserializing formula node: " + node.toString());

            // Create visitor to handle the formula node
            FormulaNodeVisitor visitor = new FormulaNodeVisitorImpl(p, ctxt);

            // Check for each formula type and delegate to the appropriate visitor
            if (node.has("numConst")) {
                LOGGER.info(getClass(), "Found constant formula: " + node.get("numConst").asText());
                return visitor.visitNumConst(node.get("numConst"));
            }

            if (node.has("boolConst")) {
                LOGGER.info(getClass(), "Found boolean constant formula: " + node.get("boolConst").asText());
                return visitor.visitBoolConst(node.get("boolConst"));
            }

            if (node.has("predicateID")) {
                LOGGER.info(getClass(), "Found predicate ID atom: " + node.get("predicateID").asText());
                return visitor.visitPredicateID(node.get("predicateID"));
            }

            if (node.has("goalID")) {
                LOGGER.info(getClass(), "Found goal ID atom: " + node.get("goalID").asText());
                return visitor.visitGoalID(node.get("goalID"));
            }

            if (node.has("taskID")) {
                LOGGER.info(getClass(), "Found task ID atom: " + node.get("taskID").asText());
                return visitor.visitTaskID(node.get("taskID"));
            }

            if (node.has("variableID")) {
                LOGGER.info(getClass(), "Found variable ID atom: " + node.get("variableID").asText());
                return visitor.visitVariableID(node.get("variableID"));
            }

            if (node.has("qualID")) {
                LOGGER.info(getClass(), "Found quality ID atom: " + node.get("qualID").asText());
                return visitor.visitQualID(node.get("qualID"));
            }

            if (node.has("effectID")) {
                LOGGER.info(getClass(), "Found effect ID atom: " + node.get("effectID").asText());
                return visitor.visitEffectID(node.get("effectID"));
            }

            if (node.has("conditionID")) {
                LOGGER.info(getClass(), "Found condition ID atom: " + node.get("conditionID").asText());
                return visitor.visitConditionID(node.get("conditionID"));
            }

            if (node.has("add")) {
                LOGGER.info(getClass(), "Found add operator formula");
                return visitor.visitAdd(node.get("add"));
            }

            if (node.has("subtract")) {
                LOGGER.info(getClass(), "Found subtract operator formula");
                return visitor.visitSubtract(node.get("subtract"));
            }

            if (node.has("multiply")) {
                LOGGER.info(getClass(), "Found multiply operator formula");
                return visitor.visitMultiply(node.get("multiply"));
            }

            if (node.has("divide")) {
                LOGGER.info(getClass(), "Found divide operator formula");
                return visitor.visitDivide(node.get("divide"));
            }

            if (node.has("previous")) {
                LOGGER.info(getClass(), "Found previous operator formula");
                return visitor.visitPrevious(node.get("previous"));
            }

            if (node.has("negate")) {
                LOGGER.info("Found negate operator formula");
                return visitor.visitNegate(node.get("negate"));
            }

            if (node.has("gt")) {
                LOGGER.info(getClass(), "Found greater than operator formula");
                return visitor.visitGreaterThan(node.get("gt"));
            }

            if (node.has("gte")) {
                LOGGER.info(getClass(), "Found greater than or equals operator formula");
                return visitor.visitGreaterThanEquals(node.get("gte"));
            }

            if (node.has("lt")) {
                LOGGER.info(getClass(), "Found less than operator formula");
                return visitor.visitLessThan(node.get("lt"));
            }

            if (node.has("lte")) {
                LOGGER.info(getClass(), "Found less than or equals operator formula");
                return visitor.visitLessThanEquals(node.get("lte"));
            }

            if (node.has("eq")) {
                LOGGER.info(getClass(), "Found equals operator formula");
                return visitor.visitEquals(node.get("eq"));
            }

            if (node.has("neq")) {
                LOGGER.info(getClass(), "Found not equals operator formula");
                return visitor.visitNotEquals(node.get("neq"));
            }

            if (node.has("and")) {
                LOGGER.info(getClass(), "Found AND operator formula");
                return visitor.visitAnd(node.get("and"));
            }

            if (node.has("or")) {
                LOGGER.info(getClass(), "Found OR operator formula");
                return visitor.visitOr(node.get("or"));
            }

            if (node.has("not")) {
                LOGGER.info(getClass(), "Found NOT operator formula");
                return visitor.visitNot(node.get("not"));
            }

            // Default case
            LOGGER.warning(getClass(),"Unknown formula type encountered: " + node);
            return Formula.createConstantFormula("Unknown Formula");
        } catch (IOException e) {
            LOGGER.error(getClass(), "Error deserializing formula: " + e.getMessage(), e);
            throw e;
        }
    }
}