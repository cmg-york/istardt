package com.example.istar;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "actor", namespace = "https://example.org/istar-t")
@XmlAccessorType(XmlAccessType.FIELD)
public class Actor {

    @XmlAttribute(name = "name", required = true)
    private String name;

    @XmlElement(name = "predicates", namespace = "https://example.org/istar-t")
    private Predicates predicates;

    @XmlElement(name = "preBoxes", namespace = "https://example.org/istar-t")
    private PreBoxes preBoxes;

    @XmlElement(name = "indirectEffects", namespace = "https://example.org/istar-t")
    private IndirectEffects indirectEffects;

    @XmlElement(name = "qualities", namespace = "https://example.org/istar-t")
    private Qualities qualities;

    @XmlElement(name = "goals", namespace = "https://example.org/istar-t")
    private Goals goals;

    @XmlElement(name = "tasks", namespace = "https://example.org/istar-t")
    private Tasks tasks;

    // Getters and setters

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Predicates getPredicates() {
        return predicates;
    }
    public void setPredicates(Predicates predicates) {
        this.predicates = predicates;
    }
    public PreBoxes getPreBoxes() {
        return preBoxes;
    }
    public void setPreBoxes(PreBoxes preBoxes) {
        this.preBoxes = preBoxes;
    }
    public IndirectEffects getIndirectEffects() {
        return indirectEffects;
    }
    public void setIndirectEffects(IndirectEffects indirectEffects) {
        this.indirectEffects = indirectEffects;
    }
    public Qualities getQualities() {
        return qualities;
    }
    public void setQualities(Qualities qualities) {
        this.qualities = qualities;
    }
    public Goals getGoals() {
        return goals;
    }
    public void setGoals(Goals goals) {
        this.goals = goals;
    }
    public Tasks getTasks() {
        return tasks;
    }
    public void setTasks(Tasks tasks) {
        this.tasks = tasks;
    }
}
