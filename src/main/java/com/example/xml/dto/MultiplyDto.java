package com.example.xml.dto;

import java.util.List;
import jakarta.xml.bind.annotation.*;

/**
 * Multiplication operation DTO
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class MultiplyDto {
    @XmlElement(name = "const", namespace = "https://example.org/istar-t")
    private List<String> const_;

    @XmlElement(name = "numAtom", namespace = "https://example.org/istar-t")
    private List<String> numAtom;

    // Getters and setters
    public List<String> getConst() { return const_; }
    public void setConst(List<String> const_) { this.const_ = const_; }

    public List<String> getNumAtom() { return numAtom; }
    public void setNumAtom(List<String> numAtom) { this.numAtom = numAtom; }
}

