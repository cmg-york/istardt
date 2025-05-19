package ca.yorku.cmg.istardt.xmlparser.objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ca.yorku.cmg.istardt.xmlparser.xml.deserializers.ActorDeserializer;

import java.util.ArrayList;
import java.util.List;

@JsonDeserialize(using = ActorDeserializer.class)
public class Actor extends Element {
    private List<Goal> goals;
    private List<Task> tasks;
    private List<Effect> effects;
    private List<Quality> qualities;
    private List<Condition> conditions;
    private List<Predicate> predicates;
    private List<Variable> variables;
    private CrossRunSet crossRunSet;
    private ExportedSet exportedSet;
    private InitializationSet initializationSet;

    public Actor() {
        this.goals = new ArrayList<>();
        this.tasks = new ArrayList<>();
        this.qualities = new ArrayList<>();
        this.effects = new ArrayList<>();
        this.conditions = new ArrayList<>();
        this.predicates = new ArrayList<>();
        this.variables = new ArrayList<>();
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

    public void setEffects(List<Effect> effects) {
        this.effects = effects;
    }

    public List<Effect> getEffects() {
        return effects;
    }

    public void setQualities(List<Quality> qualities) {
        this.qualities = qualities;
    }

    public List<Quality> getQualities() {
        return qualities;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public List<Predicate> getPredicates() {
        return predicates;
    }

    public void setPredicates(List<Predicate> predicates) {
        this.predicates = predicates;
    }

    public List<Variable> getVariables() {
        return variables;
    }

    public void setVariables(List<Variable> variables) {
        this.variables = variables;
    }

    public CrossRunSet getCrossRunSet() {
        return crossRunSet;
    }

    public List<Element> getCrossRunSetElements() {
        return crossRunSet.getElements();
    }

    public void setCrossRunSet(CrossRunSet crossRunSet) {
        this.crossRunSet = crossRunSet;
    }

    public ExportedSet getExportedSet() {
        return exportedSet;
    }

    public void setExportedSet(ExportedSet exportedSet) {
        this.exportedSet = exportedSet;
    }

    public InitializationSet getInitializationSet() {
        return initializationSet;
    }

    public void setInitializationSet(InitializationSet initializationSet) {
        this.initializationSet = initializationSet;
    }

    /**
     * Gets the root element among the goals.
     *
     * @return The root element, or null if not found
     */
    public Goal getGoalRoot() {
        if (goals != null) {
            for (Goal goal : goals) {
                if (goal.isRoot()) {
                    return goal;
                }
            }
        }
        return null;
    }

    /**
     * Gets the root element among the qualities.
     *
     * @return The root element, or null if not found
     */
    public Quality getQualityRoot() {
        if (qualities != null) {
            for (Quality quality : qualities) {
                if (quality.isRoot()) {
                    return quality;
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Actor{id=" + getId() +
                ", effectCount=" + (effects != null ? effects.size() : 0) +
                ", taskCount=" + (tasks != null ? tasks.size() : 0) +
                ", qualityCount=" + (qualities != null ? qualities.size() : 0) +
                ", goalCount=" + (goals != null ? goals.size() : 0) +
                ", conditionCount=" + (conditions != null ? conditions.size() : 0) +
                ", variableCount=" + (variables != null ? variables.size() : 0) +
                ", predicateCount=" + (predicates != null ? predicates.size() : 0) + "}";
    }
}