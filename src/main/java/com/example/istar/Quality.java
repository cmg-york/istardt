package com.example.istar;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class Quality extends NumericExpression {
    @XmlAttribute(name = "name", required = true)
    private String name;

    @XmlAttribute(name = "description")
    private String description;

    @XmlAttribute(name = "exported")
    private boolean exported;

    @XmlAttribute(name = "root")
    private boolean root;

    // Getters and setters

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public boolean isExported() {
        return exported;
    }
    public void setExported(boolean exported) {
        this.exported = exported;
    }
    public boolean isRoot() {
        return root;
    }
    public void setRoot(boolean root) {
        this.root = root;
    }
}
