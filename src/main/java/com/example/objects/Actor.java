package com.example.objects;

import java.util.List;

/**
 * Extensions to the Actor class to add functionality needed for XML mapping
 */
public class Actor extends Element {
    private String name;
    private List<Goal> goals;
    private List<Task> tasks;
    private List<Effect> directEffects;
    private List<Quality> qualities;
    
    public void setName(String name) {
        this.name = name;
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
}