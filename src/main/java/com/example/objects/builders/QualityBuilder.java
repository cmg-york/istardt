package com.example.objects.builders;

import com.example.objects.Atom;
import com.example.objects.Formula;
import com.example.objects.Quality;

/**
 * Builder for Quality objects
 */
public class QualityBuilder {
    private final Quality quality;

    public QualityBuilder() {
        this.quality = new Quality();
    }

    public QualityBuilder withId(String id) {
        quality.setId(id);
        return this;
    }

    public QualityBuilder withRepresentation(String description) {
        Atom atom = new Atom();
        atom.setId(quality.getId());
        atom.setTitleText(description);
        quality.setAtom(atom);
        return this;
    }

    public QualityBuilder isRoot(boolean isRoot) {
        quality.setRoot(isRoot);
        return this;
    }

    public QualityBuilder withFormula(Formula formula) {
        quality.setValueFormula(formula);
        return this;
    }

    public Quality build() {
        return quality;
    }
}
