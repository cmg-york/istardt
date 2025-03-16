package com.example.xml.deserializers;

import com.example.objects.*;
import com.example.xml.ReferenceResolver;
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

        // Generate a UUID for the element ID
        String uuid = UUID.randomUUID().toString();
        task.setId(uuid);

        // Get the name attribute from XML
        String name = DeserializerUtils.getStringAttribute(node, "name", null);
        String description = DeserializerUtils.getStringAttribute(node, "description", null);

        // Create an atom with a UUID as its ID
        Atom atom = new Atom();
        atom.setId(UUID.randomUUID().toString());

        // IMPORTANT: Set the titleText to the name from XML for content-based comparison
        atom.setTitleText(name);

        // Set description if available
        if (description != null) {
            atom.setTitleHTMLText(description);
        }

        // Set the atom as the task's representation
        task.setRepresentation(atom);

        // Process preconditions
        List<String> preconditions = DeserializerUtils.getStringList(node, "pre");
        for (String pre : preconditions) {
            task.addPrecondition(pre);
        }

        // Process negative preconditions
        List<String> negPreconditions = DeserializerUtils.getStringList(node, "npr");
        for (String npr : negPreconditions) {
            task.addNegPrecondition(npr);
        }

        // Process effect group
        try {
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
        } catch (IOException e) {
            DeserializerUtils.handleDeserializationError(LOGGER, "Error processing effects for task " + name, e);
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

        // Generate a UUID for the element ID
        String uuid = UUID.randomUUID().toString();
        effect.setId(uuid);

        // Get the attributes from XML
        String name = DeserializerUtils.getStringAttribute(effectNode, "name", null);
        String description = DeserializerUtils.getStringAttribute(effectNode, "description", null);
        boolean satisfying = DeserializerUtils.getBooleanAttribute(effectNode, "satisfying", true);
        float probability = DeserializerUtils.getFloatAttribute(effectNode, "probability", 1.0f);

        effect.setSatisfying(satisfying);
        effect.setProbability(probability);

        // Create an atom with a UUID as its ID
        Atom atom = new Atom();
        atom.setId(UUID.randomUUID().toString());

        // IMPORTANT: Set the titleText to the name from XML for content-based comparison
        atom.setTitleText(name);

        // Set description if available
        if (description != null) {
            atom.setTitleHTMLText(description);
        }

        // Set the atom as the effect's representation
        effect.setAtom(atom);

        // Process turnsTrue
        List<String> turnsTrue = DeserializerUtils.getStringList(effectNode, "turnsTrue");
        effect.setTurnsTrue(turnsTrue);

        // Process turnsFalse
        List<String> turnsFalse = DeserializerUtils.getStringList(effectNode, "turnsFalse");
        effect.setTurnsFalse(turnsFalse);

        // Process preconditions
        List<String> preconditions = DeserializerUtils.getStringList(effectNode, "pre");
        effect.setPreconditions(preconditions);

        // Process negative preconditions
        List<String> negPreconditions = DeserializerUtils.getStringList(effectNode, "npr");
        effect.setNegPreconditions(negPreconditions);

        // Register the effect for reference resolution
        ReferenceResolver.getInstance().registerElement(uuid, effect);

        return effect;
    }
}