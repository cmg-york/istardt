package ca.yorku.cmg.istardt.xmlparser.objects;

public class MinusOperator extends OperatorDecorator {
    public MinusOperator(Formula left, Formula right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String getFormula() {
        return "(" + left.getFormula() + " - " + right.getFormula() + ")";
    }
}
