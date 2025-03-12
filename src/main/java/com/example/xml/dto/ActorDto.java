package com.example.xml.dto;

import jakarta.xml.bind.annotation.*;
import java.util.List;

/**
 * Root DTO for the actor element
 */
@XmlRootElement(name = "actor", namespace = "https://example.org/istar-t")
@XmlAccessorType(XmlAccessType.FIELD)
public class ActorDto {
    @XmlAttribute
    private String name;

    @XmlElement(namespace = "https://example.org/istar-t")
    private PredicatesDto predicates;

    @XmlElement(namespace = "https://example.org/istar-t")
    private PreBoxesDto preBoxes;

    @XmlElement(namespace = "https://example.org/istar-t")
    private IndirectEffectsDto indirectEffects;

    @XmlElement(namespace = "https://example.org/istar-t")
    private QualitiesDto qualities;

    @XmlElement(namespace = "https://example.org/istar-t")
    private GoalsDto goals;

    @XmlElement(namespace = "https://example.org/istar-t")
    private TasksDto tasks;

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public PredicatesDto getPredicates() { return predicates; }
    public void setPredicates(PredicatesDto predicates) { this.predicates = predicates; }

    public PreBoxesDto getPreBoxes() { return preBoxes; }
    public void setPreBoxes(PreBoxesDto preBoxes) { this.preBoxes = preBoxes; }

    public IndirectEffectsDto getIndirectEffects() { return indirectEffects; }
    public void setIndirectEffects(IndirectEffectsDto indirectEffects) { this.indirectEffects = indirectEffects; }

    public QualitiesDto getQualities() { return qualities; }
    public void setQualities(QualitiesDto qualities) { this.qualities = qualities; }

    public GoalsDto getGoals() { return goals; }
    public void setGoals(GoalsDto goals) { this.goals = goals; }

    public TasksDto getTasks() { return tasks; }
    public void setTasks(TasksDto tasks) { this.tasks = tasks; }
}
