package com.example.objects;


/**
 * OR operator implementation
 */
public class OROperator extends OperatorDecorator {

    public OROperator(Formula left, Formula right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String getFormula() {
        return "(" + left.getFormula() + " OR " + right.getFormula() + ")";
    }
}
