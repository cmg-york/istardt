package ca.yorku.cmg.istardt.xmlparser.xml.deserializers;

import ca.yorku.cmg.istardt.xmlparser.objects.Atom;
import ca.yorku.cmg.istardt.xmlparser.objects.Condition;
import ca.yorku.cmg.istardt.xmlparser.objects.Formula;
import ca.yorku.cmg.istardt.xmlparser.objects.Predicate;
import ca.yorku.cmg.istardt.xmlparser.xml.utils.DeserializerUtils;

import java.io.IOException;
import java.util.UUID;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

public class PredicateDeserializer extends BaseDeserializer<Predicate> {
    private static final Logger LOGGER = Logger.getLogger(PredicateDeserializer.class.getName());

    public PredicateDeserializer() {
        super(Predicate.class);
    }

    @Override
    protected Predicate createNewElement() {
        return new Predicate();
    }

    @Override
    protected void handleSpecificAttributes(Predicate element, JsonNode node, JsonParser p, DeserializationContext ctxt) throws IOException {
    }
    @Override
    protected Predicate extractCommonAttributes(Predicate element, JsonNode node) {
        String id = DeserializerUtils.getStringAttribute(node, "id", UUID.randomUUID().toString());
        element.setId(id);

        String name = null;
        if (node.has("")) {
            name = node.get("").asText().trim();
        }
        String description = DeserializerUtils.getStringAttribute(node, "description", null);

        // Create atom and add bidirectional relationship
        Atom atom = createAtom(name, description);
        element.setRepresentation(atom);
        atom.setElement(element);

        // Register the element
        registerElement(element);

        return element;
    }

}