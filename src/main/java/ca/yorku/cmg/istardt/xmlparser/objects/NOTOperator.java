package ca.yorku.cmg.istardt.xmlparser.objects;

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
