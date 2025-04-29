package ca.yorku.cmg.istardt.xmlparser.objects;

/**
 * NOT operator implementation
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
