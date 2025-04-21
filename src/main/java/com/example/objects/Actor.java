package com.example.objects;

import com.example.xml.ReferenceResolver;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.example.xml.deserializers.ActorDeserializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Actor class representing an agent in the iStar-T model.
 * Modified to support Jackson XML unmarshalling.
 */
@JsonRootName("actor")
@JsonDeserialize(using = ActorDeserializer.class)
public class Actor extends Element {

    @JacksonXmlElementWrapper(localName = "goals")
    @JacksonXmlProperty(localName = "goal")
    @JsonManagedReference("actor-goals")
    private List<Goal> goals;

    @JacksonXmlElementWrapper(localName = "tasks")
    @JacksonXmlProperty(localName = "task")
    @JsonManagedReference("actor-tasks")
    private List<Task> tasks;

    @JsonIgnore
    private List<Effect> directEffects;

    @JacksonXmlElementWrapper(localName = "qualities")
    @JacksonXmlProperty(localName = "quality")
    @JsonManagedReference("actor-qualities")
    private List<Quality> qualities;

    public Actor() {
        this.goals = new ArrayList<>();
        this.tasks = new ArrayList<>();
        this.qualities = new ArrayList<>();
        this.directEffects = new ArrayList<>();
    }

    @Override
    public void setId(String id) {
        super.setId(id);
        // Register this actor with the reference resolver
        ReferenceResolver.getInstance().registerElement(id, this);
    }

    public void setGoals(List<Goal> goals) {
        this.goals = goals;
    }

    public List<Goal> getGoals() {
        return goals;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setDirectEffects(List<Effect> directEffects) {
        this.directEffects = directEffects;
    }

    public List<Effect> getDirectEffects() {
        return directEffects;
    }

    public void setQualities(List<Quality> qualities) {
        this.qualities = qualities;
    }

    public List<Quality> getQualities() {
        return qualities;
    }

    /**
     * Gets the root element among the goals.
     *
     * @return The root element, or null if not found
     */
    @JsonIgnore
    public Element getRoot() {
        // Find the root element among the goals
        if (goals != null) {
            for (Goal goal : goals) {
                if (goal instanceof DecompositionElement && ((DecompositionElement) goal).isRoot()) {
                    return goal;
                }
            }
        }
        return null;
    }

    /**
     * Adds a goal to this actor.
     *
     * @param goal The goal to add
     */
    public void addGoal(Goal goal) {
        if (goals == null) {
            goals = new ArrayList<>();
        }
        goals.add(goal);
    }

    /**
     * Adds a task to this actor.
     *
     * @param task The task to add
     */
    public void addTask(Task task) {
        if (tasks == null) {
            tasks = new ArrayList<>();
        }
        tasks.add(task);
    }

    /**
     * Adds a quality to this actor.
     *
     * @param quality The quality to add
     */
    public void addQuality(Quality quality) {
        if (qualities == null) {
            qualities = new ArrayList<>();
        }
        qualities.add(quality);
    }

    /**
     * Returns a string representation of this actor.
     * Includes the ID, name, and counts of contained elements.
     *
     * @return A string representation of this actor
     */
    @Override
    public String toString() {
        // Get name from atom's titleText
        String name = getAtom() != null ? getAtom().getTitleText() : null;

        return "Actor{id=" + getId() +
                ", name='" + name + '\'' +
                ", goals=" + (goals != null ? goals.size() : 0) +
                ", tasks=" + (tasks != null ? tasks.size() : 0) +
                ", qualities=" + (qualities != null ? qualities.size() : 0) + "}";
    }
}