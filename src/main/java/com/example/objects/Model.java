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
import java.util.Objects;
import java.util.stream.Collectors;

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

    /**
     * Compares this model to the specified object.
     * Models are considered equal if they have similar actors (based on names)
     * and environment.
     *
     * @param obj The object to compare this model to
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Model model = (Model) obj;

        // Compare actor names
        List<String> thisActorNames = actors != null ?
                actors.stream().map(Actor::getName).sorted().collect(Collectors.toList()) :
                new ArrayList<>();

        List<String> thatActorNames = model.actors != null ?
                model.actors.stream().map(Actor::getName).sorted().collect(Collectors.toList()) :
                new ArrayList<>();

        return thisActorNames.equals(thatActorNames) &&
                Objects.equals(environment, model.environment);
    }

    /**
     * Returns a hash code value for this model.
     * The hash code is computed based on the actor names and environment
     * to ensure it satisfies the contract with equals().
     *
     * @return A hash code value for this model
     */
    @Override
    public int hashCode() {
        List<String> actorNames = actors != null ?
                actors.stream().map(Actor::getName).sorted().collect(Collectors.toList()) :
                new ArrayList<>();

        return Objects.hash(actorNames, environment);
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