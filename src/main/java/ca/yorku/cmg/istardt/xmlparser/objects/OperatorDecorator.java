package ca.yorku.cmg.istardt.xmlparser.objects;

public abstract class OperatorDecorator extends Formula {
    protected Formula left;
    protected Formula right;

    public Formula getLeft(){
        return this.left;
    }

    public Formula getRight(){
        return this.right;
    }
}
