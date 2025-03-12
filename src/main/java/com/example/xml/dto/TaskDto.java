package com.example.xml.dto;

import java.util.List;
import jakarta.xml.bind.annotation.*;

/**
 * Single task DTO
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class TaskDto {
    @XmlAttribute
    private String name;

    @XmlAttribute
    private String description;

    @XmlElement(name = "pre", namespace = "https://example.org/istar-t")
    private List<String> pre;

    @XmlElement(name = "npr", namespace = "https://example.org/istar-t")
    private List<String> npr;

    @XmlElement(name = "effectGroup", namespace = "https://example.org/istar-t")
    private EffectGroupDto effectGroup;

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<String> getPre() { return pre; }
    public void setPre(List<String> pre) { this.pre = pre; }

    public List<String> getNpr() { return npr; }
    public void setNpr(List<String> npr) { this.npr = npr; }

    public EffectGroupDto getEffectGroup() { return effectGroup; }
    public void setEffectGroup(EffectGroupDto effectGroup) { this.effectGroup = effectGroup; }
}