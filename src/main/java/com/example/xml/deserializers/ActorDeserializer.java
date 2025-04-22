package com.example.xml.deserializers;

import com.example.objects.*;
import com.example.xml.utils.DeserializerUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

/**
 * Deserializer for Actor objects.
 * This deserializer handles the conversion of XML actor elements to Actor domain objects.
 * Simplified to rely on base deserializer for handling name via atom.
 */
public class ActorDeserializer extends BaseDeserializer<Actor> {
    private static final Logger LOGGER = Logger.getLogger(ActorDeserializer.class.getName());

    public ActorDeserializer() {
        super(Actor.class);
    }

    @Override
    protected Actor createNewElement() {
        return new Actor();
    }

    @Override
    protected void handleSpecificAttributes(Actor actor, JsonNode node, JsonParser p, DeserializationContext ctxt) throws IOException {
        // The name is already handled by the base deserializer in extractCommonAttributes
        // which sets up the atom with the name from the XML

        try {
            // Process predicates (create Atoms)
            if (node.has("predicates") && node.get("predicates").has("predicate")) {
                JsonNode predicatesNode = node.get("predicates").get("predicate");
                List<Atom> predicates = deserializePredicates(predicatesNode);
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
            // Get name from atom for logging
            String name = actor.getAtom() != null ? actor.getAtom().getTitleText() : actor.getId();
            DeserializerUtils.handleDeserializationError(LOGGER, "Error deserializing actor " + name, e);
        }

        // Log with name from atom for consistency
        String name = actor.getAtom() != null ? actor.getAtom().getTitleText() : actor.getId();
        LOGGER.info("Deserialized actor: " + name);
    }

    /**
     * Deserializes predicates into Atom objects.
     *
     * @param predicatesNode The JSON node containing predicate elements
     * @return A list of Atom objects
     */
    private List<Atom> deserializePredicates(JsonNode predicatesNode) {
        return DeserializerUtils.processNodeItems(predicatesNode, this::deserializePredicate);
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
        atom.setId(java.util.UUID.randomUUID().toString()); // Generate UUID for atom ID
        atom.setTitleText(value); // Use predicate value as titleText

        if (description != null) {
            atom.setTitleHTMLText(description);
        }

        // Don't register atoms in the reference resolver as they don't extend Element
        return atom;
    }
}