package ca.yorku.cmg.istardt.xmlparser.xml.deserializers;

import ca.yorku.cmg.istardt.xmlparser.objects.Atom;
import ca.yorku.cmg.istardt.xmlparser.objects.Variable;
import ca.yorku.cmg.istardt.xmlparser.xml.utils.DeserializerUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.UUID;

public class VariableDeserializer extends BaseDeserializer<Variable> {
    public VariableDeserializer() {
        super(Variable.class);
    }

    @Override
    protected Variable createNewElement() {
        return new Variable();
    }

    @Override
    protected void handleSpecificAttributes(Variable element, JsonNode node, JsonParser p, DeserializationContext ctxt) throws IOException {
    }
    @Override
    protected Variable extractCommonAttributes(Variable element, JsonNode node) {
        String id = DeserializerUtils.getStringAttribute(node, "id", UUID.randomUUID().toString());
        element.setId(id);

        String name = null;
        if (node.has("")) {
            name = node.get("").asText().trim();
        }
        String description = DeserializerUtils.getStringAttribute(node, "description", "");

        // Create atom and add bidirectional relationship
        Atom atom = createAtom(name, description);
        element.setRepresentation(atom);
        atom.setElement(element);

        // Register the element
        registerElement(element);

        return element;
    }
}