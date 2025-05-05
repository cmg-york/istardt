package ca.yorku.cmg.istardt.xmlparser.xml.deserializers;

import ca.yorku.cmg.istardt.xmlparser.xml.ReferenceResolver;
import ca.yorku.cmg.istardt.xmlparser.xml.utils.DeserializerUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import ca.yorku.cmg.istardt.xmlparser.objects.Atom;
import ca.yorku.cmg.istardt.xmlparser.objects.Effect;
import ca.yorku.cmg.istardt.xmlparser.objects.Formula;
import ca.yorku.cmg.istardt.xmlparser.objects.Task;

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
 * Deserializer for Task objects with formula handling
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
        // Process pre formula with detailed logging
        DeserializerUtils.logInfo(LOGGER, "Processing task: " + task.getId());
        Formula preFormula = DeserializerUtils.processFormula("pre", node, p, ctxt, LOGGER);
        if (preFormula != null) {
            task.setPreFormula(preFormula);
            DeserializerUtils.logInfo(LOGGER, "Set pre formula for task: " + task.getId());
        }

        // Process npr formula with detailed logging
        Formula nprFormula = DeserializerUtils.processFormula("npr", node, p, ctxt, LOGGER);
        if (nprFormula != null) {
            task.setNprFormula(nprFormula);
            DeserializerUtils.logInfo(LOGGER, "Set npr formula for task: " + task.getId());
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
        effect.setRepresentation(atom);

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
        Formula preFormula = DeserializerUtils.processFormula("pre", effectNode, p, ctxt, LOGGER);
        if (preFormula != null) {
            effect.setPreFormula(preFormula);
        }

        // Process npr formula with detailed logging
        Formula nprFormula = DeserializerUtils.processFormula("npr", effectNode, p, ctxt, LOGGER);
        if (nprFormula != null) {
            effect.setNprFormula(nprFormula);
        }

        // Register the effect for reference resolution
        ReferenceResolver.getInstance().registerElement(uuid, effect);
        LOGGER.info("Successfully registered effect: " + effect.getId() + " (" + effect.getAtom().getTitleText() + ")");

        return effect;
    }
}