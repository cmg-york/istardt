package ca.yorku.cmg.istardt.xmlparser;

import ca.yorku.cmg.istardt.xmlparser.objects.*;
import ca.yorku.cmg.istardt.xmlparser.xml.IStarUnmarshaller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class ModelUnmarshallerTest {
    private IStarUnmarshaller unmarshaller;
    private Model model;
    File xmlFile = getResourceFile("xml/figure1a_updated.xml");

    @BeforeEach
    public void setUp() throws IOException {
        unmarshaller = new IStarUnmarshaller();
        model = unmarshaller.unmarshalToModel(xmlFile);
    }

    @Test
    public void testUnmarshalValidXmlFile() {
        assertTrue(xmlFile.exists(), "Test XML file should exist");
        assertNotNull(model, "Model should not be null");
        assertEquals(0, model.getActors().size(), "Model should have no actor");
    }

    @Test
    public void testUnmarshalHeader() {
        Header header = model.getHeader();
        assertEquals("first last", header.getAuthor(), "Header author");
        assertEquals("title", header.getTitle(), "header title");
        assertEquals("path to xml file", header.getSource(), "header source");
        assertEquals("String date here", header.getLastUpdated(), "header date");
        assertEquals("notes here", header.getNotes(), "header notes");

//        Actor actor = model.getActors().get(0);
//        assertEquals("Manufacturer", actor.getName(), "Actor name should be 'Manufacturer'");
    }

    @Test
    public void testUnmarshalOptions() {
        Options options = model.getOptions();
        assertTrue(options.isContinuous());
        assertEquals(1.0, options.getInfActionPenalty(), "getInfActionPenalty");

    }
//    @Test
//    public void testUnmarshalWithMissingFile() {
//        File nonExistentFile = new File("non_existent_file.xml");
//
//        // Verify that attempting to unmarshal a non-existent file throws IOException
//        assertThrows(IOException.class, () -> {
//            unmarshaller.unmarshalToModel(nonExistentFile);
//        });
//    }

    /**
     * Helper method to get a file from the resources directory.
     */
    private File getResourceFile(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        return new File(classLoader.getResource(fileName).getFile());
    }
}