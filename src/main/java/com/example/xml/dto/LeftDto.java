package com.example.xml.dto;

import jakarta.xml.bind.annotation.*;
import java.util.List;

/**
 * Left expression in a comparison
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class LeftDto implements OperandDto {
    @XmlElement(name = "const", namespace = "https://example.org/istar-t")
    private String const_;

    @XmlElement(name = "numAtom", namespace = "https://example.org/istar-t")
    private String numAtom;

    // Getters and setters
    public String getConst() { return const_; }
    public void setConst(String const_) { this.const_ = const_; }

    public String getNumAtom() { return numAtom; }
    public void setNumAtom(String numAtom) { this.numAtom = numAtom; }
}