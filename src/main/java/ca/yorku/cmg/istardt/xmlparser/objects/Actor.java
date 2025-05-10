package ca.yorku.cmg.istardt.xmlparser.objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import ca.yorku.cmg.istardt.xmlparser.xml.deserializers.ActorDeserializer;

import java.util.ArrayList;
import java.util.List;

@JsonRootName("actor")
@JsonDeserialize(using = ActorDeserializer.class)
public class Actor extends Element {
    @JsonManagedReference("actor-goals")
    private List<Goal> goals;
    @JsonManagedReference("actor-tasks")
    private List<Task> tasks;
    @JsonIgnore
    private List<Effect> effects;
    @JsonManagedReference("actor-qualities")
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
    public Element getRoot() {
        if (goals != null) {
            for (Goal goal : goals) {
                if (goal instanceof DecompositionElement && ((DecompositionElement) goal).isRoot()) {
                    return goal;
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