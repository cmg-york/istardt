package com.example.objects;

/**
 * Equals operator implementation
 */
public class EQOperator extends OperatorDecorator {
    public EQOperator(Formula left, Formula right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String getFormula() {
        return "(" + left.getFormula() + " == " + right.getFormula() + ")";
    }
}