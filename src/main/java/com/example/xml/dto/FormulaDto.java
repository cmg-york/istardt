package com.example.xml.dto;

import jakarta.xml.bind.annotation.*;

/**
 * Formula DTO
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class FormulaDto {
    @XmlElement(name = "add", namespace = "https://example.org/istar-t")
    private AddDto add;

    @XmlElement(name = "subtract", namespace = "https://example.org/istar-t")
    private SubtractDto subtract;

    @XmlElement(name = "multiply", namespace = "https://example.org/istar-t")
    private MultiplyDto multiply;

    @XmlElement(name = "divide", namespace = "https://example.org/istar-t")
    private DivideDto divide;

    @XmlElement(name = "const", namespace = "https://example.org/istar-t")
    private String const_;

    @XmlElement(name = "numAtom", namespace = "https://example.org/istar-t")
    private String numAtom;

    // Getters and setters
    public AddDto getAdd() { return add; }
    public void setAdd(AddDto add) { this.add = add; }

    public SubtractDto getSubtract() { return subtract; }
    public void setSubtract(SubtractDto subtract) { this.subtract = subtract; }

    public MultiplyDto getMultiply() { return multiply; }
    public void setMultiply(MultiplyDto multiply) { this.multiply = multiply; }

    public DivideDto getDivide() { return divide; }
    public void setDivide(DivideDto divide) { this.divide = divide; }

    public String getConst() { return const_; }
    public void setConst(String const_) { this.const_ = const_; }

    public String getNumAtom() { return numAtom; }
    public void setNumAtom(String numAtom) { this.numAtom = numAtom; }
}