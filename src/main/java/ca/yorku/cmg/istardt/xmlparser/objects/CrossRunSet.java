package ca.yorku.cmg.istardt.xmlparser.objects;

import java.util.ArrayList;
import java.util.List;

public class CrossRunSet {
    private List<String> refs = new ArrayList<>();
    private List<Element> elements = new ArrayList<>();

    public List<String> getRefs() {
        return refs;
    }

    public void addRefs(String ref){
        refs.add(ref);
    }

    public void setRefs(List<String> refs) {
        this.refs = refs;
    }

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }

    public void addElement(Element element) {
        elements.add(element);
    }
}
