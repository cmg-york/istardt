package ca.yorku.cmg.istardt.xmlparser.xml.deserializers;

import ca.yorku.cmg.istardt.xmlparser.objects.Quality;
import ca.yorku.cmg.istardt.xmlparser.xml.utils.DeserializerUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
public class QualityDeserializer extends BaseDeserializer<Quality> {
    public QualityDeserializer() {
        super(Quality.class);
    }

    @Override
    protected Quality createNewElement() {
        return new Quality();
    }

    @Override
    protected void handleSpecificAttributes(Quality quality, JsonNode node, JsonParser p, DeserializationContext ctxt) throws IOException {
        boolean root = DeserializerUtils.getBooleanAttribute(node, "root", false);
        quality.setRoot(root);
        quality.setRawFormulaNode(node);
    }
}