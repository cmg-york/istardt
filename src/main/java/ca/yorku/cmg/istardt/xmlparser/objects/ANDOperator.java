package ca.yorku.cmg.istardt.xmlparser.objects;

public class ANDOperator extends OperatorDecorator {
    public ANDOperator(Formula left, Formula right) {
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
        return "(" + left.getFormula() + " AND " + right.getFormula() + ")";
    }
}
