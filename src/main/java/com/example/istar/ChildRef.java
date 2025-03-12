package com.example.istar;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class ChildRef {
    @XmlAttribute(name = "ref", required = true)
    private String ref;

    public String getRef() {
        return ref;
    }
    public void setRef(String ref) {
        this.ref = ref;
    }
}
