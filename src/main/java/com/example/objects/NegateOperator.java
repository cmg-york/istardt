package com.example.objects;

/**
 * Negate operator implementation
 * Represents numeric negation of a formula
 */
public class NegateOperator extends OperatorDecorator {
    public NegateOperator(Formula formula) {
        this.left = formula;
        this.right = null;
    }

    @Override
    public String getFormula() {
        return "-(" + left.getFormula() + ")";
    }
}