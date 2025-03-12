package com.example.xml.dto;

import jakarta.xml.bind.annotation.*;
import java.util.List;

/**
 * Single effect DTO
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class EffectDto {
    @XmlAttribute
    private String name;

    @XmlAttribute
    private Boolean satisfying;

    @XmlAttribute
    private Float probability;

    @XmlAttribute
    private String description;

    @XmlElement(name = "turnsTrue", namespace = "https://example.org/istar-t")
    private List<String> turnsTrue;

    @XmlElement(name = "turnsFalse", namespace = "https://example.org/istar-t")
    private List<String> turnsFalse;

    @XmlElement(name = "pre", namespace = "https://example.org/istar-t")
    private List<String> pre;

    @XmlElement(name = "npr", namespace = "https://example.org/istar-t")
    private List<String> npr;

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Boolean getSatisfying() { return satisfying; }
    public void setSatisfying(Boolean satisfying) { this.satisfying = satisfying; }

    public Float getProbability() { return probability; }
    public void setProbability(Float probability) { this.probability = probability; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<String> getTurnsTrue() { return turnsTrue; }
    public void setTurnsTrue(List<String> turnsTrue) { this.turnsTrue = turnsTrue; }

    public List<String> getTurnsFalse() { return turnsFalse; }
    public void setTurnsFalse(List<String> turnsFalse) { this.turnsFalse = turnsFalse; }

    public List<String> getPre() { return pre; }
    public void setPre(List<String> pre) { this.pre = pre; }

    public List<String> getNpr() { return npr; }
    public void setNpr(List<String> npr) { this.npr = npr; }
}