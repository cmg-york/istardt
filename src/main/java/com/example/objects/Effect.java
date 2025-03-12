package com.example.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Extensions to the Effect class to add functionality needed for XML mapping
 */
public class Effect extends NonDecompositionElement {
    public float probability;
    public Formula preFormula;
    public Formula nprFormula;
    private Task task;
    private boolean satisfying = true;

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

    public List<Effect> getSiblings() {
        if (task == null) {
            return new ArrayList<>();
        }

        List<Effect> siblings = new ArrayList<>(task.getEffects());
        siblings.remove(this);
        return siblings;
    }

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
}