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

public class EffectDeserializer extends BaseDeserializer<Effect> {
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
        Map<String, BiConsumer<Effect, List<String>>> turnsSetter = new HashMap<>();
        turnsSetter.put("turnsTrue", Effect::setTurnsTrue);
        turnsSetter.put("turnsFalse", Effect::setTurnsFalse);
        // Apply list-based properties
        for (Map.Entry<String, BiConsumer<Effect, List<String>>> entry : turnsSetter.entrySet()) {
            List<String> values = DeserializerUtils.getStringList(node, entry.getKey());
            entry.getValue().accept(effect, values);
        }

        Map<String, Float> variableMap = new HashMap<>();
        JsonNode setsNode = node.get("set");
        if (setsNode != null) {
            if (setsNode.isArray()) {
                for (JsonNode setElem : setsNode) {
                    String varId = setElem.path("variableID").asText();
                    float numConst = DeserializerUtils.getFloatAttribute(setElem, "numConst", 0);
                    variableMap.put(varId, numConst);
                }
            } else {
                String varId = setsNode.path("variableID").asText();
                float numConst = DeserializerUtils.getFloatAttribute(setsNode, "numConst", 0);
                variableMap.put(varId, numConst);
            }
        }
        effect.setVariableNameSet(variableMap);
    }
}