package com.example.xml.dto;

import java.util.List;
import jakarta.xml.bind.annotation.*;

/**
 * Qualities container DTO
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class QualitiesDto {
    @XmlElement(name = "quality", namespace = "https://example.org/istar-t")
    private List<QualityDto> quality;

    // Getters and setters
    public List<QualityDto> getQuality() { return quality; }
    public void setQuality(List<QualityDto> quality) { this.quality = quality; }
}
