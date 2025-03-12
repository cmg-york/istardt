package com.example.xml.dto;

import jakarta.xml.bind.annotation.*;

/**
 * Single quality DTO
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class QualityDto {
    @XmlAttribute
    private String name;

    @XmlAttribute
    private String description;

    @XmlAttribute
    private Boolean exported;

    @XmlAttribute
    private Boolean root;

    @XmlElement(name = "formula", namespace = "https://example.org/istar-t")
    private FormulaDto formula;

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Boolean getExported() { return exported; }
    public void setExported(Boolean exported) { this.exported = exported; }

    public Boolean getRoot() { return root; }
    public void setRoot(Boolean root) { this.root = root; }

    public FormulaDto getFormula() { return formula; }
    public void setFormula(FormulaDto formula) { this.formula = formula; }
}