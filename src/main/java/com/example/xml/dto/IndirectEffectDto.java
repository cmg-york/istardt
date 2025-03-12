package com.example.xml.dto;

import jakarta.xml.bind.annotation.*;

/**
 * Single indirect effect DTO
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class IndirectEffectDto {
    @XmlAttribute
    private String name;

    @XmlAttribute
    private Boolean exported;

    @XmlAttribute
    private String description;

    @XmlElement(name = "formula", namespace = "https://example.org/istar-t")
    private FormulaDto formula;

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Boolean getExported() { return exported; }
    public void setExported(Boolean exported) { this.exported = exported; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public FormulaDto getFormula() { return formula; }
    public void setFormula(FormulaDto formula) { this.formula = formula; }
}
