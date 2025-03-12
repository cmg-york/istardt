package com.example.objects.builders;

import com.example.objects.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder for Goal objects
 */
public class GoalBuilder {
    private final Goal goal;
    private final List<String> preConditions = new ArrayList<>();
    private final List<String> negPreConditions = new ArrayList<>();

    public GoalBuilder() {
        this.goal = new Goal();
    }

    public GoalBuilder withId(String id) {
        goal.setId(id);
        return this;
    }

    public GoalBuilder withRepresentation(String description) {
        Atom atom = new Atom();
        atom.setId(goal.getId());
        atom.setTitleText(description);
        goal.setRepresentation(atom);
        return this;
    }

    public GoalBuilder withDecompType(DecompType type) {
        goal.setDecompType(type);
        return this;
    }

    public GoalBuilder withRuns(int runs) {
        goal.setRuns(runs);
        return this;
    }

    public GoalBuilder isRoot(boolean isRoot) {
        // In a real implementation, this would set a flag that would be used
        // during the model building phase
        return this;
    }

    public GoalBuilder withPrecondition(String precondition) {
        preConditions.add(precondition);
        return this;
    }

    public GoalBuilder withNegPrecondition(String negPrecondition) {
        negPreConditions.add(negPrecondition);
        return this;
    }

    public GoalBuilder withAndChild(DecompositionElement child) {
        goal.addANDChild(child);
        return this;
    }

    public GoalBuilder withOrChild(DecompositionElement child) {
        goal.addORChild(child);
        return this;
    }

    public Goal build() {
        // Set preconditions as Formula objects if needed
        // This would use the preconditions and negPreconditions lists

        return goal;
    }
}


