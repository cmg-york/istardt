package com.example.xml.deserializers;

import com.example.objects.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Deserializer for Formula objects.
 * Handles the conversion of XML formula expressions to Formula object hierarchy.
 * Note that this doesn't extend BaseDeserializer since Formula doesn't extend Element.
 */
public class FormulaDeserializer extends StdDeserializer<Formula> {

    public FormulaDeserializer() {
        super(Formula.class);
    }

    @Override
    public Formula deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        // Check for different formula types and delegate to appropriate handler
        if (node.has("const")) {
            return Formula.createConstantFormula(node.get("const").asText());
        }

        if (node.has("numAtom")) {
            return Formula.createConstantFormula(node.get("numAtom").asText());
        }

        if (node.has("boolConst")) {
            return Formula.createBooleanFormula(Boolean.parseBoolean(node.get("boolConst").asText()));
        }

        if (node.has("boolAtom")) {
            return Formula.createConstantFormula(node.get("boolAtom").asText());
        }

        if (node.has("add")) {
            return deserializeAddFormula(node.get("add"));
        }

        if (node.has("subtract")) {
            return deserializeSubtractFormula(node.get("subtract"));
        }

        if (node.has("multiply")) {
            return deserializeMultiplyFormula(node.get("multiply"));
        }

        if (node.has("divide")) {
            return deserializeDivideFormula(node.get("divide"));
        }

        if (node.has("gt")) {
            return deserializeGtFormula(node.get("gt"));
        }

        if (node.has("and")) {
            return deserializeAndFormula(node.get("and"));
        }

        if (node.has("or")) {
            return deserializeOrFormula(node.get("or"));
        }

        if (node.has("not")) {
            return deserializeNotFormula(node.get("not"));
        }

        // Default case - return a simple constant formula
        return Formula.createConstantFormula("Unknown Formula");
    }

    /**
     * Extracts the name/ID attribute from a node.
     *
     * @param node The JSON node to extract from
     * @return The name/ID attribute, or null if not found
     */
    protected String getName(JsonNode node) {
        return node.has("name") ? node.get("name").asText() : null;
    }

    /**
     * Extracts the description attribute from a node.
     *
     * @param node The JSON node to extract from
     * @return The description attribute, or null if not found
     */
    protected String getDescription(JsonNode node) {
        return node.has("description") ? node.get("description").asText() : null;
    }

    /**
     * Extracts a list of string values from child nodes with a specific name.
     *
     * @param node The parent JSON node
     * @param childName The name of the child nodes
     * @return A list of string values
     */
    protected List<String> getStringList(JsonNode node, String childName) {
        List<String> result = new ArrayList<>();
        if (node.has(childName)) {
            JsonNode childNode = node.get(childName);
            if (childNode.isArray()) {
                for (JsonNode item : childNode) {
                    result.add(item.asText());
                }
            } else {
                result.add(childNode.asText());
            }
        }
        return result;
    }

    /**
     * Deserializes an addition formula.
     *
     * @param node The JSON node containing the add formula
     * @return The Formula object representing the addition
     * @throws IOException If there's an error during deserialization
     */
    private Formula deserializeAddFormula(JsonNode node) throws IOException {
        List<Formula> operands = new ArrayList<>();

        // Process const operands
        if (node.has("const")) {
            JsonNode constNodes = node.get("const");
            if (constNodes.isArray()) {
                for (JsonNode constNode : constNodes) {
                    operands.add(Formula.createConstantFormula(constNode.asText()));
                }
            } else {
                operands.add(Formula.createConstantFormula(constNodes.asText()));
            }
        }

        // Process numAtom operands
        if (node.has("numAtom")) {
            JsonNode atomNodes = node.get("numAtom");
            if (atomNodes.isArray()) {
                for (JsonNode atomNode : atomNodes) {
                    operands.add(Formula.createConstantFormula(atomNode.asText()));
                }
            } else {
                operands.add(Formula.createConstantFormula(atomNodes.asText()));
            }
        }

        // Process subtract operands
        if (node.has("subtract")) {
            operands.add(deserializeSubtractFormula(node.get("subtract")));
        }

        // Process multiply operands
        if (node.has("multiply")) {
            operands.add(deserializeMultiplyFormula(node.get("multiply")));
        }

        // Create the addition formula
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

    /**
     * Deserializes a subtraction formula.
     *
     * @param node The JSON node containing the subtract formula
     * @return The Formula object representing the subtraction
     * @throws IOException If there's an error during deserialization
     */
    private Formula deserializeSubtractFormula(JsonNode node) throws IOException {
        if (node.has("left") && node.has("right")) {
            Formula left = deserializeOperand(node.get("left"));
            Formula right = deserializeOperand(node.get("right"));

            if (left != null && right != null) {
                return new MinusOperator(left, right);
            }
        }

        return Formula.createConstantFormula("0");
    }

    /**
     * Deserializes a multiplication formula.
     *
     * @param node The JSON node containing the multiply formula
     * @return The Formula object representing the multiplication
     * @throws IOException If there's an error during deserialization
     */
    private Formula deserializeMultiplyFormula(JsonNode node) throws IOException {
        List<Formula> operands = new ArrayList<>();

        // Process const operands
        if (node.has("const")) {
            JsonNode constNodes = node.get("const");
            if (constNodes.isArray()) {
                for (JsonNode constNode : constNodes) {
                    operands.add(Formula.createConstantFormula(constNode.asText()));
                }
            } else {
                operands.add(Formula.createConstantFormula(constNodes.asText()));
            }
        }

        // Process numAtom operands
        if (node.has("numAtom")) {
            JsonNode atomNodes = node.get("numAtom");
            if (atomNodes.isArray()) {
                for (JsonNode atomNode : atomNodes) {
                    operands.add(Formula.createConstantFormula(atomNode.asText()));
                }
            } else {
                operands.add(Formula.createConstantFormula(atomNodes.asText()));
            }
        }

        // Create the multiplication formula
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

    /**
     * Deserializes a division formula.
     *
     * @param node The JSON node containing the divide formula
     * @return The Formula object representing the division
     * @throws IOException If there's an error during deserialization
     */
    private Formula deserializeDivideFormula(JsonNode node) throws IOException {
        if (node.has("left") && node.has("right")) {
            Formula left = deserializeOperand(node.get("left"));
            Formula right = deserializeOperand(node.get("right"));

            if (left != null && right != null) {
                // Since there's no DivideOperator in the original code, create a string representation
                return Formula.createConstantFormula("(" + left.getFormula() + " / " + right.getFormula() + ")");
            }
        }

        return Formula.createConstantFormula("1");
    }

    /**
     * Deserializes a greater-than formula.
     *
     * @param node The JSON node containing the gt formula
     * @return The Formula object representing the greater-than comparison
     * @throws IOException If there's an error during deserialization
     */
    private Formula deserializeGtFormula(JsonNode node) throws IOException {
        if (node.has("left") && node.has("right")) {
            Formula left = deserializeOperand(node.get("left"));
            Formula right = deserializeOperand(node.get("right"));

            if (left != null && right != null) {
                return new GTOperator(left, right);
            }
        }

        return Formula.createBooleanFormula(false);
    }

    /**
     * Deserializes an AND formula.
     *
     * @param node The JSON node containing the and formula
     * @return The Formula object representing the AND operation
     * @throws IOException If there's an error during deserialization
     */
    private Formula deserializeAndFormula(JsonNode node) throws IOException {
        List<Formula> operands = new ArrayList<>();

        // Process boolean atom operands
        if (node.has("boolAtom")) {
            JsonNode atomNodes = node.get("boolAtom");
            if (atomNodes.isArray()) {
                for (JsonNode atomNode : atomNodes) {
                    operands.add(Formula.createConstantFormula(atomNode.asText()));
                }
            } else {
                operands.add(Formula.createConstantFormula(atomNodes.asText()));
            }
        }

        // Process boolean constant operands
        if (node.has("boolConst")) {
            JsonNode constNodes = node.get("boolConst");
            if (constNodes.isArray()) {
                for (JsonNode constNode : constNodes) {
                    operands.add(Formula.createBooleanFormula(Boolean.parseBoolean(constNode.asText())));
                }
            } else {
                operands.add(Formula.createBooleanFormula(Boolean.parseBoolean(constNodes.asText())));
            }
        }

        // Process nested AND, OR, or NOT operands if they exist
        // (This would require deeper processing of child nodes)

        // Create the AND formula
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

    /**
     * Deserializes an OR formula.
     *
     * @param node The JSON node containing the or formula
     * @return The Formula object representing the OR operation
     * @throws IOException If there's an error during deserialization
     */
    private Formula deserializeOrFormula(JsonNode node) throws IOException {
        List<Formula> operands = new ArrayList<>();

        // Process boolean atom operands
        if (node.has("boolAtom")) {
            JsonNode atomNodes = node.get("boolAtom");
            if (atomNodes.isArray()) {
                for (JsonNode atomNode : atomNodes) {
                    operands.add(Formula.createConstantFormula(atomNode.asText()));
                }
            } else {
                operands.add(Formula.createConstantFormula(atomNodes.asText()));
            }
        }

        // Process boolean constant operands
        if (node.has("boolConst")) {
            JsonNode constNodes = node.get("boolConst");
            if (constNodes.isArray()) {
                for (JsonNode constNode : constNodes) {
                    operands.add(Formula.createBooleanFormula(Boolean.parseBoolean(constNode.asText())));
                }
            } else {
                operands.add(Formula.createBooleanFormula(Boolean.parseBoolean(constNodes.asText())));
            }
        }

        // Create the OR formula
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

    /**
     * Deserializes a NOT formula.
     *
     * @param node The JSON node containing the not formula
     * @return The Formula object representing the NOT operation
     * @throws IOException If there's an error during deserialization
     */
    private Formula deserializeNotFormula(JsonNode node) throws IOException {
        Formula operand = null;

        if (node.has("boolAtom")) {
            operand = Formula.createConstantFormula(node.get("boolAtom").asText());
        } else if (node.has("boolConst")) {
            operand = Formula.createBooleanFormula(Boolean.parseBoolean(node.get("boolConst").asText()));
        }

        if (operand != null) {
            return new NOTOperator(operand);
        }

        return Formula.createBooleanFormula(false);
    }

    /**
     * Deserializes a formula operand (left or right side of binary operations).
     *
     * @param node The JSON node containing the operand
     * @return The Formula object representing the operand
     * @throws IOException If there's an error during deserialization
     */
    private Formula deserializeOperand(JsonNode node) throws IOException {
        if (node == null) {
            return null;
        }

        if (node.has("const")) {
            return Formula.createConstantFormula(node.get("const").asText());
        } else if (node.has("numAtom")) {
            return Formula.createConstantFormula(node.get("numAtom").asText());
        }

        // If it's a more complex formula, deserialize based on content
        if (node.has("add")) {
            return deserializeAddFormula(node.get("add"));
        } else if (node.has("subtract")) {
            return deserializeSubtractFormula(node.get("subtract"));
        } else if (node.has("multiply")) {
            return deserializeMultiplyFormula(node.get("multiply"));
        } else if (node.has("divide")) {
            return deserializeDivideFormula(node.get("divide"));
        }

        return Formula.createConstantFormula("Unknown");
    }
}