package ca.yorku.cmg.istardt.xmlparser.objects;

import ca.yorku.cmg.istardt.xmlparser.xml.deserializers.ModelDeserializer;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Root model class containing actors and environment.
 * Modified to support Jackson XML unmarshalling.
 */
@JacksonXmlRootElement(localName = "istar-model")
@JsonDeserialize(using = ModelDeserializer.class)
public class Model {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "actor")
    @JsonManagedReference("model-actors")
    private List<Actor> actors;

    private Environment environment;

    public Model() {
        this.actors = new ArrayList<>();
        this.environment = new Environment();
    }

    public List<Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    /**
     * Returns a string representation of this model.
     * Includes counts of actors and environment information.
     *
     * @return A string representation of this model
     */
    @Override
    public String toString() {
        int actorCount = actors != null ? actors.size() : 0;
        int envElementCount = environment != null && environment.getNonDecompElements() != null ?
                environment.getNonDecompElements().size() : 0;

        return "Model{actors=" + actorCount +
                ", environmentElements=" + envElementCount + "}";
    }
}