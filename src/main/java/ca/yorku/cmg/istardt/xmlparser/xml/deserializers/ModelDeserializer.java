package ca.yorku.cmg.istardt.xmlparser.xml.deserializers;

import ca.yorku.cmg.istardt.xmlparser.objects.Actor;
import ca.yorku.cmg.istardt.xmlparser.objects.Model;
import ca.yorku.cmg.istardt.xmlparser.xml.utils.DeserializerUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Deserializer for Model objects.
 * Handles the conversion of XML root element to Model.
 */
public class ModelDeserializer extends StdDeserializer<Model> {
    private static final Logger LOGGER = Logger.getLogger(ModelDeserializer.class.getName());

    public ModelDeserializer() {
        super(Model.class);
    }

    @Override
    public Model deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        Model model = new Model();
        List<Actor> actors = new ArrayList<>();

        try {
            // If root node is an actor, deserialize it directly
            if (node.has("name")) {
                Actor actor = ctxt.readValue(node.traverse(p.getCodec()), Actor.class);
                actors.add(actor);
                LOGGER.info("Deserialized single actor model");
            }
            // Multiple actors (future implementation)
            else if (node.has("actor")) {
                JsonNode actorNodes = node.get("actor");
                actors = DeserializerUtils.deserializeList(actorNodes, p, ctxt, Actor.class);
                LOGGER.info("Deserialized multi-actor model with " + actors.size() + " actors");
            }
        } catch (IOException e) {
            DeserializerUtils.handleDeserializationError(LOGGER, "Error deserializing actors in model", e);
        }

        model.setActors(actors);
        return model;
    }
}