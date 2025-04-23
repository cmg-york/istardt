package com.example.objects;

/**
 * Less Than operator implementation
 */
public class LTOperator extends OperatorDecorator {
    public LTOperator(Formula left, Formula right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String getFormula() {
        return "(" + left.getFormula() + " < " + right.getFormula() + ")";
    }
}