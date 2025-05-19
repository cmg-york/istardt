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
        // Store formula nodes for future processing
        if (node.has("pre")) {
            task.setRawPreFormulaNode(node.get("pre"));
        }
        if (node.has("npr")) {
            task.setRawNprFormulaNode(node.get("npr"));
        }

        // Process effect group
        try {
            if (node.has("effectGroup") && node.get("effectGroup").has("effect")) {
                JsonNode effectGroupNode = node.get("effectGroup");
                JsonNode effectNodes = effectGroupNode.get("effect");
                LOGGER.info("Processing effect group for task " + task.getId());
                List<Effect> effects = DeserializerUtils.deserializeList(effectNodes, p, ctxt, Effect.class);
                task.setEffects(effects); // bidirectional relationship
                LOGGER.info("Successfully processed " + effects.size() + " effects for task " + task.getId());
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error processing effects for task " + task.getId() + ": " + e.getMessage(), e);
        }
    }
}