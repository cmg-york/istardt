package ca.yorku.cmg.istardt.xmlparser.objects;

/**
 * Not Equals operator implementation
 */
public class NEQOperator extends OperatorDecorator {
    public NEQOperator(Formula left, Formula right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String getFormula() {
        return "(" + left.getFormula() + " != " + right.getFormula() + ")";
    }
}