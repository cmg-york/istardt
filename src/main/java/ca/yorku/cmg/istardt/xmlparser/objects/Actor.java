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

    @JacksonXmlElementWrapper(localName = "goals")
    @JacksonXmlProperty(localName = "goal")
    @JsonManagedReference("actor-goals")
    private List<Goal> goals;

    @JacksonXmlElementWrapper(localName = "tasks")
    @JacksonXmlProperty(localName = "task")
    @JsonManagedReference("actor-tasks")
    private List<Task> tasks;

    @JsonIgnore
    private List<Effect> effects;

    @JacksonXmlElementWrapper(localName = "qualities")
    @JacksonXmlProperty(localName = "quality")
    @JsonManagedReference("actor-qualities")
    private List<Quality> qualities;
    private List<NonDecompositionElement> nonDecompElements;


    public Actor() {
        this.goals = new ArrayList<>();
        this.tasks = new ArrayList<>();
        this.qualities = new ArrayList<>();
        this.effects = new ArrayList<>();
        this.nonDecompElements = new ArrayList<>();

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

    public List<NonDecompositionElement> getNonDecompElements() {
        return nonDecompElements;
    }

    public void setNonDecompElements(List<NonDecompositionElement> nonDecompElements) {
        this.nonDecompElements = nonDecompElements;
    }

    public void addNonDecompElement(NonDecompositionElement element) {
        if (nonDecompElements == null) {
            nonDecompElements = new ArrayList<>();
        }
        nonDecompElements.add(element);
    }

    @Override
    public String toString() {
        return "Actor{id=" + getId() +
                ", effectCount=" + (effects != null ? effects.size() : 0) +
                ", taskCount=" + (tasks != null ? tasks.size() : 0) +
                ", qualityCount=" + (qualities != null ? qualities.size() : 0) +
                ", goalCount=" + (goals != null ? goals.size() : 0) +
                ", nonDecompElementsCount=" + (nonDecompElements != null ? nonDecompElements.size() : 0)  + "}";
    }
}