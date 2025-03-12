package com.example.xml.mappers;

import com.example.objects.*;
import com.example.xml.dto.ActorDto;
import java.util.ArrayList;
import java.util.List;

/**
 * Main mapper for converting from ActorDto to a complete Model.
 * This is the top-level mapper that coordinates other mappers.
 */
public class ModelMapper {

    private final ActorMapper actorMapper;

    /**
     * Constructor with default mappers.
     */
    public ModelMapper() {
        this.actorMapper = new ActorMapper();
    }

    /**
     * Constructor with custom mapper for testing or extension.
     */
    public ModelMapper(ActorMapper actorMapper) {
        this.actorMapper = actorMapper;
    }

    /**
     * Maps an ActorDto to a complete Model.
     *
     * @param actorDto The actor DTO from XML
     * @return The populated domain model
     */
    public Model fromDto(ActorDto actorDto) {
        // Create the model and environment
        Model model = new Model();
        Environment environment = new Environment();

        // Map the actor DTO to an actor domain object
        Actor actor = actorMapper.fromDto(actorDto, environment);

        // Add the actor to the model
        List<Actor> actors = new ArrayList<>();
        actors.add(actor);
        model.setActors(actors);

        // Set the environment in the model
        model.setEnvironment(environment);

        // Process any cross-references or post-processing
        actorMapper.processReferences(model);

        return model;
    }
}