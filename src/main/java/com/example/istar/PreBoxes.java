package com.example.istar;

import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class PreBoxes {
    @XmlElement(name = "preBox", namespace = "https://example.org/istar-t")
    private List<PreBox> preBoxList;

    public List<PreBox> getPreBoxList() {
        return preBoxList;
    }
    public void setPreBoxList(List<PreBox> preBoxList) {
        this.preBoxList = preBoxList;
    }
}
