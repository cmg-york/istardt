package com.example.objects;

import com.example.xml.ReferenceResolver;
import com.example.xml.deserializers.QualityDeserializer;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * Quality class representing a measurable attribute.
 * Modified to support Jackson XML unmarshalling.
 */
@JsonDeserialize(using = QualityDeserializer.class)
public class Quality extends NonDecompositionElement {

    @JacksonXmlProperty(isAttribute = true)
    private boolean root = false;

    @JacksonXmlProperty(isAttribute = true)
    private boolean exported = false;

    @JsonBackReference("actor-qualities")
    private Actor actor;

    /**
     * Default constructor.
     */
    public Quality() {
        super();
    }

    /**
     * Check if this quality is a root quality.
     *
     * @return True if this is a root quality, false otherwise
     */
    public boolean isRoot() {
        return root;
    }

    /**
     * Set whether this quality is a root quality.
     *
     * @param isRoot True if this is a root quality, false otherwise
     */
    public void setRoot(boolean isRoot) {
        this.root = isRoot;
    }

    /**
     * Check if this quality is exported.
     *
     * @return True if this quality is exported, false otherwise
     */
    public boolean isExported() {
        return exported;
    }

    /**
     * Set whether this quality is exported.
     *
     * @param exported True if this quality is exported, false otherwise
     */
    public void setExported(boolean exported) {
        this.exported = exported;
    }

    @Override
    public Formula getFormula() {
        return super.getFormula();
    }
}