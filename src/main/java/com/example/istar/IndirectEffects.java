package com.example.istar;

import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class IndirectEffects {
    @XmlElement(name = "indirectEffect", namespace = "https://example.org/istar-t")
    private List<IndirectEffect> indirectEffectList;

    public List<IndirectEffect> getIndirectEffectList() {
        return indirectEffectList;
    }
    public void setIndirectEffectList(List<IndirectEffect> indirectEffectList) {
        this.indirectEffectList = indirectEffectList;
    }
}
