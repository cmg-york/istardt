package com.example.objects;

/**
 * Greater Than or Equal To operator implementation
 */
public class GTEOperator extends OperatorDecorator {

    public GTEOperator(Formula left, Formula right) {
        this.left = left;
        this.right = right;
    }
    
    public Formula getLeft() {
    	return(this.left);
    }
    
    public Formula getRight() {
    	return(this.right);
    }
    
    @Override
    public String getFormula() {
        return "(" + left.getFormula() + " >= " + right.getFormula() + ")";
    }
}