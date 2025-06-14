package ca.yorku.cmg.istardt.xmlparser.objects;

import ca.yorku.cmg.istardt.xmlparser.xml.deserializers.EffectDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@JsonDeserialize(using = EffectDeserializer.class)
public class Effect extends DecompositionElement {
    private float probability;
    private boolean satisfying = true;

    private List<String> turnsTrue;
    private List<String> turnsFalse;
    private Map<Variable, Float> sets;

    private Task task;

    public Effect() {
        this.turnsTrue = new ArrayList<>();
        this.turnsFalse = new ArrayList<>();
        this.sets = new HashMap<>();
        decompType = DecompType.TERM;
    }

    public boolean isSatisfying() {
        return satisfying;
    }

    public void setSatisfying(boolean satisfying) {
        this.satisfying = satisfying;
    }

    public Task getTask() {
        return task;
    }
    @Override
    public DecompositionElement getParent() {
        return task;
    }
    public void setTask(Task task) {
        this.task = task;
    }

    public List<String> getTurnsTrue() {
        return turnsTrue;
    }
    public void setTurnsTrue(List<String> turnsTrue) {
        this.turnsTrue = turnsTrue;
    }
    public List<String> getTurnsFalse() {
        return turnsFalse;
    }
    public void setTurnsFalse(List<String> turnsFalse) {
        this.turnsFalse = turnsFalse;
    }
    public void addTurnsTrue(String element) {
        turnsTrue.add(element);
    }
    public void addTurnsFalse(String element) {
        turnsFalse.add(element);
    }
    public Map<Variable, Float> getSets() {return sets;}
    public void setSets(Map<Variable, Float> sets) {this.sets = sets;}

    /**
     * Get the siblings of this effect (other effects from the same task).
     *
     * @return The list of sibling effects
     */
    @Override
    public List<DecompositionElement> getSiblings() {
        if (task == null) {
            return new ArrayList<>();
        }

        List<DecompositionElement> siblings = new ArrayList<>(task.getEffects());
        siblings.remove(this);
        return siblings;
    }

    /**
     * Check if this decompositionElement is a sibling of another effect.
     *
     * @param decompositionElement The DecompositionElement to check
     * @return True if they are siblings, false otherwise
     */
    @Override
    public boolean isSibling(DecompositionElement decompositionElement) {
        if (task == null || decompositionElement == null || decompositionElement.getParent() == null) {
            return false;
        }
        return task.equals(decompositionElement.getParent());
    }

    public float getProbability() {
        return probability;
    }
    public void setProbability(float probability) {
        this.probability = probability;
    }

    @Override
    public String toString() {
        return "Effect{id=" + getId() +
                ", probability=" + probability +
                ", satisfying=" + satisfying +
                ", turnsTrueCount=" + (turnsTrue != null ? turnsTrue.size() : 0) +
                ", turnsFalseCount=" + (turnsFalse != null ? turnsFalse.size() : 0) ;
    }
}