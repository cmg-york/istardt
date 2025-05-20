package ca.yorku.cmg.istardt.xmlparser.xml.deserializers;

import ca.yorku.cmg.istardt.xmlparser.objects.Effect;
import ca.yorku.cmg.istardt.xmlparser.xml.utils.DeserializerUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.logging.Logger;

public class EffectDeserializer extends BaseDeserializer<Effect> {
    private static final Logger LOGGER = Logger.getLogger(EffectDeserializer.class.getName());
    public EffectDeserializer() {
        super(Effect.class);
    }

    @Override
    protected Effect createNewElement() {
        return new Effect();
    }

    @Override
    protected void handleSpecificAttributes(Effect effect, JsonNode node, JsonParser p, DeserializationContext ctxt) throws IOException {
        boolean satisfying = DeserializerUtils.getBooleanAttribute(node, "satisfying", true);
        float probability = DeserializerUtils.getFloatAttribute(node, "probability", 1.0f);
        effect.setSatisfying(satisfying);
        effect.setProbability(probability);

        if (node.has("pre")) {
            effect.setRawPreFormulaNode(node.get("pre"));
        }
        if (node.has("npr")) {
            effect.setRawNprFormulaNode(node.get("npr"));
        }

        // Process string list properties for turnsTrue and turnsFalse
        Map<String, BiConsumer<Effect, List<String>>> listSetters = new HashMap<>();
        listSetters.put("turnsTrue", Effect::setTurnsTrue);
        listSetters.put("turnsFalse", Effect::setTurnsFalse);

        // Apply list-based properties
        for (Map.Entry<String, BiConsumer<Effect, List<String>>> entry : listSetters.entrySet()) {
            List<String> values = DeserializerUtils.getStringList(node, entry.getKey());
            entry.getValue().accept(effect, values);
        }
    }
}