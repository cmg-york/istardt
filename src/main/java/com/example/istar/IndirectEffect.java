package com.example.istar;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class IndirectEffect extends NumericExpression {
    @XmlAttribute(name = "name", required = true)
    private String name;

    @XmlAttribute(name = "exported")
    private boolean exported;

    @XmlAttribute(name = "description")
    private String description;

    @XmlElement(name = "formula", namespace = "https://example.org/istar-t")
    private NumericExpression formula;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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
    public NumericExpression getFormula() {
        return formula;
    }
    public void setFormula(NumericExpression formula) {
        this.formula = formula;
    }
}
