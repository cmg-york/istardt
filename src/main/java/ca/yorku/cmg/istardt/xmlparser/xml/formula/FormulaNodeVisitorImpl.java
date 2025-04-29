package ca.yorku.cmg.istardt.xmlparser.xml.formula;

import ca.yorku.cmg.istardt.xmlparser.objects.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.logging.Logger;

/**
 * Refactored implementation of FormulaNodeVisitor for deserializing formulas.
 * Consolidates duplicate code into helper methods.
 */
public class FormulaNodeVisitorImpl implements FormulaNodeVisitor {
    private static final Logger LOGGER = Logger.getLogger(FormulaNodeVisitorImpl.class.getName());

    private static final String[] NUMERIC_OPERAND_TYPES = {
            "const", "numAtom", "add", "subtract", "multiply", "divide", "negate", "previous"
    };

    private static final String[] BOOLEAN_OPERAND_TYPES = {
            "boolConst", "boolAtom", "and", "or", "not", "gt", "gte", "lt", "lte", "eq", "neq", "previous"
    };

    private final JsonParser parser;
    private final DeserializationContext context;

    // Maps to match node types to visitor methods
    private final Map<String, OperandVisitor> operandVisitors = new HashMap<>();

    public FormulaNodeVisitorImpl(JsonParser parser, DeserializationContext context) {
        this.parser = parser;
        this.context = context;
        initializeOperandVisitors();
    }

    /**
     * Initialize the map of operand visitors
     */
    private void initializeOperandVisitors() {
        operandVisitors.put("const", this::visitConstant);
        operandVisitors.put("numAtom", this::visitNumAtom);
        operandVisitors.put("boolConst", this::visitBoolConst);
        operandVisitors.put("boolAtom", this::visitBoolAtom);
        operandVisitors.put("add", this::visitAdd);
        operandVisitors.put("subtract", this::visitSubtract);
        operandVisitors.put("multiply", this::visitMultiply);
        operandVisitors.put("divide", this::visitDivide);
        operandVisitors.put("gt", this::visitGreaterThan);
        operandVisitors.put("gte", this::visitGreaterThanEquals);
        operandVisitors.put("lt", this::visitLessThan);
        operandVisitors.put("lte", this::visitLessThanEquals);
        operandVisitors.put("eq", this::visitEquals);
        operandVisitors.put("neq", this::visitNotEquals);
        operandVisitors.put("and", this::visitAnd);
        operandVisitors.put("or", this::visitOr);
        operandVisitors.put("not", this::visitNot);
        operandVisitors.put("previous", this::visitPrevious);
        operandVisitors.put("negate", this::visitNegate);
    }

    @Override
    public Formula visitConstant(JsonNode node) {
        return Formula.createConstantFormula(node.asText());
    }

    @Override
    public Formula visitNumAtom(JsonNode node) {
        return Formula.createConstantFormula(node.asText());
    }

    @Override
    public Formula visitBoolConst(JsonNode node) {
        return Formula.createBooleanFormula(Boolean.parseBoolean(node.asText()));
    }

    @Override
    public Formula visitBoolAtom(JsonNode node) {
        return Formula.createConstantFormula(node.asText());
    }

    @Override
    public Formula visitAdd(JsonNode node) throws IOException {
        return visitMultiOperator(node, PlusOperator::new, Formula.createConstantFormula("0"),
                this::collectNumericOperands);
    }

    @Override
    public Formula visitSubtract(JsonNode node) throws IOException {
        return visitBinaryOperator(node, MinusOperator::new, Formula.createConstantFormula("0"));
    }

    @Override
    public Formula visitMultiply(JsonNode node) throws IOException {
        return visitMultiOperator(node, MultiplyOperator::new, Formula.createConstantFormula("1"),
                this::collectNumericOperands);
    }

    @Override
    public Formula visitDivide(JsonNode node) throws IOException {
        return visitBinaryOperator(node, DivideOperator::new, Formula.createConstantFormula("1"));
    }

    @Override
    public Formula visitPrevious(JsonNode node) {
        // Handle both boolean and numeric previous references
        if (node.has("boolAtom")) {
            Formula formula = visitBoolAtom(node.get("boolAtom"));
            return new PreviousOperator(formula);
        } else if (node.has("numAtom")) {
            Formula formula = visitNumAtom(node.get("numAtom"));
            return new PreviousOperator(formula);
        }

        LOGGER.warning("Previous node has neither boolAtom nor numAtom");
        return Formula.createConstantFormula("Unknown Previous");
    }

    @Override
    public Formula visitNegate(JsonNode node) throws IOException {
        // Process the first child element found
        for (Map.Entry<String, OperandVisitor> entry : operandVisitors.entrySet()) {
            String type = entry.getKey();
            if (node.has(type)) {
                Formula formula = visitOperandByType(type, node.get(type));
                return new NegateOperator(formula);
            }
        }

        LOGGER.warning("No valid operand found in negate operation");
        return Formula.createConstantFormula("0");
    }

    @Override
    public Formula visitGreaterThan(JsonNode node) throws IOException {
        return visitBinaryOperator(node, GTOperator::new, Formula.createBooleanFormula(false));
    }

    @Override
    public Formula visitLessThan(JsonNode node) throws IOException {
        return visitBinaryOperator(node, LTOperator::new, Formula.createBooleanFormula(false));
    }

    @Override
    public Formula visitLessThanEquals(JsonNode node) throws IOException {
        return visitBinaryOperator(node, LTEOperator::new, Formula.createBooleanFormula(false));
    }

    @Override
    public Formula visitGreaterThanEquals(JsonNode node) throws IOException {
        return visitBinaryOperator(node, GTEOperator::new, Formula.createBooleanFormula(false));
    }

    @Override
    public Formula visitEquals(JsonNode node) throws IOException {
        return visitBinaryOperator(node, EQOperator::new, Formula.createBooleanFormula(false));
    }

    @Override
    public Formula visitNotEquals(JsonNode node) throws IOException {
        return visitBinaryOperator(node, NEQOperator::new, Formula.createBooleanFormula(false));
    }

    @Override
    public Formula visitAnd(JsonNode node) throws IOException {
        return visitMultiOperator(node, ANDOperator::new, Formula.createBooleanFormula(true),
                this::collectBooleanOperands);
    }

    @Override
    public Formula visitOr(JsonNode node) throws IOException {
        return visitMultiOperator(node, OROperator::new, Formula.createBooleanFormula(false),
                this::collectBooleanOperands);
    }

    private List<Formula> collectOperands(JsonNode node, String[] operandTypes) throws IOException {
        List<Formula> operands = new ArrayList<>();

        for (String type : operandTypes) {
            if (node.has(type)) {
                if (type.equals("const") || type.equals("numAtom") ||
                        type.equals("boolConst") || type.equals("boolAtom")) {
                    addNodeToOperands(node.get(type), operands,
                            n -> {
                                try {
                                    return visitOperandByType(type, n);
                                } catch (IOException e) {
                                    LOGGER.severe("Error processing operand: " + e.getMessage());
                                    return null;
                                }
                            });
                } else {
                    operands.add(visitOperandByType(type, node.get(type)));
                }
            }
        }

        return operands;
    }

    @Override
    public Formula visitNot(JsonNode node) throws IOException {
        for (String type : BOOLEAN_OPERAND_TYPES) {
            if (node.has(type)) {
                Formula operand = visitOperandByType(type, node.get(type));
                if (operand != null) {
                    return new NOTOperator(operand);
                }
            }
        }

        return Formula.createBooleanFormula(false);
    }

    /**
     * Helper method for binary operators that take left and right operands.
     * Consolidates the duplicated code from multiple visit methods.
     *
     * @param node The JSON node containing the operator data
     * @param operatorFactory Function to create the specific operator type
     * @param defaultValue Default value to return if operands are missing
     * @return The formula representing the binary operation
     */
    private Formula visitBinaryOperator(
            JsonNode node,
            BiFunction<Formula, Formula, Formula> operatorFactory,
            Formula defaultValue) throws IOException {
        if (node.has("left") && node.has("right")) {
            Formula left = visitOperand(node.get("left"));
            Formula right = visitOperand(node.get("right"));

            if (left != null && right != null) {
                return operatorFactory.apply(left, right);
            }
        }
        return defaultValue;
    }

    /**
     * Helper method for operators that can take multiple operands (add, multiply, and, or).
     * Consolidates the duplicated code from multiple visit methods.
     *
     * @param node The JSON node containing the operator data
     * @param operatorFactory Function to create the specific operator type
     * @param defaultValue Default value to return if no operands are found
     * @param operandsCollector Function to collect operands
     * @return The formula representing the operation with multiple operands
     */
    private Formula visitMultiOperator(
            JsonNode node,
            BiFunction<Formula, Formula, Formula> operatorFactory,
            Formula defaultValue,
            OperandsCollector operandsCollector) throws IOException {

        List<Formula> operands = operandsCollector.collectOperands(node);

        if (operands.size() >= 2) {
            Formula result = operatorFactory.apply(operands.get(0), operands.get(1));
            for (int i = 2; i < operands.size(); i++) {
                result = operatorFactory.apply(result, operands.get(i));
            }
            return result;
        } else if (operands.size() == 1) {
            return operands.get(0);
        }

        return defaultValue;
    }

    /**
     * Interface for operand visitors
     */
    @FunctionalInterface
    private interface OperandVisitor {
        Formula visit(JsonNode node) throws IOException;
    }

    /**
     * Interface for operands collectors
     */
    @FunctionalInterface
    private interface OperandsCollector {
        List<Formula> collectOperands(JsonNode node) throws IOException;
    }

    /**
     * Visits a child operand based on its type.
     */
    private Formula visitOperandByType(String type, JsonNode node) throws IOException {
        OperandVisitor visitor = operandVisitors.get(type);
        if (visitor != null) {
            return visitor.visit(node);
        }
        return null;
    }

    /**
     * Helper method to visit an operand node and return the corresponding Formula.
     */
    private Formula visitOperand(JsonNode node) throws IOException {
        if (node == null) {
            return null;
        }

        for (Map.Entry<String, OperandVisitor> entry : operandVisitors.entrySet()) {
            String type = entry.getKey();
            if (node.has(type)) {
                return visitOperandByType(type, node.get(type));
            }
        }

        return Formula.createConstantFormula("Unknown");
    }

    /**
     * Helper method to collect numeric operands from a node.
     */
    private List<Formula> collectNumericOperands(JsonNode node) throws IOException {
        return collectOperands(node, NUMERIC_OPERAND_TYPES);
    }

    /**
     * Helper method to collect boolean operands from a node.
     */
    private List<Formula> collectBooleanOperands(JsonNode node) throws IOException {
        return collectOperands(node, BOOLEAN_OPERAND_TYPES);
    }

    /**
     * Helper to add one or more nodes to the operands list using a visitor method
     */
    private void addNodeToOperands(JsonNode node, List<Formula> operands,
                                   java.util.function.Function<JsonNode, Formula> visitor) {
        if (node.isArray()) {
            for (JsonNode item : node) {
                Formula formula = visitor.apply(item);
                if (formula != null) {
                    operands.add(formula);
                }
            }
        } else {
            Formula formula = visitor.apply(node);
            if (formula != null) {
                operands.add(formula);
            }
        }
    }
}