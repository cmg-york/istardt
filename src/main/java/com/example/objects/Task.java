package com.example.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Extensions to the Task class to add functionality needed for XML mapping
 */
public class Task extends DecompositionElement {
    public List<Effect> effects;

    public Task() {
        this.effects = new ArrayList<>();
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

    public boolean isDeterministic() {
        // A task is deterministic if it has only one effect with 100% probability
        return effects != null && effects.size() == 1 && effects.get(0).getProbability() == 1.0f;
    }
}