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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Enhanced deserializer for Task objects with improved formula handling
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
        LOGGER.info("Processing task: " + task.getId());

        // Process pre formula with detailed logging
        if (node.has("pre")) {
            JsonNode preNode = node.get("pre");
            LOGGER.info("Processing pre node for task " + task.getId() + ": " + preNode.toString());

            if (preNode.has("formula")) {
                try {
                    LOGGER.info("Found formula element in pre node: " + preNode.get("formula").toString());
                    Formula formula = ctxt.readValue(preNode.get("formula").traverse(p.getCodec()), Formula.class);
                    task.setPreFormula(formula);
                    LOGGER.info("Successfully set preFormula: " + formula.getFormula());
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Error processing pre formula for task " + task.getId() + ": " + e.getMessage(), e);
                }
            } else {
                LOGGER.warning("Pre node exists but formula element not found in task " + task.getId());
            }
        }

        // Process npr formula with detailed logging
        if (node.has("npr")) {
            JsonNode nprNode = node.get("npr");
            LOGGER.info("Processing npr node for task " + task.getId() + ": " + nprNode.toString());

            if (nprNode.has("formula")) {
                try {
                    LOGGER.info("Found formula element in npr node: " + nprNode.get("formula").toString());
                    Formula formula = ctxt.readValue(nprNode.get("formula").traverse(p.getCodec()), Formula.class);
                    task.setNprFormula(formula);
                    LOGGER.info("Successfully set nprFormula: " + formula.getFormula());
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Error processing npr formula for task " + task.getId() + ": " + e.getMessage(), e);
                }
            } else {
                LOGGER.warning("Npr node exists but formula element not found in task " + task.getId());
            }
        }

        // Process effect group
        try {
            if (node.has("effectGroup") && node.get("effectGroup").has("effect")) {
                JsonNode effectGroupNode = node.get("effectGroup");
                JsonNode effectNodes = effectGroupNode.get("effect");
                LOGGER.info("Processing effect group for task " + task.getId());

                List<Effect> effects = new ArrayList<>();

                if (effectNodes.isArray()) {
                    for (JsonNode effectNode : effectNodes) {
                        Effect effect = deserializeEffect(effectNode, p, ctxt);
                        effect.setTask(task);
                        effects.add(effect);
                    }
                } else {
                    Effect effect = deserializeEffect(effectNodes, p, ctxt);
                    effect.setTask(task);
                    effects.add(effect);
                }

                task.setEffects(effects);
                LOGGER.info("Successfully processed " + effects.size() + " effects for task " + task.getId());
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error processing effects for task " + task.getId() + ": " + e.getMessage(), e);
        }
    }

    /**
     * Deserialize an effect within a task with improved formula handling.
     *
     * @param effectNode The JSON node containing effect data
     * @param ctxt The deserialization context
     * @return The deserialized Effect object
     * @throws IOException If there's an error during deserialization
     */
    private Effect deserializeEffect(JsonNode effectNode, JsonParser p, DeserializationContext ctxt) throws IOException {
        Effect effect = new Effect();

        // Generate a UUID for the element ID
        String uuid = UUID.randomUUID().toString();
        effect.setId(uuid);

        // Get the attributes from XML
        String name = DeserializerUtils.getStringAttribute(effectNode, "name", null);
        String description = DeserializerUtils.getStringAttribute(effectNode, "description", null);

        // Create an atom using the base deserializer method
        Atom atom = createAtom(name, description);
        effect.setAtom(atom);

        // Set specific attributes
        boolean satisfying = DeserializerUtils.getBooleanAttribute(effectNode, "satisfying", true);
        float probability = DeserializerUtils.getFloatAttribute(effectNode, "probability", 1.0f);
        effect.setSatisfying(satisfying);
        effect.setProbability(probability);

        // Process string list properties for turnsTrue and turnsFalse
        Map<String, BiConsumer<Effect, List<String>>> listSetters = new HashMap<>();
        listSetters.put("turnsTrue", Effect::setTurnsTrue);
        listSetters.put("turnsFalse", Effect::setTurnsFalse);

        // Apply all list-based properties
        for (Map.Entry<String, BiConsumer<Effect, List<String>>> entry : listSetters.entrySet()) {
            List<String> values = DeserializerUtils.getStringList(effectNode, entry.getKey());
            entry.getValue().accept(effect, values);
        }

        // Process pre formula with detailed logging
        if (effectNode.has("pre")) {
            JsonNode preNode = effectNode.get("pre");
            LOGGER.info("Processing pre node for effect " + effect.getId() + ": " + preNode.toString());

            if (preNode.has("formula")) {
                try {
                    LOGGER.info("Found formula element in pre node: " + preNode.get("formula").toString());
                    Formula formula = ctxt.readValue(preNode.get("formula").traverse(p.getCodec()), Formula.class);
                    effect.setPreFormula(formula);
                    LOGGER.info("Successfully set preFormula for effect: " + formula.getFormula());
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Error processing pre formula for effect " + effect.getId() + ": " + e.getMessage(), e);
                }
            } else {
                LOGGER.warning("Pre node exists but formula element not found in effect " + effect.getId());
            }
        }

        // Process npr formula with detailed logging
        if (effectNode.has("npr")) {
            JsonNode nprNode = effectNode.get("npr");
            LOGGER.info("Processing npr node for effect " + effect.getId() + ": " + nprNode.toString());

            if (nprNode.has("formula")) {
                try {
                    LOGGER.info("Found formula element in npr node: " + nprNode.get("formula").toString());
                    Formula formula = ctxt.readValue(nprNode.get("formula").traverse(p.getCodec()), Formula.class);
                    effect.setNprFormula(formula);
                    LOGGER.info("Successfully set nprFormula for effect: " + formula.getFormula());
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Error processing npr formula for effect " + effect.getId() + ": " + e.getMessage(), e);
                }
            } else {
                LOGGER.warning("Npr node exists but formula element not found in effect " + effect.getId());
            }
        }

        // Register the effect for reference resolution
        ReferenceResolver.getInstance().registerElement(uuid, effect);
        LOGGER.info("Successfully registered effect: " + effect.getId() + " (" + effect.getAtom().getTitleText() + ")");

        return effect;
    }
}