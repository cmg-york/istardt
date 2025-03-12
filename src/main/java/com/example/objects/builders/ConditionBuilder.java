package com.example.objects.builders;

import com.example.objects.Atom;
import com.example.objects.Condition;
import com.example.objects.Formula;

/**
 * Builder for Condition objects
 */
public class ConditionBuilder {
    private final Condition condition;

    public ConditionBuilder() {
        this.condition = new Condition();
    }

    public ConditionBuilder withId(String id) {
        condition.setId(id);
        return this;
    }

    public ConditionBuilder withRepresentation(String description) {
        Atom atom = new Atom();
        atom.setId(condition.getId());
        atom.setTitleText(description);
        condition.setAtom(atom);
        return this;
    }

    public ConditionBuilder withFormula(Formula formula) {
        condition.setValueFormula(formula);
        return this;
    }

    public Condition build() {
        return condition;
    }
}