package ca.yorku.cmg.istardt.xmlparser.objects;

import ca.yorku.cmg.istardt.xmlparser.xml.deserializers.ModelDeserializer;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;
import java.util.List;

@JacksonXmlRootElement(localName = "istar-model")
@JsonDeserialize(using = ModelDeserializer.class)
public class Model {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "actor")
    @JsonManagedReference("model-actors")
    private List<Actor> actors;

    private ModelHeader modelHeader;

    public Model() {
        this.actors = new ArrayList<>();
    }
    public List<Actor> getActors() {
        return actors;
    }
    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

    public ModelHeader getModelHeader() {
        return modelHeader;
    }

    public void setModelHeader(ModelHeader modelHeader) {
        this.modelHeader = modelHeader;
    }
}