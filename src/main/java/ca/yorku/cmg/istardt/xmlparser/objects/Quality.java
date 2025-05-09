package ca.yorku.cmg.istardt.xmlparser.objects;

import ca.yorku.cmg.istardt.xmlparser.xml.deserializers.QualityDeserializer;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonDeserialize(using = QualityDeserializer.class)
public class Quality extends NonDecompositionElement {

    @JacksonXmlProperty(isAttribute = true)
    private boolean root = false;

    private float init;

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

    public float getInit() {
        return init;
    }

    public void setInit(float init) {
        this.init = init;
    }

    @Override
    public Formula getFormula() {
        return super.getFormula();
    }
}