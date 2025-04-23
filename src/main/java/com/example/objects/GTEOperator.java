package com.example.objects;

/**
 * Greater Than or Equal To operator implementation
 */
public class GTEOperator extends OperatorDecorator {

    public GTEOperator(Formula left, Formula right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String getFormula() {
        return "(" + left.getFormula() + " >= " + right.getFormula() + ")";
    }
}