package com.example.istar;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class Predicate {
    // The text content inside <predicate> (e.g. "deliveredInTimeDom")
    @XmlValue
    private String value;

    @XmlAttribute
    private boolean primitive;

    @XmlAttribute
    private boolean init;

    @XmlAttribute
    private boolean exported;

    @XmlAttribute
    private String description;

    // Getters and setters

    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public boolean isPrimitive() {
        return primitive;
    }
    public void setPrimitive(boolean primitive) {
        this.primitive = primitive;
    }
    public boolean isInit() {
        return init;
    }
    public void setInit(boolean init) {
        this.init = init;
    }
    public boolean isExported() {
        return exported;
    }
    public void setExported(boolean exported) {
        this.exported = exported;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
