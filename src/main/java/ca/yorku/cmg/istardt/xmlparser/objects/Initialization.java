package ca.yorku.cmg.istardt.xmlparser.objects;

public class Initialization {
    private String value;
    private String ref;

    private Element element;

    public Element getElement() { return element; }
    public void setElement(Element e) { this.element = e; }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }
}
