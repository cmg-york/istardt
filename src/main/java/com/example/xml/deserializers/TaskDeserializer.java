package com.example.xml.deserializers;

import com.example.objects.*;
import com.example.xml.ReferenceResolver;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Deserializer for Task objects.
 */
public class TaskDeserializer extends BaseDeserializer<Task> {
    private static final Logger LOGGER = Logger.getLogger(TaskDeserializer.class.getName());

    public TaskDeserializer() {
        super(Task.class);
    }

    @Override
    public Task deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        // Create new Task
        Task task = new Task();

        // Set basic properties
        String name = getName(node);
        String description = getDescription(node);

        task.setId(name);

        // Create an atom for the task
        Atom atom = createAtom(name, description);
        task.setRepresentation(atom);

        // Process preconditions
        List<String> preconditions = getStringList(node, "pre");
        for (String pre : preconditions) {
            task.addPrecondition(pre);
        }

        // Process negative preconditions
        List<String> negPreconditions = getStringList(node, "npr");
        for (String npr : negPreconditions) {
            task.addNegPrecondition(npr);
        }

        // Process effect group
        if (node.has("effectGroup") && node.get("effectGroup").has("effect")) {
            JsonNode effectGroupNode = node.get("effectGroup");
            JsonNode effectNodes = effectGroupNode.get("effect");

            List<Effect> effects = new ArrayList<>();

            if (effectNodes.isArray()) {
                for (JsonNode effectNode : effectNodes) {
                    Effect effect = deserializeEffect(effectNode, ctxt);
                    effect.setTask(task);
                    effects.add(effect);
                }
            } else {
                Effect effect = deserializeEffect(effectNodes, ctxt);
                effect.setTask(task);
                effects.add(effect);
            }

            task.setEffects(effects);
        }

        // Register the task for reference resolution
        registerElement(task);

        return task;
    }

    /**
     * Deserialize an effect within a task.
     *
     * @param effectNode The JSON node containing effect data
     * @param ctxt The deserialization context
     * @return The deserialized Effect object
     * @throws IOException If there's an error during deserialization
     */
    private Effect deserializeEffect(JsonNode effectNode, DeserializationContext ctxt) throws IOException {
        Effect effect = new Effect();

        // Set basic properties
        String name = getName(effectNode);
        String description = getDescription(effectNode);
        boolean satisfying = getBooleanAttribute(effectNode, "satisfying", true);
        float probability = getFloatAttribute(effectNode, "probability", 1.0f);

        effect.setId(name);
        effect.setSatisfying(satisfying);
        effect.setProbability(probability);

        // Create an atom for the effect
        Atom atom = createAtom(name, description);
        effect.setAtom(atom);

        // Process turnsTrue
        List<String> turnsTrue = getStringList(effectNode, "turnsTrue");
        effect.setTurnsTrue(turnsTrue);

        // Process turnsFalse
        List<String> turnsFalse = getStringList(effectNode, "turnsFalse");
        effect.setTurnsFalse(turnsFalse);

        // Process preconditions
        List<String> preconditions = getStringList(effectNode, "pre");
        effect.setPreconditions(preconditions);

        // Process negative preconditions
        List<String> negPreconditions = getStringList(effectNode, "npr");
        effect.setNegPreconditions(negPreconditions);

        // Register the effect for reference resolution
        ReferenceResolver.getInstance().registerElement(name, effect);

        return effect;
    }
}