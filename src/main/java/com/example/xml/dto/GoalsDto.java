package com.example.xml.dto;

import java.util.List;
import jakarta.xml.bind.annotation.*;

/**
 * Goals container DTO
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class GoalsDto {
    @XmlElement(name = "goal", namespace = "https://example.org/istar-t")
    private List<GoalDto> goal;

    // Getters and setters
    public List<GoalDto> getGoal() { return goal; }
    public void setGoal(List<GoalDto> goal) { this.goal = goal; }
}