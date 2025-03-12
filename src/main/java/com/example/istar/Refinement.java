package com.example.istar;

import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Refinement {
    @XmlAttribute(name = "type", required = true)
    private String type;

    // Child elements: could be childGoal or childTask (we use the same type)
    @XmlElements({
            @XmlElement(name = "childGoal", type = ChildRef.class, namespace = "https://example.org/istar-t"),
            @XmlElement(name = "childTask", type = ChildRef.class, namespace = "https://example.org/istar-t")
    })
    private List<ChildRef> children;

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public List<ChildRef> getChildren() {
        return children;
    }
    public void setChildren(List<ChildRef> children) {
        this.children = children;
    }
}
