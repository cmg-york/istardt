package com.example.objects;

/**
 * Less Than or Equal To operator implementation
 */
public class LTEOperator extends OperatorDecorator {

    public LTEOperator(Formula left, Formula right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String getFormula() {
        return "(" + left.getFormula() + " <= " + right.getFormula() + ")";
    }
}