package com.example.objects.builders;

import com.example.objects.Atom;
import com.example.objects.Effect;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder for Effect objects
 */
public class EffectBuilder {
    private final Effect effect;
    private final List<String> turnsTrue = new ArrayList<>();
    private final List<String> turnsFalse = new ArrayList<>();
    private final List<String> preConditions = new ArrayList<>();
    private final List<String> negPreConditions = new ArrayList<>();

    public EffectBuilder() {
        this.effect = new Effect();
    }

    public EffectBuilder withId(String id) {
        effect.setId(id);
        return this;
    }

    public EffectBuilder withRepresentation(String description) {
        Atom atom = new Atom();
        atom.setId(effect.getId());
        atom.setTitleText(description);
        effect.setAtom(atom);
        return this;
    }

    public EffectBuilder withProbability(float probability) {
        effect.setProbability(probability);
        return this;
    }

    public EffectBuilder isSatisfying(boolean satisfying) {
        effect.setSatisfying(satisfying);
        return this;
    }

    public EffectBuilder withTurnsTrue(String predicate) {
        turnsTrue.add(predicate);
        return this;
    }

    public EffectBuilder withTurnsFalse(String predicate) {
        turnsFalse.add(predicate);
        return this;
    }

    public EffectBuilder withPrecondition(String precondition) {
        preConditions.add(precondition);
        return this;
    }

    public EffectBuilder withNegPrecondition(String negPrecondition) {
        negPreConditions.add(negPrecondition);
        return this;
    }

    public EffectBuilder withAtom(Atom atom) {
        effect.setAtom(atom);
        return this;
    }

    public Effect build() {
        // The actual atom setting would be done when we have the atom references
        // This is just a placeholder for the concept

        return effect;
    }
}