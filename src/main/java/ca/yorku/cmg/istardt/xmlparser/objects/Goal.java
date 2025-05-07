package ca.yorku.cmg.istardt.xmlparser.objects;

import ca.yorku.cmg.istardt.xmlparser.xml.deserializers.GoalDeserializer;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.ArrayList;
import java.util.List;

@JsonDeserialize(using = GoalDeserializer.class)
public class Goal extends DecompositionElement {

    @JacksonXmlProperty(isAttribute = true)
    private int runs;

    @JacksonXmlProperty(isAttribute = true)
    private boolean root;

    @JsonIgnore
    private List<String> childGoalRefs;

    @JsonIgnore
    private List<String> childTaskRefs;

    @JsonBackReference("actor-goals")
    private Actor actor;

    public Goal() {
        super();
        this.childGoalRefs = new ArrayList<>();
        this.childTaskRefs = new ArrayList<>();
    }

    public int getRuns() {
        return runs;
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }

    public boolean isRoot() {
        return root;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }

    public List<String> getChildGoalRefs() {
        return childGoalRefs;
    }

    public void setChildGoalRefs(List<String> childGoalRefs) {
        this.childGoalRefs = childGoalRefs;
    }

    public List<String> getChildTaskRefs() {
        return childTaskRefs;
    }

    public void setChildTaskRefs(List<String> childTaskRefs) {
        this.childTaskRefs = childTaskRefs;
    }

    public void addChildGoalRef(String childGoalRef) {
        if (childGoalRefs == null) {
            childGoalRefs = new ArrayList<>();
        }
        childGoalRefs.add(childGoalRef);
    }

    public void addChildTaskRef(String childTaskRef) {
        if (childTaskRefs == null) {
            childTaskRefs = new ArrayList<>();
        }
        childTaskRefs.add(childTaskRef);
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    /**
     * Override toString to prevent infinite recursion from parent-child circular references.
     * Includes Goal-specific information while avoiding circular references.
     */
    @Override
    public String toString() {
        return "Goal{id=" + getId() +
                ", root=" + root +
                ", decompType=" + getDecompType() +
                ", runs=" + runs +
                ", childCount=" + (getChildren() != null ? getChildren().size() : 0) +
                ", hasParent=" + (getParent() != null) + "}";
    }
}