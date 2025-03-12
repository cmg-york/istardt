package com.example.xml.dto;

import jakarta.xml.bind.annotation.*;
import java.util.List;

/**
 * Addition operation DTO
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class AddDto {
    @XmlElement(name = "const", namespace = "https://example.org/istar-t")
    private List<String> const_;

    @XmlElement(name = "numAtom", namespace = "https://example.org/istar-t")
    private List<String> numAtom;

    @XmlElement(name = "subtract", namespace = "https://example.org/istar-t")
    private SubtractDto subtract;

    @XmlElement(name = "multiply", namespace = "https://example.org/istar-t")
    private MultiplyDto multiply;

    // Getters and setters
    public List<String> getConst() { return const_; }
    public void setConst(List<String> const_) { this.const_ = const_; }

    public List<String> getNumAtom() { return numAtom; }
    public void setNumAtom(List<String> numAtom) { this.numAtom = numAtom; }

    public SubtractDto getSubtract() { return subtract; }
    public void setSubtract(SubtractDto subtract) { this.subtract = subtract; }

    public MultiplyDto getMultiply() { return multiply; }
    public void setMultiply(MultiplyDto multiply) { this.multiply = multiply; }
}