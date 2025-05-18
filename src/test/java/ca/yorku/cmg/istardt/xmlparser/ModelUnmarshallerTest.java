package ca.yorku.cmg.istardt.xmlparser;

import ca.yorku.cmg.istardt.xmlparser.objects.*;
import ca.yorku.cmg.istardt.xmlparser.xml.IStarUnmarshaller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ModelUnmarshallerTest {
    private IStarUnmarshaller unmarshaller;
    private Model model;
    private Actor actor;
    private File xmlFile = getResourceFile("xml/figure1a_updated.xml");

    @BeforeEach
    public void setUp() throws IOException {
        unmarshaller = new IStarUnmarshaller();
        model = unmarshaller.unmarshalToModel(xmlFile);
        actor = model.getActors().get(0);
    }

    @Test
    public void testUnmarshalValidXmlFile() {
        assertTrue(xmlFile.exists(), "Test XML file should exist");
        assertNotNull(model, "Model should not be null");
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

    @Test
    public void testUnmarshalActor() {
        assertEquals(1, model.getActors().size(), "Model should have 1 actor");
        Atom atom = actor.getAtom();
        assertEquals("Manufacturer", actor.getName(), "actor Name");
        assertEquals("A Manufacturer actor", atom.getDescription(), "actor Description");
    }

    @Test
    public void testUnmarshalPredicates() {
        List<Predicate> predicates = actor.getPredicates();
        Atom predicateAtom1 = predicates.get(0).getAtom();
        assertEquals("deliveredInTimeDom", predicateAtom1.getTitleText(), "predicate Name");
        assertEquals("Materials delivered on time (domestic)", predicateAtom1.getDescription(), "predicate Description");

        Atom predicateAtom2 = predicates.get(1).getAtom();
        assertEquals("deliveredLateDom", predicateAtom2.getTitleText(), "predicate Name");
        assertEquals("Materials delivered late (domestic)", predicateAtom2.getDescription(), "predicate Description");

        Atom predicateAtom3 = predicates.get(2).getAtom();
        assertEquals("neverDeliveredDom", predicateAtom3.getTitleText(), "predicate Name");
        assertEquals("Materials never delivered (domestic)", predicateAtom3.getDescription(), "predicate Description");
    }

    @Test
    public void testUnmarshalVariables() {
        List<Variable> variables = actor.getVariables();
        Atom variableAtom1 = variables.get(0).getAtom();
        assertEquals("test1", variableAtom1.getTitleText(), "variable Name");
        assertEquals("description1", variableAtom1.getDescription(), "variable Description");

        Atom variableAtom2 = variables.get(1).getAtom();
        assertEquals("test2", variableAtom2.getTitleText(), "variable Name");
        assertEquals("description2", variableAtom2.getDescription(), "variable Description");
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