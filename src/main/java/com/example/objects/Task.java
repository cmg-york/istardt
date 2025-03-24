package com.example.objects;

import com.example.xml.ReferenceResolver;
import com.example.xml.deserializers.TaskDeserializer;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Task class representing an executable action.
 * Modified to support Jackson XML unmarshalling.
 */
@JsonDeserialize(using = TaskDeserializer.class)
public class Task extends DecompositionElement {

    @JacksonXmlElementWrapper(localName = "effectGroup")
    @JacksonXmlProperty(localName = "effect")
    @JsonManagedReference("task-effects")
    private List<Effect> effects;

    @JsonBackReference("actor-tasks")
    private Actor actor;

    public Task() {
        this.effects = new ArrayList<>();
    }

    @Override
    public void setId(String id) {
        super.setId(id);
        // Register this task with the reference resolver
        ReferenceResolver.getInstance().registerElement(id, this);
    }

    public void setEffects(List<Effect> effects) {
        this.effects = effects;

        // Set up the relationship between task and effects
        for (Effect effect : effects) {
            effect.setTask(this);
        }
    }

    public void addEffect(Effect e) {
        if (effects == null) {
            effects = new ArrayList<>();
        }
        effects.add(e);
        e.setTask(this);
    }

    public List<Effect> getEffects() {
        return effects;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    /**
     * A task is deterministic if it has only one effect with 100% probability.
     *
     * @return True if the task is deterministic, false otherwise
     */
    @JsonIgnore
    public boolean isDeterministic() {
        return effects != null && effects.size() == 1 && effects.get(0).getProbability() == 1.0f;
    }

    /**
     * Override toString to prevent infinite recursion from parent-child circular references.
     * Includes Task-specific information while avoiding circular references.
     */
    @Override
    public String toString() {
        return "Task{id=" + getId() +
                ", effectCount=" + (effects != null ? effects.size() : 0) +
                ", deterministic=" + isDeterministic() +
                ", decompType=" + getDecompType() +
                ", childCount=" + (getChildren() != null ? getChildren().size() : 0) +
                ", hasParent=" + (getParent() != null) + "}";
    }
}