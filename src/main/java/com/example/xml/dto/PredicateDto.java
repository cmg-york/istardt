package com.example.xml.dto;

import jakarta.xml.bind.annotation.*;
import java.util.List;

/**
 * Single predicate definition
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PredicateDto {
    @XmlAttribute
    private Boolean primitive;

    @XmlAttribute
    private Boolean init;

    @XmlAttribute
    private Boolean exported;

    @XmlAttribute
    private String description;

    @XmlValue
    private String value;

    // Getters and setters
    public Boolean getPrimitive() {
        return primitive;
    }

    public void setPrimitive(Boolean primitive) {
        this.primitive = primitive;
    }

    public Boolean getInit() {
        return init;
    }

    public void setInit(Boolean init) {
        this.init = init;
    }

    public Boolean getExported() {
        return exported;
    }

    public void setExported(Boolean exported) {
        this.exported = exported;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}