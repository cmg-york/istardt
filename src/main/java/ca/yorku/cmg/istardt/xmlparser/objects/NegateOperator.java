package ca.yorku.cmg.istardt.xmlparser.objects;

public class NegateOperator extends OperatorDecorator {
    public NegateOperator(Formula formula) {
        this.left = formula;
        this.right = null;
    }

    @Override
    public String getFormula() {
        return "-(" + left.getFormula() + ")";
    }
}