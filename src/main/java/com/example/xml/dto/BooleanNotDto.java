package com.example.xml.dto;

import jakarta.xml.bind.annotation.*;

/**
 * Boolean NOT operation DTO
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BooleanNotDto {
    // A NOT operation wraps a single boolean expression
    @XmlElement(name = "boolAtom", namespace = "https://example.org/istar-t")
    private String boolAtom;

    @XmlElement(name = "boolConst", namespace = "https://example.org/istar-t")
    private String boolConst;

    // Getters and setters
    public String getBoolAtom() { return boolAtom; }
    public void setBoolAtom(String boolAtom) { this.boolAtom = boolAtom; }

    public String getBoolConst() { return boolConst; }
    public void setBoolConst(String boolConst) { this.boolConst = boolConst; }
}
