package com.example.istar;

import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Task {
    @XmlAttribute(name = "name", required = true)
    private String name;

    @XmlAttribute(name = "description")
    private String description;

    // Pre and npr can appear multiple times
    @XmlElement(name = "pre", namespace = "https://example.org/istar-t")
    private List<String> pre;

    @XmlElement(name = "npr", namespace = "https://example.org/istar-t")
    private List<String> npr;

    @XmlElement(name = "effectGroup", namespace = "https://example.org/istar-t")
    private EffectGroup effectGroup;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public List<String> getPre() {
        return pre;
    }
    public void setPre(List<String> pre) {
        this.pre = pre;
    }
    public List<String> getNpr() {
        return npr;
    }
    public void setNpr(List<String> npr) {
        this.npr = npr;
    }
    public EffectGroup getEffectGroup() {
        return effectGroup;
    }
    public void setEffectGroup(EffectGroup effectGroup) {
        this.effectGroup = effectGroup;
    }
}
