package ca.yorku.cmg.istardt.xmlparser.xml.deserializers;

import ca.yorku.cmg.istardt.xmlparser.objects.Formula;
import ca.yorku.cmg.istardt.xmlparser.objects.IndirectEffect;
import ca.yorku.cmg.istardt.xmlparser.xml.utils.DeserializerUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Deserializer for IndirectEffect objects.
 */
public class IndirectEffectDeserializer extends BaseDeserializer<IndirectEffect> {
    private static final Logger LOGGER = Logger.getLogger(IndirectEffectDeserializer.class.getName());

    public IndirectEffectDeserializer() {
        super(IndirectEffect.class);
    }

    @Override
    protected IndirectEffect createNewElement() {
        return new IndirectEffect();
    }

    @Override
    protected void handleSpecificAttributes(IndirectEffect indirectEffect, JsonNode node, JsonParser p, DeserializationContext ctxt) throws IOException {
        // Get specific attributes
        boolean exported = DeserializerUtils.getBooleanAttribute(node, "exported", false);
        indirectEffect.setExported(exported);

        // Process formula
        try {
            if (node.has("formula")) {
                Formula formula = ctxt.readValue(node.get("formula").traverse(p.getCodec()), Formula.class);
                indirectEffect.setValueFormula(formula);
            }
        } catch (IOException e) {
            DeserializerUtils.handleDeserializationError(LOGGER,
                    "Error processing formula for indirect effect " + indirectEffect.getAtom().getTitleText(), e);
        }
    }
}