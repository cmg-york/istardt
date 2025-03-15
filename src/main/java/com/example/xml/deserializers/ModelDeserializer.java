package com.example.xml.deserializers;

import com.example.objects.Actor;
import com.example.objects.Environment;
import com.example.objects.Model;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Deserializer for Model objects.
 * Handles the conversion of XML root element to Model domain object.
 * Note that this doesn't extend BaseDeserializer since Model doesn't extend Element.
 */
public class ModelDeserializer extends StdDeserializer<Model> {
    private static final Logger LOGGER = Logger.getLogger(ModelDeserializer.class.getName());

    /**
     * Constructor.
     */
    public ModelDeserializer() {
        super(Model.class);
    }

    @Override
    public Model deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        // Create new Model
        Model model = new Model();

        // Create Environment
        Environment environment = new Environment();
        model.setEnvironment(environment);

        // Determine if this is a single actor or multiple actors
        List<Actor> actors = new ArrayList<>();

        // If root node is an actor, deserialize it directly
        if (node.has("name")) {
            Actor actor = ctxt.readValue(node.traverse(p.getCodec()), Actor.class);
            actors.add(actor);
            LOGGER.info("Deserialized single actor model");
        }
        // If root has multiple actor elements, deserialize each one
        else if (node.has("actor")) {
            JsonNode actorNodes = node.get("actor");
            if (actorNodes.isArray()) {
                for (JsonNode actorNode : actorNodes) {
                    Actor actor = ctxt.readValue(actorNode.traverse(p.getCodec()), Actor.class);
                    actors.add(actor);
                }
            } else {
                Actor actor = ctxt.readValue(actorNodes.traverse(p.getCodec()), Actor.class);
                actors.add(actor);
            }
            LOGGER.info("Deserialized multi-actor model with " + actors.size() + " actors");
        }

        model.setActors(actors);
        return model;
    }

    /**
     * Extracts the name/ID attribute from a node.
     *
     * @param node The JSON node to extract from
     * @return The name/ID attribute, or null if not found
     */
    protected String getName(JsonNode node) {
        return node.has("name") ? node.get("name").asText() : null;
    }

    /**
     * Extracts the description attribute from a node.
     *
     * @param node The JSON node to extract from
     * @return The description attribute, or null if not found
     */
    protected String getDescription(JsonNode node) {
        return node.has("description") ? node.get("description").asText() : null;
    }
}