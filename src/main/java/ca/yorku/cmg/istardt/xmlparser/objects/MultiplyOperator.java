package ca.yorku.cmg.istardt.xmlparser.objects;

public class MultiplyOperator extends OperatorDecorator {
    public MultiplyOperator(Formula left, Formula right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String getFormula() {
        return "(" + left.getFormula() + " * " + right.getFormula() + ")";
    }
}


