package ca.yorku.cmg.istardt.xmlparser.xml;

import ca.yorku.cmg.istardt.xmlparser.objects.Model;
import ca.yorku.cmg.istardt.xmlparser.xml.deserializers.IStarDTXModule;
import ca.yorku.cmg.istardt.xmlparser.xml.processing.ReferenceProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Main entry point for unmarshalling iStar-DT-X XML to the domain model using Jackson XML.
 */
public class IStarUnmarshaller {
    private final XmlMapper xmlMapper;
    private final ReferenceProcessor referenceProcessor;

    /**
     * Constructor with default configuration.
     */
    public IStarUnmarshaller() {
        this.xmlMapper = createXmlMapper();
        this.referenceProcessor = new ReferenceProcessor(this.xmlMapper);
    }

    /**
     * Creates and configures the XmlMapper with necessary settings for handling iStar-DT-X XML.
     *
     * @return Configured XmlMapper instance
     */
    private XmlMapper createXmlMapper() {
        XmlMapper mapper = new XmlMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Register custom module with deserializers for iStar-DT-X specific types
        mapper.registerModule(new IStarDTXModule());
        return mapper;
    }

    /**
     * Unmarshals an XML file to the domain model.
     *
     * @param xmlFile The XML file to unmarshall
     * @return The populated domain model
     * @throws IOException If there's an error during unmarshalling
     */
    public Model unmarshalToModel(File xmlFile) throws IOException {
        // Clear any existing references before processing a new file
        ReferenceResolver.getInstance().clear();

        // Parse XML to domain model
        Model model = xmlMapper.readValue(xmlFile, Model.class);

        // Process references to link objects
        referenceProcessor.processReferences(model);
        return model;
    }

    /**
     * Unmarshals an XML input stream to the domain model.
     *
     * @param xmlStream The XML input stream to unmarshall
     * @return The populated domain model
     * @throws IOException If there's an error during unmarshalling
     */
    public Model unmarshalToModel(InputStream xmlStream) throws IOException {
        // Clear any existing references before processing a new file
        ReferenceResolver.getInstance().clear();

        // Parse XML to domain model
        Model model = xmlMapper.readValue(xmlStream, Model.class);

        // Process references to link objects
        referenceProcessor.processReferences(model);
        return model;
    }
}