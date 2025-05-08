package ca.yorku.cmg.istardt.xmlparser.objects;

public class PreviousOperator extends OperatorDecorator {
    public PreviousOperator(Formula formula) {
        this.left = formula;
        this.right = null;
    }

    @Override
    public String getFormula() {
        return "PREVIOUS(" + left.getFormula() + ")";
    }
}