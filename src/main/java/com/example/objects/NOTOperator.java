package com.example.objects;

/**
 * NOT operator implementation
 */
public class NOTOperator extends OperatorDecorator {
    public NOTOperator(Formula formula) {
        this.left = formula;
        this.right = null;
    }

    public Formula getLeft() {
    	return(this.left);
    }
    
    public Formula getRight() {
    	return(this.right);
    }
    
    
    @Override
    public String getFormula() {
        return "NOT(" + left.getFormula() + ")";
    }
}
