package ca.yorku.cmg.istardt.xmlparser.xml.deserializers;

import ca.yorku.cmg.istardt.xmlparser.xml.utils.CustomLogger;
import ca.yorku.cmg.istardt.xmlparser.xml.utils.DeserializerUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import ca.yorku.cmg.istardt.xmlparser.objects.Effect;
import ca.yorku.cmg.istardt.xmlparser.objects.Task;

import java.io.IOException;
import java.util.List;

public class TaskDeserializer extends BaseDeserializer<Task> {
    private static final CustomLogger LOGGER = CustomLogger.getInstance();

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
                LOGGER.info(getClass(),"Processing effect group for task " + task.getName());
                List<Effect> effects = DeserializerUtils.deserializeList(effectNodes, p, ctxt, Effect.class);
                task.setEffects(effects); // bidirectional relationship
                LOGGER.info(getClass(), "Successfully processed " + effects.size() + " effects for task " + task.getName());
            }
        } catch (IOException e) {
            LOGGER.error(getClass(), "Error processing effects for task " + task.getName() + ": " + e.getMessage(), e);
        }
    }
}