package ca.yorku.cmg.istardt.xmlparser.objects;

import ca.yorku.cmg.istardt.xmlparser.xml.deserializers.TaskDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

@JsonDeserialize(using = TaskDeserializer.class)
public class Task extends DecompositionElement {
    private List<Effect> effects;

    public Task() {
        this.effects = new ArrayList<>();
        this.setDecompType(DecompType.TERM); // Set default DecompType to TERM
    }

    public void setEffects(List<Effect> effects) {
        this.effects = effects;

        // Set up the relationship between task and effects
        for (Effect effect : effects) {
            effect.setTask(this);
        }
    }

    public List<Effect> getEffects() {
        return effects;
    }

    /**
     * A task is deterministic if it has only one effect with 100% probability.
     *
     * @return True if the task is deterministic, false otherwise
     */
    public boolean isDeterministic() {
        return effects != null && effects.size() == 1 && effects.get(0).getProbability() == 1.0f;
    }
    @Override
    public String toString() {
        return "Task{id=" + getId() +
                ", effectCount=" + (effects != null ? effects.size() : 0) +
                ", deterministic=" + isDeterministic() +
                ", decompType=" + getDecompType() +
                ", childCount=" + (getChildren() != null ? getChildren().size() : 0) +
                ", hasParent=" + (getParent() != null) + "}";
    }
}