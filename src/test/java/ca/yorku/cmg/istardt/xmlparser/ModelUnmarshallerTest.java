package ca.yorku.cmg.istardt.xmlparser;

import ca.yorku.cmg.istardt.xmlparser.objects.*;
import ca.yorku.cmg.istardt.xmlparser.xml.IStarUnmarshaller;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

        Quality rootQuality = actor.getQualityRoot();
        assertEquals("totalValue", rootQuality.getName(), "root quality Name");
        assertEquals("Overall Value", rootQuality.getAtom().getDescription(), "root quality Description");

        Goal rootGoal = actor.getGoalRoot();
        assertEquals("productManufactured", rootGoal.getName(), "root goal Name");
        assertEquals("Product Manufactured", rootGoal.getAtom().getDescription(), "root goal Description");
        assertEquals(4, rootGoal.getRuns(), "root goal episodeLength/runs");
        assertEquals(DecompType.AND, rootGoal.getDecompType(), "root goal DecompType");
        assertEquals("deliveredInTimeDom", rootGoal.getPreFormula().getFormula(), "root goal pre");

        assertEquals(2, rootGoal.getChildren().size(), "root goal child size");
        assertEquals("materialOrdered", rootGoal.getChildren().get(0).getName(), "root goal child name 1");
        assertEquals("manufacturingCompleted", rootGoal.getChildren().get(1).getName(), "root goal child name 2");
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

    @Test
    public void testUnmarshalQualities() {
        List<Quality> qualities = actor.getQualities();
        Quality quality1 = qualities.get(0);
        Atom qualityAtom1 = quality1.getAtom();
        assertEquals("reputation", qualityAtom1.getTitleText(), "quality Name");
        assertEquals("Reputation of the Manufacturer", qualityAtom1.getDescription(), "quality Description");
        assertEquals(false, quality1.isRoot(), "quality is root");
        // TODO should const be Atom objects?
        assertEquals("-(5)", quality1.getFormula().getFormula(), "quality expression");

        Quality quality2 = qualities.get(1);
        Atom qualityAtom2 = quality2.getAtom();
        assertEquals("financialGain", qualityAtom2.getTitleText(), "quality Name");
        assertEquals("Financial Gain", qualityAtom2.getDescription(), "quality Description");
        assertEquals(false, quality2.isRoot(), "quality is root");
        assertEquals("-(((2 + 5) + 10))", quality2.getFormula().getFormula(), "quality expression");

        Quality quality3 = qualities.get(2);
        Atom qualityAtom3 = quality3.getAtom();
        assertEquals("totalValue", qualityAtom3.getTitleText(), "quality Name");
        assertEquals("Overall Value", qualityAtom3.getDescription(), "quality Description");
        assertEquals(true, quality3.isRoot(), "quality is root");
        assertEquals("(reputation + financialGain)", quality3.getFormula().getFormula(), "quality expression");
        Formula formula = quality3.getFormula();
        // Check if it is a PlusOperator
        assertTrue(formula instanceof PlusOperator,
                "Expected the formula to be a PlusOperator but was " + formula.getClass().getName());
        PlusOperator op = (PlusOperator) formula;

        // Check operands
        assertTrue(op.getLeft() instanceof Atom,
                "Expected the left operand to be an Atom but was " + op.getLeft().getClass().getName());
        assertTrue(op.getRight() instanceof Atom,
                "Expected the right operand to be an Atom but was " + op.getRight().getClass().getName());
    }

    @Test
    public void testUnmarshalConditions() {
        List<Condition> conditions = actor.getConditions();
        Condition condition1 = conditions.get(0);
        Atom conditionAtom1 = condition1.getAtom();
        assertEquals("materialAvailable", conditionAtom1.getTitleText(), "Condition Name");
        assertEquals("Material availability in stock", conditionAtom1.getDescription(), "Condition Description");
        assertEquals("PREVIOUS(deliveredLateDom)", condition1.getFormula().getFormula(), "Condition expression");

        Formula formula = condition1.getFormula();
        // Check if it is a PreviousOperator
        assertTrue(formula instanceof PreviousOperator,
                "Expected the formula to be a PreviousOperator but was " + formula.getClass().getName());
        PreviousOperator op = (PreviousOperator) formula;

        // Check the left operand
        assertTrue(op.getLeft() instanceof Atom,
                "Expected the left operand to be an Atom but was " + op.getLeft().getClass().getName());
        // Atom leftAtom = (Atom) op.getLeft();
        assertNull(op.getRight());

        Condition condition2 = conditions.get(1);
        Atom conditionAtom2 = condition2.getAtom();
        assertEquals("hasManufacturingCapacity", conditionAtom2.getTitleText(), "Condition Name");
        assertEquals("Manufacturer has capacity to build in-house", conditionAtom2.getDescription(), "Condition Description");
        assertEquals("false", condition2.getFormula().getFormula(), "Condition expression");
    }

    @Test
    public void testUnmarshalGoals() {
        List<Goal> goals = actor.getGoals();
        Goal goal1 = goals.get(0);
        Atom goalAtom1 = goal1.getAtom();

        assertEquals("productManufactured", goalAtom1.getTitleText(), "goal Name");
        assertEquals("Product Manufactured", goalAtom1.getDescription(), "goal Description");
        assertEquals(4, goal1.getRuns(), "goal episodeLength/runs");
        assertEquals(DecompType.AND, goal1.getDecompType(), "goal DecompType");
        assertEquals("deliveredInTimeDom", goal1.getPreFormula().getFormula(), "goal pre");
        Formula formula1 = goal1.getPreFormula();
        assertTrue(formula1 instanceof Atom,
                "Expected the formula to be a Atom but was " + formula1.getClass().getName());
        assertEquals(2, goal1.getChildren().size(), "goal child size");
        assertEquals("materialOrdered", goal1.getChildren().get(0).getName(), "goal child name 1");
        assertEquals("manufacturingCompleted", goal1.getChildren().get(1).getName(), "goal child name 2");
        assertNull(goal1.getNprFormula());

        Goal goal2 = goals.get(1);
        Atom goalAtom2 = goal2.getAtom();
        assertEquals("materialOrdered", goalAtom2.getTitleText(), "root goal Name");
        assertEquals("Material Ordered", goalAtom2.getDescription(), "root goal Description");
        assertEquals(1, goal2.getRuns(), "root goal episodeLength/runs");
        assertEquals(DecompType.OR, goal2.getDecompType(), "root goal DecompType");
        assertEquals(2, goal2.getChildren().size(), "goal child size");
        assertEquals("sourceDomestically", goal2.getChildren().get(0).getName(), "goal child name 1");
        assertEquals("sourceFromAbroad", goal2.getChildren().get(1).getName(), "goal child name 2");
        assertNull(goal2.getNprFormula());
        assertNull(goal2.getPreFormula());

        Goal goal3 = goals.get(2);
        Atom goalAtom3 = goal3.getAtom();
        assertEquals("manufacturingCompleted", goalAtom3.getTitleText(), "goal Name");
        assertEquals("Manufacturing Completed", goalAtom3.getDescription(), "goal Description");
        assertEquals(1, goal3.getRuns(), "goal episodeLength/runs");
        assertEquals(DecompType.OR, goal3.getDecompType(), "goal DecompType");
        assertEquals("materialOrdered", goal3.getPreFormula().getFormula(), "goal pre");
        Formula formula3 = goal3.getPreFormula();
        assertTrue(formula3 instanceof Atom,
                "Expected the formula to be a Atom but was " + formula3.getClass().getName());
        assertEquals(2, goal3.getChildren().size(), "goal child size");
        assertEquals("buildInHouse", goal3.getChildren().get(0).getName(), "goal child name 1");
        assertEquals("assignToSpecialists", goal3.getChildren().get(1).getName(), "goal child name 2");
        assertNull(goal3.getNprFormula());
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