package com.example.istar;

import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public abstract class NumericExpression {
    @XmlMixed
    @XmlAnyElement(lax = true)
    private List<Object> content;

    public List<Object> getContent() {
        return content;
    }
    public void setContent(List<Object> content) {
        this.content = content;
    }
}
