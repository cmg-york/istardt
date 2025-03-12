package com.example.objects;

/**
 * Plus operator implementation
 */
public class PlusOperator extends OperatorDecorator {

    public PlusOperator(Formula left, Formula right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String getFormula() {
        return "(" + left.getFormula() + " + " + right.getFormula() + ")";
    }
}


