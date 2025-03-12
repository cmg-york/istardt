package com.example.objects.builders;


import com.example.objects.Atom;
import com.example.objects.Effect;
import com.example.objects.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder for Task objects
 */
public class TaskBuilder {
    private final Task task;
    private final List<String> preConditions = new ArrayList<>();
    private final List<String> negPreConditions = new ArrayList<>();

    public TaskBuilder() {
        this.task = new Task();
    }

    public TaskBuilder withId(String id) {
        task.setId(id);
        return this;
    }

    public TaskBuilder withRepresentation(String description) {
        Atom atom = new Atom();
        atom.setId(task.getId());
        atom.setTitleText(description);
        task.setRepresentation(atom);
        return this;
    }

    public TaskBuilder withPrecondition(String precondition) {
        preConditions.add(precondition);
        return this;
    }

    public TaskBuilder withNegPrecondition(String negPrecondition) {
        negPreConditions.add(negPrecondition);
        return this;
    }

    public TaskBuilder withEffect(Effect effect) {
        task.addEffect(effect);
        effect.setTask(task);
        return this;
    }

    public Task build() {
        // Set preconditions as Formula objects if needed
        // This would use the preconditions and negPreconditions lists

        return task;
    }
}
