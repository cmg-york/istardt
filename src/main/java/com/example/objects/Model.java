package com.example.objects;

import com.example.xml.deserializers.ModelDeserializer;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonRootName;
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
     * Add an actor to the model
     *
     * @param actor The actor to add
     */
    public void addActor(Actor actor) {
        if (actors == null) {
            actors = new ArrayList<>();
        }
        actors.add(actor);
    }

    /**
     * Get an actor by name
     *
     * @param name The name of the actor to find
     * @return The actor with the given name, or null if not found
     */
    public Actor getActorByName(String name) {
        if (actors != null) {
            for (Actor actor : actors) {
                if (actor.getName().equals(name)) {
                    return actor;
                }
            }
        }
        return null;
    }
}