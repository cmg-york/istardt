package com.example.xml.deserializers;

import com.example.objects.*;
import com.example.xml.utils.DeserializerUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Deserializer for Actor objects.
 * This deserializer handles the conversion of XML actor elements to Actor domain objects.
 */
public class ActorDeserializer extends BaseDeserializer<Actor> {
    private static final Logger LOGGER = Logger.getLogger(ActorDeserializer.class.getName());

    public ActorDeserializer() {
        super(Actor.class);
    }

    @Override
    public Actor deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        // Create new Actor
        Actor actor = new Actor();

        // Generate a UUID for the element ID
        String uuid = UUID.randomUUID().toString();
        actor.setId(uuid);

        // Get the name attribute from XML
        String name = DeserializerUtils.getStringAttribute(node, "name", null);
        String description = DeserializerUtils.getStringAttribute(node, "description", null);

        // Set the name attribute
        actor.setName(name);

        // Create an atom with a UUID as its ID
        Atom atom = new Atom();
        atom.setId(UUID.randomUUID().toString());

        // IMPORTANT: Set the titleText to the name from XML for content-based comparison
        atom.setTitleText(name);

        // Set description if available
        if (description != null) {
            atom.setTitleHTMLText(description);
        }

        // Set the atom as the actor's representation
        actor.setRepresentation(atom);

        // Register the actor for reference resolution
        registerElement(actor);

        try {
            // Process predicates (create Atoms)
            if (node.has("predicates") && node.get("predicates").has("predicate")) {
                JsonNode predicatesNode = node.get("predicates").get("predicate");
                List<Atom> predicates = deserializePredicates(predicatesNode);
                // In a real implementation, store these atoms somewhere or add to environment
            }

            // Process preBoxes (conditions)
            if (node.has("preBoxes") && node.get("preBoxes").has("preBox")) {
                JsonNode preBoxesNode = node.get("preBoxes").get("preBox");
                List<Condition> conditions = DeserializerUtils.deserializeList(preBoxesNode, p, ctxt, Condition.class);
                // Add conditions to the environment in post-processing
            }

            // Process indirect effects
            if (node.has("indirectEffects") && node.get("indirectEffects").has("indirectEffect")) {
                JsonNode effectsNode = node.get("indirectEffects").get("indirectEffect");
                List<IndirectEffect> indirectEffects = DeserializerUtils.deserializeList(effectsNode, p, ctxt, IndirectEffect.class);
                // Add indirect effects to the environment in post-processing
            }

            // Process qualities
            if (node.has("qualities") && node.get("qualities").has("quality")) {
                JsonNode qualitiesNode = node.get("qualities").get("quality");
                List<Quality> qualities = DeserializerUtils.deserializeList(qualitiesNode, p, ctxt, Quality.class);
                actor.setQualities(qualities);
            }

            // Process goals
            if (node.has("goals") && node.get("goals").has("goal")) {
                JsonNode goalsNode = node.get("goals").get("goal");
                List<Goal> goals = DeserializerUtils.deserializeList(goalsNode, p, ctxt, Goal.class);
                actor.setGoals(goals);
            }

            // Process tasks
            if (node.has("tasks") && node.get("tasks").has("task")) {
                JsonNode tasksNode = node.get("tasks").get("task");
                List<Task> tasks = DeserializerUtils.deserializeList(tasksNode, p, ctxt, Task.class);
                actor.setTasks(tasks);
            }
        } catch (IOException e) {
            DeserializerUtils.handleDeserializationError(LOGGER, "Error deserializing actor " + name, e);
        }

        LOGGER.info("Deserialized actor: " + name);
        return actor;
    }

    /**
     * Deserializes predicates into Atom objects.
     *
     * @param predicatesNode The JSON node containing predicate elements
     * @return A list of Atom objects
     */
    private List<Atom> deserializePredicates(JsonNode predicatesNode) {
        List<Atom> atoms = new ArrayList<>();

        if (predicatesNode.isArray()) {
            for (JsonNode predicateNode : predicatesNode) {
                atoms.add(deserializePredicate(predicateNode));
            }
        } else {
            atoms.add(deserializePredicate(predicatesNode));
        }

        return atoms;
    }

    /**
     * Deserializes a single predicate into an Atom.
     *
     * @param predicateNode The JSON node representing a predicate
     * @return An Atom object
     */
    private Atom deserializePredicate(JsonNode predicateNode) {
        String value = predicateNode.asText().trim();
        String description = DeserializerUtils.getStringAttribute(predicateNode, "description", null);

        Atom atom = new Atom();
        atom.setId(UUID.randomUUID().toString()); // Generate UUID for atom ID
        atom.setTitleText(value); // Use predicate value as titleText

        if (description != null) {
            atom.setTitleHTMLText(description);
        }

        // We don't register atoms in the reference resolver as they don't extend Element
        return atom;
    }
}