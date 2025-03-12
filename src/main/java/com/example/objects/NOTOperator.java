package com.example.objects;


/**
 * Implementation for the NOTOperator
 */
public class NOTOperator extends OperatorDecorator {

    public NOTOperator(Formula formula) {
        this.left = formula;
        this.right = null;
    }

    @Override
    public String getFormula() {
        return "NOT(" + left.getFormula() + ")";
    }
}
