package com.example.xml.util;

import com.example.objects.*;
import com.example.xml.dto.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory for creating Formula objects from DTOs.
 * Uses the Factory pattern to encapsulate complex object creation logic.
 */
public class FormulaFactory {

    /**
     * Creates a Formula from a PreBoxDto.
     *
     * @param preBoxDto The preBox DTO from XML
     * @return The created Formula
     */
    public static Formula createFromPreBox(PreBoxDto preBoxDto) {
        // Handle boolean constant
        if (preBoxDto.getBoolConst() != null) {
            return Formula.createBooleanFormula(Boolean.parseBoolean(preBoxDto.getBoolConst()));
        }

        // Handle boolean atom reference
        if (preBoxDto.getBoolAtom() != null) {
            return Formula.createConstantFormula(preBoxDto.getBoolAtom());
        }

        // Handle greater than comparison
        if (preBoxDto.getGt() != null) {
            GtDto gtDto = preBoxDto.getGt();

            // Create left formula
            Formula leftFormula = createFormulaFromOperand(gtDto.getLeft());

            // Create right formula
            Formula rightFormula = createFormulaFromOperand(gtDto.getRight());

            if (leftFormula != null && rightFormula != null) {
                return new GTOperator(leftFormula, rightFormula);
            }
        }

        // Handle AND operation
        if (preBoxDto.getAnd() != null) {
            return createAndFormula(preBoxDto.getAnd());
        }

        // Handle OR operation
        if (preBoxDto.getOr() != null) {
            return createOrFormula(preBoxDto.getOr());
        }

        // Handle NOT operation
        if (preBoxDto.getNot() != null) {
            return createNotFormula(preBoxDto.getNot());
        }

        // Default to a simple constant formula with the preBox name
        return Formula.createConstantFormula(preBoxDto.getName());
    }

    /**
     * Creates a Formula from an operand (LeftDto or RightDto).
     *
     * @param operandDto The operand DTO from XML
     * @return The created Formula
     */
    private static Formula createFormulaFromOperand(OperandDto operandDto) {
        if (operandDto == null) {
            return null;
        }

        if (operandDto.getConst() != null) {
            return Formula.createConstantFormula(operandDto.getConst());
        } else if (operandDto.getNumAtom() != null) {
            return Formula.createConstantFormula(operandDto.getNumAtom());
        }

        return null;
    }

    /**
     * Creates an AND formula from a BooleanAndDto.
     *
     * @param andDto The AND DTO from XML
     * @return The created Formula
     */
    private static Formula createAndFormula(BooleanAndDto andDto) {
        // This is a placeholder - actual implementation would extract boolean expressions
        // and combine them with AND operators
        List<Formula> operands = new ArrayList<>();

        // If there are boolAtoms
        if (andDto.getBoolAtom() != null && !andDto.getBoolAtom().isEmpty()) {
            for (String atom : andDto.getBoolAtom()) {
                operands.add(Formula.createConstantFormula(atom));
            }
        }

        // If there are boolConsts
        if (andDto.getBoolConst() != null && !andDto.getBoolConst().isEmpty()) {
            for (String constVal : andDto.getBoolConst()) {
                operands.add(Formula.createBooleanFormula(Boolean.parseBoolean(constVal)));
            }
        }

        // Combine operands with AND operators
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
     * Creates an OR formula from a BooleanOrDto.
     *
     * @param orDto The OR DTO from XML
     * @return The created Formula
     */
    private static Formula createOrFormula(BooleanOrDto orDto) {
        // This is a placeholder - actual implementation would extract boolean expressions
        // and combine them with OR operators
        List<Formula> operands = new ArrayList<>();

        // If there are boolAtoms
        if (orDto.getBoolAtom() != null && !orDto.getBoolAtom().isEmpty()) {
            for (String atom : orDto.getBoolAtom()) {
                operands.add(Formula.createConstantFormula(atom));
            }
        }

        // If there are boolConsts
        if (orDto.getBoolConst() != null && !orDto.getBoolConst().isEmpty()) {
            for (String constVal : orDto.getBoolConst()) {
                operands.add(Formula.createBooleanFormula(Boolean.parseBoolean(constVal)));
            }
        }

        // Combine operands with OR operators
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
     * Creates a NOT formula from a BooleanNotDto.
     *
     * @param notDto The NOT DTO from XML
     * @return The created Formula
     */
    private static Formula createNotFormula(BooleanNotDto notDto) {
        // This is a placeholder - actual implementation would extract the boolean expression
        // and wrap it with a NOT operator
        Formula operand = null;

        if (notDto.getBoolAtom() != null) {
            operand = Formula.createConstantFormula(notDto.getBoolAtom());
        } else if (notDto.getBoolConst() != null) {
            operand = Formula.createBooleanFormula(Boolean.parseBoolean(notDto.getBoolConst()));
        }

        if (operand != null) {
            return new NOTOperator(operand);
        }

        return Formula.createBooleanFormula(false);
    }

    /**
     * Creates a Formula from a FormulaDto.
     *
     * @param formulaDto The formula DTO from XML
     * @return The created Formula
     */
    public static Formula createFromFormula(FormulaDto formulaDto) {
        if (formulaDto == null) {
            return null;
        }

        // Handle constant
        if (formulaDto.getConst() != null) {
            return Formula.createConstantFormula(formulaDto.getConst());
        }

        // Handle numAtom reference
        if (formulaDto.getNumAtom() != null) {
            return Formula.createConstantFormula(formulaDto.getNumAtom());
        }

        // Handle addition
        if (formulaDto.getAdd() != null) {
            return createAddFormula(formulaDto.getAdd());
        }

        // Handle subtraction
        if (formulaDto.getSubtract() != null) {
            return createSubtractFormula(formulaDto.getSubtract());
        }

        // Handle multiplication
        if (formulaDto.getMultiply() != null) {
            return createMultiplyFormula(formulaDto.getMultiply());
        }

        // Handle division
        if (formulaDto.getDivide() != null) {
            return createDivideFormula(formulaDto.getDivide());
        }

        // Default to a simple constant formula
        return Formula.createConstantFormula("Default Formula");
    }

    /**
     * Creates an addition formula from AddDto.
     *
     * @param addDto The addition DTO from XML
     * @return The created Formula
     */
    private static Formula createAddFormula(AddDto addDto) {
        List<Formula> formulas = new ArrayList<>();

        // Add constants if present
        if (addDto.getConst() != null) {
            for (String constVal : addDto.getConst()) {
                formulas.add(Formula.createConstantFormula(constVal));
            }
        }

        // Add numeric atoms if present
        if (addDto.getNumAtom() != null) {
            for (String atomName : addDto.getNumAtom()) {
                formulas.add(Formula.createConstantFormula(atomName));
            }
        }

        // Add subtraction if present
        if (addDto.getSubtract() != null) {
            formulas.add(createSubtractFormula(addDto.getSubtract()));
        }

        // Add multiplication if present
        if (addDto.getMultiply() != null) {
            formulas.add(createMultiplyFormula(addDto.getMultiply()));
        }

        // Combine all formulas with plus operators
        if (formulas.size() >= 2) {
            Formula result = new PlusOperator(formulas.get(0), formulas.get(1));
            for (int i = 2; i < formulas.size(); i++) {
                result = new PlusOperator(result, formulas.get(i));
            }
            return result;
        } else if (formulas.size() == 1) {
            return formulas.get(0);
        }

        return Formula.createConstantFormula("0");
    }

    /**
     * Creates a subtraction formula from SubtractDto.
     *
     * @param subtractDto The subtraction DTO from XML
     * @return The created Formula
     */
    private static Formula createSubtractFormula(SubtractDto subtractDto) {
        Formula leftFormula = createFormulaFromOperand(subtractDto.getLeft());
        Formula rightFormula = createFormulaFromOperand(subtractDto.getRight());

        if (leftFormula != null && rightFormula != null) {
            return new MinusOperator(leftFormula, rightFormula);
        }

        return Formula.createConstantFormula("0");
    }

    /**
     * Creates a multiplication formula from MultiplyDto.
     *
     * @param multiplyDto The multiplication DTO from XML
     * @return The created Formula
     */
    private static Formula createMultiplyFormula(MultiplyDto multiplyDto) {
        List<Formula> formulas = new ArrayList<>();

        // Add constants if present
        if (multiplyDto.getConst() != null) {
            for (String constVal : multiplyDto.getConst()) {
                formulas.add(Formula.createConstantFormula(constVal));
            }
        }

        // Add numeric atoms if present
        if (multiplyDto.getNumAtom() != null) {
            for (String atomName : multiplyDto.getNumAtom()) {
                formulas.add(Formula.createConstantFormula(atomName));
            }
        }

        // Combine all formulas with multiply operators
        if (formulas.size() >= 2) {
            Formula result = new MultiplyOperator(formulas.get(0), formulas.get(1));
            for (int i = 2; i < formulas.size(); i++) {
                result = new MultiplyOperator(result, formulas.get(i));
            }
            return result;
        } else if (formulas.size() == 1) {
            return formulas.get(0);
        }

        return Formula.createConstantFormula("1");
    }

    /**
     * Creates a division formula from DivideDto.
     *
     * @param divideDto The division DTO from XML
     * @return The created Formula
     */
    private static Formula createDivideFormula(DivideDto divideDto) {
        // This is a placeholder implementation
        Formula leftFormula = createFormulaFromOperand(divideDto.getLeft());
        Formula rightFormula = createFormulaFromOperand(divideDto.getRight());

        if (leftFormula != null && rightFormula != null) {
            // Note: There's no DivideOperator in the provided code, so this is just a placeholder
            // You would need to implement a DivideOperator class that extends OperatorDecorator
            return Formula.createConstantFormula("(" + leftFormula.getFormula() + " / " + rightFormula.getFormula() + ")");
        }

        return Formula.createConstantFormula("1");
    }
}