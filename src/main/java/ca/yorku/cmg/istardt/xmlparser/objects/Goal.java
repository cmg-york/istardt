package ca.yorku.cmg.istardt.xmlparser.objects;

import ca.yorku.cmg.istardt.xmlparser.xml.deserializers.GoalDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.ArrayList;
import java.util.List;

@JsonDeserialize(using = GoalDeserializer.class)
public class Goal extends DecompositionElement {
    private int runs;
    private boolean root;
    private List<String> childGoalRefs;
    private List<String> childTaskRefs;
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