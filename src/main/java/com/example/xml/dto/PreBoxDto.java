package com.example.xml.dto;

import jakarta.xml.bind.annotation.*;
import java.util.List;

/**
 * Single preBox definition (mapped to Condition in the domain model)
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PreBoxDto {
    @XmlAttribute
    private String name;

    @XmlAttribute
    private String description;

    // Boolean expressions
    @XmlElement(name = "gt", namespace = "https://example.org/istar-t")
    private GtDto gt;

    @XmlElement(name = "boolConst", namespace = "https://example.org/istar-t")
    private String boolConst;

    @XmlElement(name = "boolAtom", namespace = "https://example.org/istar-t")
    private String boolAtom;

    @XmlElement(name = "and", namespace = "https://example.org/istar-t")
    private BooleanAndDto and;

    @XmlElement(name = "or", namespace = "https://example.org/istar-t")
    private BooleanOrDto or;

    @XmlElement(name = "not", namespace = "https://example.org/istar-t")
    private BooleanNotDto not;

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public GtDto getGt() { return gt; }
    public void setGt(GtDto gt) { this.gt = gt; }

    public String getBoolConst() { return boolConst; }
    public void setBoolConst(String boolConst) { this.boolConst = boolConst; }

    public String getBoolAtom() { return boolAtom; }
    public void setBoolAtom(String boolAtom) { this.boolAtom = boolAtom; }

    public BooleanAndDto getAnd() { return and; }
    public void setAnd(BooleanAndDto and) { this.and = and; }

    public BooleanOrDto getOr() { return or; }
    public void setOr(BooleanOrDto or) { this.or = or; }

    public BooleanNotDto getNot() { return not; }
    public void setNot(BooleanNotDto not) { this.not = not; }
}
