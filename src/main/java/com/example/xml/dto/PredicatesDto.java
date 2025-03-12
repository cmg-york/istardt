package com.example.xml.dto;

import jakarta.xml.bind.annotation.*;
import java.util.List;

/**
 * Container for predicates
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PredicatesDto {
    @XmlElement(name = "predicate", namespace = "https://example.org/istar-t")
    private List<PredicateDto> predicate;

    // Getters and setters
    public List<PredicateDto> getPredicate() { return predicate; }
    public void setPredicate(List<PredicateDto> predicate) { this.predicate = predicate; }
}