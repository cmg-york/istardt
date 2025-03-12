package com.example.xml.dto;

import jakarta.xml.bind.annotation.*;

/**
 * Child reference DTO
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ChildRefDto {
    @XmlAttribute
    private String ref;

    // Getters and setters
    public String getRef() { return ref; }
    public void setRef(String ref) { this.ref = ref; }
}