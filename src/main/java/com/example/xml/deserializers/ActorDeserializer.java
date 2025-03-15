package com.example.xml.deserializers;

import com.example.objects.*;
import com.example.xml.ReferenceResolver;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

/**
 * Deserializer for Actor objects.
 * This deserializer handles the conversion of XML actor elements to Actor domain objects.
 */
public class ActorDeserializer extends BaseDeserializer<Actor> {
    private static final Logger LOGGER = Logger.getLogger(ActorDeserializer.class.getName());

    /**
     * Constructor.
     */
    public ActorDeserializer() {
        super(Actor.class);
    }

    @Override
    public Actor deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        // Create new Actor
        Actor actor = new Actor();

        // Set ID/name
        String name = getName(node);
        actor.setName(name);
        actor.setId(name); // Using name as ID for actors

        // Register the actor for reference resolution
        registerElement(actor);

        // Process predicates (create Atoms)
        if (node.has("predicates") && node.get("predicates").has("predicate")) {
            JsonNode predicatesNode = node.get("predicates").get("predicate");
            List<Atom> predicates = deserializePredicates(predicatesNode);
            // In a real implementation, store these atoms somewhere or add to environment
        }

        // Process preBoxes (conditions)
        if (node.has("preBoxes") && node.get("preBoxes").has("preBox")) {
            JsonNode preBoxesNode = node.get("preBoxes").get("preBox");
            List<Condition> conditions = deserializeConditions(preBoxesNode, p, ctxt);
            // Add conditions to the environment in post-processing
        }

        // Process indirect effects
        if (node.has("indirectEffects") && node.get("indirectEffects").has("indirectEffect")) {
            JsonNode effectsNode = node.get("indirectEffects").get("indirectEffect");
            List<IndirectEffect> indirectEffects = deserializeIndirectEffects(effectsNode, p, ctxt);
            // Add indirect effects to the environment in post-processing
        }

        // Process qualities
        if (node.has("qualities") && node.get("qualities").has("quality")) {
            JsonNode qualitiesNode = node.get("qualities").get("quality");
            List<Quality> qualities = deserializeQualities(qualitiesNode, p, ctxt);
            actor.setQualities(qualities);
        }

        // Process goals
        if (node.has("goals") && node.get("goals").has("goal")) {
            JsonNode goalsNode = node.get("goals").get("goal");
            List<Goal> goals = deserializeGoals(goalsNode, p, ctxt);
            actor.setGoals(goals);
        }

        // Process tasks
        if (node.has("tasks") && node.get("tasks").has("task")) {
            JsonNode tasksNode = node.get("tasks").get("task");
            List<Task> tasks = deserializeTasks(tasksNode, p, ctxt);
            actor.setTasks(tasks);
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
        String description = getDescription(predicateNode);

        Atom atom = new Atom();
        atom.setId(value);
        atom.setTitleText(description);

        // We don't register atoms in the reference resolver as they don't extend Element

        return atom;
    }

    /**
     * Deserializes condition (preBox) elements.
     *
     * @param preBoxNodes The JSON node containing preBox elements
     * @param parser The JSON parser
     * @param ctxt The deserialization context
     * @return A list of Condition objects
     * @throws IOException If there's an error during deserialization
     */
    private List<Condition> deserializeConditions(JsonNode preBoxNodes, JsonParser parser, DeserializationContext ctxt) throws IOException {
        List<Condition> conditions = new ArrayList<>();

        if (preBoxNodes.isArray()) {
            for (JsonNode preBoxNode : preBoxNodes) {
                conditions.add(deserializeCondition(preBoxNode, parser, ctxt));
            }
        } else {
            conditions.add(deserializeCondition(preBoxNodes, parser, ctxt));
        }

        return conditions;
    }

    /**
     * Deserializes a single condition (preBox).
     *
     * @param preBoxNode The JSON node representing a preBox
     * @param parser The JSON parser
     * @param ctxt The deserialization context
     * @return A Condition object
     * @throws IOException If there's an error during deserialization
     */
    private Condition deserializeCondition(JsonNode preBoxNode, JsonParser parser, DeserializationContext ctxt) throws IOException {
        // Use the specific deserializer for conditions
        return ctxt.readValue(preBoxNode.traverse(parser.getCodec()), Condition.class);
    }

    /**
     * Deserializes indirect effect elements.
     *
     * @param effectNodes The JSON node containing indirectEffect elements
     * @param parser The JSON parser
     * @param ctxt The deserialization context
     * @return A list of IndirectEffect objects
     * @throws IOException If there's an error during deserialization
     */
    private List<IndirectEffect> deserializeIndirectEffects(JsonNode effectNodes, JsonParser parser, DeserializationContext ctxt) throws IOException {
        List<IndirectEffect> indirectEffects = new ArrayList<>();

        if (effectNodes.isArray()) {
            for (JsonNode effectNode : effectNodes) {
                indirectEffects.add(ctxt.readValue(effectNode.traverse(parser.getCodec()), IndirectEffect.class));
            }
        } else {
            indirectEffects.add(ctxt.readValue(effectNodes.traverse(parser.getCodec()), IndirectEffect.class));
        }

        return indirectEffects;
    }

    /**
     * Deserializes quality elements.
     *
     * @param qualityNodes The JSON node containing quality elements
     * @param parser The JSON parser
     * @param ctxt The deserialization context
     * @return A list of Quality objects
     * @throws IOException If there's an error during deserialization
     */
    private List<Quality> deserializeQualities(JsonNode qualityNodes, JsonParser parser, DeserializationContext ctxt) throws IOException {
        List<Quality> qualities = new ArrayList<>();

        if (qualityNodes.isArray()) {
            for (JsonNode qualityNode : qualityNodes) {
                Quality quality = ctxt.readValue(qualityNode.traverse(parser.getCodec()), Quality.class);
                qualities.add(quality);
            }
        } else {
            Quality quality = ctxt.readValue(qualityNodes.traverse(parser.getCodec()), Quality.class);
            qualities.add(quality);
        }

        return qualities;
    }

    /**
     * Deserializes goal elements.
     *
     * @param goalNodes The JSON node containing goal elements
     * @param parser The JSON parser
     * @param ctxt The deserialization context
     * @return A list of Goal objects
     * @throws IOException If there's an error during deserialization
     */
    private List<Goal> deserializeGoals(JsonNode goalNodes, JsonParser parser, DeserializationContext ctxt) throws IOException {
        List<Goal> goals = new ArrayList<>();

        if (goalNodes.isArray()) {
            for (JsonNode goalNode : goalNodes) {
                Goal goal = ctxt.readValue(goalNode.traverse(parser.getCodec()), Goal.class);
                goals.add(goal);
            }
        } else {
            Goal goal = ctxt.readValue(goalNodes.traverse(parser.getCodec()), Goal.class);
            goals.add(goal);
        }

        return goals;
    }

    /**
     * Deserializes task elements.
     *
     * @param taskNodes The JSON node containing task elements
     * @param parser The JSON parser
     * @param ctxt The deserialization context
     * @return A list of Task objects
     * @throws IOException If there's an error during deserialization
     */
    private List<Task> deserializeTasks(JsonNode taskNodes, JsonParser parser, DeserializationContext ctxt) throws IOException {
        List<Task> tasks = new ArrayList<>();

        if (taskNodes.isArray()) {
            for (JsonNode taskNode : taskNodes) {
                Task task = ctxt.readValue(taskNode.traverse(parser.getCodec()), Task.class);
                tasks.add(task);
            }
        } else {
            Task task = ctxt.readValue(taskNodes.traverse(parser.getCodec()), Task.class);
            tasks.add(task);
        }

        return tasks;
    }
}