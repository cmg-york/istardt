package ca.yorku.cmg.istardt.xmlparser.objects;

/**
 * Divide operator implementation
 */
public class DivideOperator extends OperatorDecorator {
    public DivideOperator(Formula left, Formula right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String getFormula() {
        return "(" + left.getFormula() + " / " + right.getFormula() + ")";
    }
}