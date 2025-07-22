package ca.yorku.cmg.istardt.xmlparser.xml.deserializers;

import ca.yorku.cmg.istardt.xmlparser.objects.Condition;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;

public class ConditionDeserializer extends BaseDeserializer<Condition> {
    public ConditionDeserializer() {
        super(Condition.class);
    }

    @Override
    protected Condition createNewElement() {
        return new Condition();
    }

    @Override
    protected void handleSpecificAttributes(Condition condition, JsonNode node, JsonParser p, DeserializationContext ctxt) throws IOException {
        condition.setRawFormulaNode(node);
    }
}