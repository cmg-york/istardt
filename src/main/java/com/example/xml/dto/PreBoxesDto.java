package com.example.xml.dto;

import jakarta.xml.bind.annotation.*;
import java.util.List;

/**
 * Container for preBoxes (Conditions)
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PreBoxesDto {
    @XmlElement(name = "preBox", namespace = "https://example.org/istar-t")
    private List<PreBoxDto> preBox;

    // Getters and setters
    public List<PreBoxDto> getPreBox() { return preBox; }
    public void setPreBox(List<PreBoxDto> preBox) { this.preBox = preBox; }
}
