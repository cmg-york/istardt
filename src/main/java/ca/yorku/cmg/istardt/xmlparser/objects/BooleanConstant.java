package ca.yorku.cmg.istardt.xmlparser.objects;

public class BooleanConstant extends Formula {
    private boolean content;

    public BooleanConstant(boolean content){
        this.content = content;
    }

    public boolean getContent() { return content; }

    public void setContent(boolean content) {
        this.content = content;
    }

    @Override
    public String getFormula() {
        return String.valueOf(content);
    }
}
