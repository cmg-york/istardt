package com.example.xml.dto;

import java.util.List;
import jakarta.xml.bind.annotation.*;

/**
 * Goal refinement DTO
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class RefinementDto {
    @XmlAttribute
    private String type;

    @XmlElement(name = "childGoal", namespace = "https://example.org/istar-t")
    private List<ChildRefDto> childGoal;

    @XmlElement(name = "childTask", namespace = "https://example.org/istar-t")
    private List<ChildRefDto> childTask;

    // Getters and setters
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public List<ChildRefDto> getChildGoal() { return childGoal; }
    public void setChildGoal(List<ChildRefDto> childGoal) { this.childGoal = childGoal; }

    public List<ChildRefDto> getChildTask() { return childTask; }
    public void setChildTask(List<ChildRefDto> childTask) { this.childTask = childTask; }
}
