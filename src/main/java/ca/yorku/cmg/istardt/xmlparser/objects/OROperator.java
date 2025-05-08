package ca.yorku.cmg.istardt.xmlparser.objects;

public class OROperator extends OperatorDecorator {
    public OROperator(Formula left, Formula right) {
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
        return "(" + left.getFormula() + " OR " + right.getFormula() + ")";
    }
}
