package com.example.xml.dto;

import java.util.List;
import jakarta.xml.bind.annotation.*;

/**
 * Boolean OR operation DTO
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BooleanOrDto {
    // Similar to BooleanAndDto
    @XmlElement(name = "boolAtom", namespace = "https://example.org/istar-t")
    private List<String> boolAtom;

    @XmlElement(name = "boolConst", namespace = "https://example.org/istar-t")
    private List<String> boolConst;

    // Getters and setters
    public List<String> getBoolAtom() { return boolAtom; }
    public void setBoolAtom(List<String> boolAtom) { this.boolAtom = boolAtom; }

    public List<String> getBoolConst() { return boolConst; }
    public void setBoolConst(List<String> boolConst) { this.boolConst = boolConst; }
}
