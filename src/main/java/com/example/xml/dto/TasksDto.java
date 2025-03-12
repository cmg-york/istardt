package com.example.xml.dto;

import java.util.List;
import jakarta.xml.bind.annotation.*;

/**
 * Tasks container DTO
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class TasksDto {
    @XmlElement(name = "task", namespace = "https://example.org/istar-t")
    private List<TaskDto> task;

    // Getters and setters
    public List<TaskDto> getTask() { return task; }
    public void setTask(List<TaskDto> task) { this.task = task; }
}
