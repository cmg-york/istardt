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

    @JacksonXmlProperty(isAttribute = true)
    private String name;

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

    public void setName(String name) {
        this.name = name;

        // If ID isn't set, use name as ID
        if (getId() == null) {
            setId(name);
        }
    }

    public String getName() {
        return name;
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
     * Compares this actor to the specified object.
     * Actors are considered equal if they have the same name and similar
     * collections of elements based on their names/titleTexts.
     *
     * @param obj The object to compare this actor to
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        if (!super.equals(obj)) return false;

        Actor actor = (Actor) obj;

        // Name is the primary identifier for content-based comparison
        if (!Objects.equals(name, actor.name)) return false;

        // Compare goals, tasks, and qualities by their atom titleTexts
        List<String> thisGoalNames = getElementNames(goals);
        List<String> thatGoalNames = getElementNames(actor.goals);

        List<String> thisTaskNames = getElementNames(tasks);
        List<String> thatTaskNames = getElementNames(actor.tasks);

        List<String> thisQualityNames = getElementNames(qualities);
        List<String> thatQualityNames = getElementNames(actor.qualities);

        return thisGoalNames.equals(thatGoalNames) &&
                thisTaskNames.equals(thatTaskNames) &&
                thisQualityNames.equals(thatQualityNames);
    }

    /**
     * Helper method to extract names/titleTexts from a list of elements
     *
     * @param elements The list of elements
     * @return A list of element names (from atoms' titleText)
     */
    private <T extends Element> List<String> getElementNames(List<T> elements) {
        if (elements == null) return new ArrayList<>();
        return elements.stream()
                .map(e -> e.getAtom() != null ? e.getAtom().getTitleText() : e.getId())
                .collect(Collectors.toList());
    }

    /**
     * Returns a hash code value for this actor.
     * The hash code is computed based on the name and element names
     * to ensure it satisfies the contract with equals().
     *
     * @return A hash code value for this actor
     */
    @Override
    public int hashCode() {
        List<String> goalNames = getElementNames(goals);
        List<String> taskNames = getElementNames(tasks);
        List<String> qualityNames = getElementNames(qualities);

        return Objects.hash(name, goalNames, taskNames, qualityNames);
    }

    /**
     * Returns a string representation of this actor.
     * Includes the ID, name, and counts of contained elements.
     *
     * @return A string representation of this actor
     */
    @Override
    public String toString() {
        return "Actor{id=" + getId() +
                ", name='" + name + '\'' +
                ", goals=" + (goals != null ? goals.size() : 0) +
                ", tasks=" + (tasks != null ? tasks.size() : 0) +
                ", qualities=" + (qualities != null ? qualities.size() : 0) + "}";
    }
}