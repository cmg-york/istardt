package com.example.istar;

import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class EffectGroup {
    @XmlElement(name = "effect", namespace = "https://example.org/istar-t")
    private List<Effect> effectList;

    public List<Effect> getEffectList() {
        return effectList;
    }
    public void setEffectList(List<Effect> effectList) {
        this.effectList = effectList;
    }
}
