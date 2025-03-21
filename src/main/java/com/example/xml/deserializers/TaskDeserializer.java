package com.example.xml.deserializers;

import com.example.objects.*;
import com.example.xml.ReferenceResolver;
import com.example.xml.utils.DeserializerUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
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
    protected Task createNewElement() {
        return new Task();
    }

    @Override
    protected void handleSpecificAttributes(Task task, JsonNode node, JsonParser p, DeserializationContext ctxt) throws IOException {
        // Process string list properties
        Map<String, BiConsumer<Task, String>> propertyConfigs = new HashMap<>();
        propertyConfigs.put("pre", Task::addPrecondition);
        propertyConfigs.put("npr", Task::addNegPrecondition);
        processStringListProperties(task, node, propertyConfigs);

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
            DeserializerUtils.handleDeserializationError(LOGGER,
                    "Error processing effects for task " + task.getAtom().getTitleText(), e);
        }
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

        // Set specific attributes
        boolean satisfying = DeserializerUtils.getBooleanAttribute(effectNode, "satisfying", true);
        float probability = DeserializerUtils.getFloatAttribute(effectNode, "probability", 1.0f);
        effect.setSatisfying(satisfying);
        effect.setProbability(probability);

        // Create an atom using the base deserializer method
        Atom atom = createAtom(name, description);
        effect.setAtom(atom);

        // Process string list properties more efficiently
        Map<String, BiConsumer<Effect, List<String>>> listSetters = new HashMap<>();
        listSetters.put("turnsTrue", Effect::setTurnsTrue);
        listSetters.put("turnsFalse", Effect::setTurnsFalse);
        listSetters.put("pre", Effect::setPreconditions);
        listSetters.put("npr", Effect::setNegPreconditions);

        // Apply all list-based properties
        for (Map.Entry<String, BiConsumer<Effect, List<String>>> entry : listSetters.entrySet()) {
            List<String> values = DeserializerUtils.getStringList(effectNode, entry.getKey());
            entry.getValue().accept(effect, values);
        }

        // Register the effect for reference resolution
        ReferenceResolver.getInstance().registerElement(uuid, effect);

        return effect;
    }
}