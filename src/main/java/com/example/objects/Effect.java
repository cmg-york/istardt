package com.example.objects;

import com.example.xml.ReferenceResolver;
import com.example.xml.deserializers.EffectDeserializer;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Effect class representing the outcome of a task.
 * Modified to support Jackson XML unmarshalling.
 */
@JsonDeserialize(using = EffectDeserializer.class)
public class Effect extends NonDecompositionElement {

    @JacksonXmlProperty(isAttribute = true)
    private float probability;

    @JacksonXmlProperty(isAttribute = true)
    private boolean satisfying = true;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "turnsTrue")
    private List<String> turnsTrue;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "turnsFalse")
    private List<String> turnsFalse;

    private Formula preFormula;
    private Formula nprFormula;

    @JsonBackReference("task-effects")
    private Task task;

    /**
     * Default constructor.
     */
    public Effect() {
        this.turnsTrue = new ArrayList<>();
        this.turnsFalse = new ArrayList<>();
    }

    public boolean isSatisfying() {
        return satisfying;
    }

    public void setSatisfying(boolean satisfying) {
        this.satisfying = satisfying;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public List<String> getTurnsTrue() {
        return turnsTrue;
    }

    public void setTurnsTrue(List<String> turnsTrue) {
        this.turnsTrue = turnsTrue;
    }

    public List<String> getTurnsFalse() {
        return turnsFalse;
    }

    public void setTurnsFalse(List<String> turnsFalse) {
        this.turnsFalse = turnsFalse;
    }

    public void addTurnsTrue(String predicate) {
        if (turnsTrue == null) {
            turnsTrue = new ArrayList<>();
        }
        turnsTrue.add(predicate);
    }

    public void addTurnsFalse(String predicate) {
        if (turnsFalse == null) {
            turnsFalse = new ArrayList<>();
        }
        turnsFalse.add(predicate);
    }

    /**
     * Get the siblings of this effect (other effects from the same task).
     *
     * @return The list of sibling effects
     */
    public List<Effect> getSiblings() {
        if (task == null) {
            return new ArrayList<>();
        }

        List<Effect> siblings = new ArrayList<>(task.getEffects());
        siblings.remove(this);
        return siblings;
    }

    /**
     * Check if this effect is a sibling of another effect.
     *
     * @param effect The effect to check
     * @return True if they are siblings, false otherwise
     */
    public boolean isSiblingOf(Effect effect) {
        if (task == null || effect == null || effect.getTask() == null) {
            return false;
        }

        return task.equals(effect.getTask());
    }

    public float getProbability() {
        return probability;
    }

    public void setProbability(float probability) {
        this.probability = probability;
    }

    public Formula getPreFormula() {
        return preFormula;
    }

    public void setPreFormula(Formula preFormula) {
        this.preFormula = preFormula;
    }

    public Formula getNprFormula() {
        return nprFormula;
    }

    public void setNprFormula(Formula nprFormula) {
        this.nprFormula = nprFormula;
    }

    /**
     * Override toString to prevent infinite recursion from parent-child circular references.
     * Includes Effect-specific information while avoiding circular references.
     */
    @Override
    public String toString() {
        return "Effect{id=" + getId() +
                ", probability=" + probability +
                ", satisfying=" + satisfying +
                ", turnsTrueCount=" + (turnsTrue != null ? turnsTrue.size() : 0) +
                ", turnsFalseCount=" + (turnsFalse != null ? turnsFalse.size() : 0) +
                ", hasTask=" + (task != null) + "}";
    }
}