package com.example.istar;

import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Qualities {
    @XmlElement(name = "quality", namespace = "https://example.org/istar-t")
    private List<Quality> qualityList;

    public List<Quality> getQualityList() {
        return qualityList;
    }
    public void setQualityList(List<Quality> qualityList) {
        this.qualityList = qualityList;
    }
}
