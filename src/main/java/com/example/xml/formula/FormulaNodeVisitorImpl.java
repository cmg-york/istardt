package com.example.xml.formula;

import com.example.objects.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Implementation of FormulaNodeVisitor for deserializing formulas.
 */
public class FormulaNodeVisitorImpl implements FormulaNodeVisitor {
    private static final Logger LOGGER = Logger.getLogger(FormulaNodeVisitorImpl.class.getName());

    private final JsonParser parser;
    private final DeserializationContext context;

    public FormulaNodeVisitorImpl(JsonParser parser, DeserializationContext context) {
        this.parser = parser;
        this.context = context;
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
        List<Formula> operands = collectNumericOperands(node);

        if (operands.size() >= 2) {
            Formula result = new PlusOperator(operands.get(0), operands.get(1));
            for (int i = 2; i < operands.size(); i++) {
                result = new PlusOperator(result, operands.get(i));
            }
            return result;
        } else if (operands.size() == 1) {
            return operands.get(0);
        }

        return Formula.createConstantFormula("0");
    }

    @Override
    public Formula visitSubtract(JsonNode node) throws IOException {
        if (node.has("left") && node.has("right")) {
            Formula left = visitOperand(node.get("left"));
            Formula right = visitOperand(node.get("right"));

            if (left != null && right != null) {
                return new MinusOperator(left, right);
            }
        }

        return Formula.createConstantFormula("0");
    }

    @Override
    public Formula visitMultiply(JsonNode node) throws IOException {
        List<Formula> operands = collectNumericOperands(node);

        if (operands.size() >= 2) {
            Formula result = new MultiplyOperator(operands.get(0), operands.get(1));
            for (int i = 2; i < operands.size(); i++) {
                result = new MultiplyOperator(result, operands.get(i));
            }
            return result;
        } else if (operands.size() == 1) {
            return operands.get(0);
        }

        return Formula.createConstantFormula("1");
    }

    @Override
    public Formula visitDivide(JsonNode node) throws IOException {
        if (node.has("left") && node.has("right")) {
            Formula left = visitOperand(node.get("left"));
            Formula right = visitOperand(node.get("right"));

            if (left != null && right != null) {
                // Since there's no DivideOperator defined, we create a custom representation
                return Formula.createConstantFormula("(" + left.getFormula() + " / " + right.getFormula() + ")");
            }
        }

        return Formula.createConstantFormula("1");
    }

    @Override
    public Formula visitGreaterThan(JsonNode node) throws IOException {
        if (node.has("left") && node.has("right")) {
            Formula left = visitOperand(node.get("left"));
            Formula right = visitOperand(node.get("right"));

            if (left != null && right != null) {
                return new GTOperator(left, right);
            }
        }

        return Formula.createBooleanFormula(false);
    }

    @Override
    public Formula visitAnd(JsonNode node) throws IOException {
        List<Formula> operands = collectBooleanOperands(node);

        if (operands.size() >= 2) {
            Formula result = new ANDOperator(operands.get(0), operands.get(1));
            for (int i = 2; i < operands.size(); i++) {
                result = new ANDOperator(result, operands.get(i));
            }
            return result;
        } else if (operands.size() == 1) {
            return operands.get(0);
        }

        return Formula.createBooleanFormula(true);
    }

    @Override
    public Formula visitOr(JsonNode node) throws IOException {
        List<Formula> operands = collectBooleanOperands(node);

        if (operands.size() >= 2) {
            Formula result = new OROperator(operands.get(0), operands.get(1));
            for (int i = 2; i < operands.size(); i++) {
                result = new OROperator(result, operands.get(i));
            }
            return result;
        } else if (operands.size() == 1) {
            return operands.get(0);
        }

        return Formula.createBooleanFormula(false);
    }

    @Override
    public Formula visitNot(JsonNode node) throws IOException {
        Formula operand = null;

        if (node.has("boolAtom")) {
            operand = visitBoolAtom(node.get("boolAtom"));
        } else if (node.has("boolConst")) {
            operand = visitBoolConst(node.get("boolConst"));
        } else if (node.has("and")) {
            operand = visitAnd(node.get("and"));
        } else if (node.has("or")) {
            operand = visitOr(node.get("or"));
        } else if (node.has("gt")) {
            operand = visitGreaterThan(node.get("gt"));
        }

        if (operand != null) {
            return new NOTOperator(operand);
        }

        return Formula.createBooleanFormula(false);
    }

    /**
     * Helper method to visit an operand node and return the corresponding Formula.
     */
    private Formula visitOperand(JsonNode node) throws IOException {
        if (node == null) {
            return null;
        }

        if (node.has("const")) {
            return visitConstant(node.get("const"));
        } else if (node.has("numAtom")) {
            return visitNumAtom(node.get("numAtom"));
        } else if (node.has("add")) {
            return visitAdd(node.get("add"));
        } else if (node.has("subtract")) {
            return visitSubtract(node.get("subtract"));
        } else if (node.has("multiply")) {
            return visitMultiply(node.get("multiply"));
        } else if (node.has("divide")) {
            return visitDivide(node.get("divide"));
        }

        return Formula.createConstantFormula("Unknown");
    }

    /**
     * Helper method to collect numeric operands from a node.
     */
    private List<Formula> collectNumericOperands(JsonNode node) throws IOException {
        List<Formula> operands = new ArrayList<>();

        // Process const operands
        if (node.has("const")) {
            addNodeToOperands(node.get("const"), operands, this::visitConstant);
        }

        // Process numAtom operands
        if (node.has("numAtom")) {
            addNodeToOperands(node.get("numAtom"), operands, this::visitNumAtom);
        }

        // Process nested add operations
        if (node.has("add")) {
            operands.add(visitAdd(node.get("add")));
        }

        // Process nested subtract operations
        if (node.has("subtract")) {
            operands.add(visitSubtract(node.get("subtract")));
        }

        // Process nested multiply operations
        if (node.has("multiply")) {
            operands.add(visitMultiply(node.get("multiply")));
        }

        // Process nested divide operations
        if (node.has("divide")) {
            operands.add(visitDivide(node.get("divide")));
        }

        return operands;
    }

    /**
     * Helper method to collect boolean operands from a node.
     */
    private List<Formula> collectBooleanOperands(JsonNode node) throws IOException {
        List<Formula> operands = new ArrayList<>();

        // Process boolConst operands
        if (node.has("boolConst")) {
            addNodeToOperands(node.get("boolConst"), operands, this::visitBoolConst);
        }

        // Process boolAtom operands
        if (node.has("boolAtom")) {
            addNodeToOperands(node.get("boolAtom"), operands, this::visitBoolAtom);
        }

        // Process nested and operations
        if (node.has("and")) {
            operands.add(visitAnd(node.get("and")));
        }

        // Process nested or operations
        if (node.has("or")) {
            operands.add(visitOr(node.get("or")));
        }

        // Process nested not operations
        if (node.has("not")) {
            operands.add(visitNot(node.get("not")));
        }

        // Process nested gt operations
        if (node.has("gt")) {
            operands.add(visitGreaterThan(node.get("gt")));
        }

        return operands;
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