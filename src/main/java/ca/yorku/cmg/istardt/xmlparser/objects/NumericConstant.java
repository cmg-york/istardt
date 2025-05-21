package ca.yorku.cmg.istardt.xmlparser.objects;

public class NumericConstant extends Formula {
    private float content;

    public NumericConstant(float content){
        this.content = content;
    }

    public float getContent() {
        return content;
    }

    public void setContent(float content) {
        this.content = content;
    }

    @Override
    public String getFormula() {
        return String.valueOf(content);
    }
}
