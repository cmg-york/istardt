package com.example.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Extensions to the Model class to add functionality needed for XML mapping
 */
public class Model {
    private List<Actor> actors;
    private Environment environment;

    public Model() {
        this.actors = new ArrayList<>();
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
     */
    public void addActor(Actor actor) {
        if (actors == null) {
            actors = new ArrayList<>();
        }
        actors.add(actor);
    }

    /**
     * Get an actor by name
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