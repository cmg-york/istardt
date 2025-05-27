package ca.yorku.cmg.istardt.xmlparser.xml.deserializers;

import ca.yorku.cmg.istardt.xmlparser.objects.Atom;
import ca.yorku.cmg.istardt.xmlparser.objects.Condition;
import ca.yorku.cmg.istardt.xmlparser.xml.utils.CustomLogger;
import ca.yorku.cmg.istardt.xmlparser.xml.utils.DeserializerUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.UUID;

public class ConditionDeserializer extends BaseDeserializer<Condition> {
    public ConditionDeserializer() {
        super(Condition.class);
    }

    @Override
    protected Condition createNewElement() {
        return new Condition();
    }

    @Override
    protected Condition extractCommonAttributes(Condition element, JsonNode node) {
        String id = DeserializerUtils.getStringAttribute(node, "id", UUID.randomUUID().toString());
        element.setId(id);

        String name = DeserializerUtils.getStringAttribute(node, "name", "");
        String description = DeserializerUtils.getStringAttribute(node, "description", "");

        // Create atom and add bidirectional relationship
        Atom atom = createAtom(name, description);
        element.setRepresentation(atom);
        atom.setElement(element);

        // Do not add condition to elementsByName map
        return element;
    }

    @Override
    protected void handleSpecificAttributes(Condition condition, JsonNode node, JsonParser p, DeserializationContext ctxt) throws IOException {
        condition.setRawFormulaNode(node);
    }
}