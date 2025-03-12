package com.example.objects;
/**
 * AND operator implementation
 */
public class ANDOperator extends OperatorDecorator {

    public ANDOperator(Formula left, Formula right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String getFormula() {
        return "(" + left.getFormula() + " AND " + right.getFormula() + ")";
    }
}
