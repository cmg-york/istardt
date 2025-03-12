package com.example.xml.dto;

import jakarta.xml.bind.annotation.*;
import java.util.List;

/**
 * Effect group DTO
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class EffectGroupDto {
    @XmlElement(name = "effect", namespace = "https://example.org/istar-t")
    private List<EffectDto> effect;

    // Getters and setters
    public List<EffectDto> getEffect() { return effect; }
    public void setEffect(List<EffectDto> effect) { this.effect = effect; }
}
