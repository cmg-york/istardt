package com.example.xml.dto;

import java.util.List;
import jakarta.xml.bind.annotation.*;

/**
 * Indirect effects container DTO
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class IndirectEffectsDto {
    @XmlElement(name = "indirectEffect", namespace = "https://example.org/istar-t")
    private List<IndirectEffectDto> indirectEffect;

    // Getters and setters
    public List<IndirectEffectDto> getIndirectEffect() { return indirectEffect; }
    public void setIndirectEffect(List<IndirectEffectDto> indirectEffect) { this.indirectEffect = indirectEffect; }
}