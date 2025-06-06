package ca.yorku.cmg.istardt.xmlparser.xml.deserializers;

import ca.yorku.cmg.istardt.xmlparser.objects.Actor;
import ca.yorku.cmg.istardt.xmlparser.objects.Header;
import ca.yorku.cmg.istardt.xmlparser.objects.Model;
import ca.yorku.cmg.istardt.xmlparser.objects.Options;
import ca.yorku.cmg.istardt.xmlparser.xml.utils.CustomLogger;
import ca.yorku.cmg.istardt.xmlparser.xml.utils.DeserializerUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ModelDeserializer extends StdDeserializer<Model> {
    private static final CustomLogger LOGGER = CustomLogger.getInstance();

    public ModelDeserializer() {
        super(Model.class);
    }

    @Override
    public Model deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        Model model = new Model();

        try {
            if (node.has("header")) {
                Header header = deserializeHeader(node.get("header"), p, ctxt);
                model.setHeader(header);
            } else {
                model.setHeader(new Header());
                LOGGER.info(getClass(),"No header found, using default");
            }

            if (node.has("options")) {
                Options options = deserializeOptions(node.get("options"), p, ctxt);
                model.setOptions(options);
            } else {
                model.setOptions(new Options());
                LOGGER.info(getClass(),"No options found, using default");
            }

            List<Actor> actors = new ArrayList<>();
            if (node.has("actors")) {
                JsonNode actorsNode = node.get("actors");
                if (actorsNode.has("actor")) {
                    JsonNode actorNodes = actorsNode.get("actor");
                    actors = DeserializerUtils.deserializeList(actorNodes, p, ctxt, Actor.class);
                    LOGGER.info(getClass(),"Deserialized " + actors.size() + " actors");
                } else {
                    LOGGER.warning(getClass(),"No actors found within actors tag");
                }
            } else {
                LOGGER.warning(getClass(),"No actors tag found");
            }
            model.setActors(actors);
        } catch (IOException e) {
            LOGGER.error(getClass(),  "Error deserializing model", e);
        }
        return model;
    }
    private Header deserializeHeader(JsonNode headerNode, JsonParser p, DeserializationContext ctxt) throws IOException {
        Header header = new Header();
        header.setTitle(DeserializerUtils.getStringAttribute(headerNode, "title", ""));
        header.setAuthor(DeserializerUtils.getStringAttribute(headerNode, "author", ""));
        header.setSource(DeserializerUtils.getStringAttribute(headerNode, "source", ""));
        header.setLastUpdated(DeserializerUtils.getStringAttribute(headerNode, "lastUpdated", ""));

        // Get mixed content text
        if (headerNode.has("")) {
            header.setNotes(headerNode.get("").asText().trim());
        } else {
            header.setNotes("");
        }

        LOGGER.info(getClass(),"Deserialized Header");
        return header;
    }

    private Options deserializeOptions(JsonNode optionsNode, JsonParser p, DeserializationContext ctxt) throws IOException {
        Options options = new Options();
        options.setContinuous(DeserializerUtils.getBooleanAttribute(optionsNode, "continuous", false));
        options.setInfActionPenalty(DeserializerUtils.getFloatAttribute(optionsNode, "infeasibleActionPenalty", 0.0f));

        LOGGER.info(getClass(),"Deserialized Options");
        return options;
    }
}