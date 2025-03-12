package com.example.objects;

/**
 * Implementation of the Formula abstract class with necessary methods
 */
public abstract class Formula {
    public abstract String getFormula();

    // Factory methods to create different formula types

    public static Formula createConstantFormula(final String value) {
        return new Formula() {
            @Override
            public String getFormula() {
                return value;
            }
        };
    }

    public static Formula createBooleanFormula(final boolean value) {
        return new Formula() {
            @Override
            public String getFormula() {
                return String.valueOf(value);
            }
        };
    }
}

/**
 * Factory for creating formulas from XML expressions
 */
class FormulaFactory {

    public static Formula createFromConstant(String value) {
        try {
            // Try to parse as a number
            double numValue = Double.parseDouble(value);
            return Formula.createConstantFormula(value);
        } catch (NumberFormatException e) {
            // It's not a number, treat as a string
            return Formula.createConstantFormula(value);
        }
    }

    public static Formula createFromBoolean(boolean value) {
        return Formula.createBooleanFormula(value);
    }

    public static Formula createGTFormula(String left, String right) {
        return new GTOperator(createFromConstant(left), createFromConstant(right));
    }

    public static Formula createAndFormula(Formula left, Formula right) {
        return new ANDOperator(left, right);
    }

    public static Formula createOrFormula(Formula left, Formula right) {
        return new OROperator(left, right);
    }

    public static Formula createNotFormula(Formula formula) {
        return new NOTOperator(formula);
    }

    public static Formula createPlusFormula(Formula left, Formula right) {
        return new PlusOperator(left, right);
    }

    public static Formula createMinusFormula(Formula left, Formula right) {
        return new MinusOperator(left, right);
    }

    public static Formula createMultiplyFormula(Formula left, Formula right) {
        return new MultiplyOperator(left, right);
    }
}