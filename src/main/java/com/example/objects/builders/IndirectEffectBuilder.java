package com.example.objects.builders;


import com.example.objects.Atom;
import com.example.objects.Formula;
import com.example.objects.IndirectEffect;

/**
 * Builder for IndirectEffect objects
 */
public class IndirectEffectBuilder {
    private final IndirectEffect indirectEffect;

    public IndirectEffectBuilder() {
        this.indirectEffect = new IndirectEffect();
    }

    public IndirectEffectBuilder withId(String id) {
        indirectEffect.setId(id);
        return this;
    }

    public IndirectEffectBuilder withRepresentation(String description) {
        Atom atom = new Atom();
        atom.setId(indirectEffect.getId());
        atom.setTitleText(description);
        indirectEffect.setAtom(atom);
        return this;
    }

    public IndirectEffectBuilder isExported(boolean exported) {
        indirectEffect.setExported(exported);
        return this;
    }

    public IndirectEffectBuilder withFormula(Formula formula) {
        indirectEffect.setValueFormula(formula);
        return this;
    }

    public IndirectEffect build() {
        return indirectEffect;
    }
}