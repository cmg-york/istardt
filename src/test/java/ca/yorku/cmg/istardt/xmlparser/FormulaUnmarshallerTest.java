package ca.yorku.cmg.istardt.xmlparser;

import ca.yorku.cmg.istardt.xmlparser.objects.*;
import ca.yorku.cmg.istardt.xmlparser.xml.IStarUnmarshaller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FormulaUnmarshallerTest {
    private IStarUnmarshaller unmarshaller;
    private Model model;
    private Actor actor;
    private File xmlFile = getResourceFile("xml/formulaTest.xml");

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
        assertEquals("", header.getAuthor(), "Header author");
        assertEquals("", header.getTitle(), "header title");
        assertEquals("", header.getSource(), "header source");
        assertEquals("2025-05-22 13:24:15", header.getLastUpdated(), "header date");
        assertEquals("", header.getNotes(), "header notes");
    }

    @Test
    public void testUnmarshalOptions() {
        Options options = model.getOptions();
        assertFalse(options.isContinuous());
        assertEquals(100, options.getInfActionPenalty(), 1e-6, "getInfActionPenalty");
    }

    @Test
    public void testUnmarshalActor() {
        assertEquals(1, model.getActors().size(), "Model should have 1 actor");
        Atom atom = actor.getAtom();
        assertEquals("default", actor.getName(), "actor Name");
        assertEquals("", atom.getDescription(), "actor Description");

        assertEquals(1, actor.getGoals().size(), "actor getGoals size");
        assertEquals(2, actor.getTasks().size(), "actor getTasks size");
        assertEquals(6, actor.getEffects().size(), "actor getEffects size");
        assertEquals(9, actor.getQualities().size(), "actor getQualities size");
        assertEquals(2, actor.getConditions().size(), "actor getConditions size");
        assertEquals(6, actor.getPredicates().size(), "actor getPredicates size");
        assertEquals(0, actor.getVariables().size(), "actor getVariables size");

        assertEquals(3, actor.getCrossRunSetElements().size(), "actor getCrossRunSet size");
        assertEquals(6, actor.getExportedSetElements().size(), "actor getExportedSetElements size");
        assertEquals(3, actor.getInitializationSetElements().size(), "actor getInitializationSetElements size");

        // ========= QUALITY ROOT =========
        Quality rootQuality = actor.getQualityRoot();
        assertEquals("overallQuality", rootQuality.getName(), "root quality Name");
        assertEquals("Overall Quality", rootQuality.getAtom().getDescription(), "root quality Description");

        // ========= GOAL ROOT =========
        Goal rootGoal = actor.getGoalRoot();
        assertEquals("orderMaterial", rootGoal.getName(), "root goal Name");
        assertEquals("Order Material", rootGoal.getAtom().getDescription(), "root goal Description");
        assertEquals(1, rootGoal.getRuns(), "root goal episodeLength/runs");
        assertEquals(DecompType.OR, rootGoal.getDecompType(), "root goal DecompType");
        assertNull(rootGoal.getPreFormula(), "root goal pre");
        assertNull(rootGoal.getNprFormula(), "root goal npr");

        assertEquals(2, rootGoal.getChildren().size(), "root goal child size");
        assertEquals("orderFromSupplierA", rootGoal.getChildren().get(0).getName(), "root goal child name 1");
        assertEquals("orderFromSupplierB", rootGoal.getChildren().get(1).getName(), "root goal child name 2");
    }

    @Test
    public void testUnmarshalQualities() {
        List<Quality> qualities = actor.getQualities();

        // ========= QUALITY 1 =========
        Quality quality1 = qualities.get(0);
        Atom qualityAtom1 = quality1.getAtom();
        assertEquals("cost", qualityAtom1.getTitleText(), "quality Name");
        assertEquals("Cost", qualityAtom1.getDescription(), "quality Description");
        assertEquals(false, quality1.isRoot(), "quality is root");
        assertEquals("((((((0.5 * deliveredInTimeA) + (0.5 * deliveredLateA)) + (0.5 * neverDeliveredA)) + (1.0 * deliveredInTimeB)) + (1.0 * deliveredLateB)) + (1.0 * neverDeliveredB))", quality1.getFormula().getFormula(), "quality expression");
        assertTrue(quality1.getFormula() instanceof PlusOperator);
        PlusOperator p1 = (PlusOperator) quality1.getFormula();
        assertTrue(p1.getRight() instanceof MultiplyOperator);
        MultiplyOperator p2 = (MultiplyOperator) p1.getRight();

        assertTrue(p2.getRight() instanceof Atom); // neverDeliveredB
        Element neverDeliveredB = ((Atom) p2.getRight()).getElement();
        assertTrue(neverDeliveredB instanceof Predicate);
        assertEquals("neverDeliveredB", neverDeliveredB.getName(), "neverDeliveredB name");

        assertTrue(p2.getLeft() instanceof NumericConstant);
        NumericConstant numConst = (NumericConstant) p2.getLeft();
        assertEquals(1.0, numConst.getContent(), "numConst content");

        assertTrue(p1.getLeft() instanceof PlusOperator);
        PlusOperator p3 = (PlusOperator) p1.getLeft();

        // ========= QUALITY 2 =========
        Quality quality2 = qualities.get(1);
        assertEquals("(((0.7 * cost) * (0.3 * happyCustomer)) * (0.1 * deliveredLateB))", quality2.getFormula().getFormula(), "quality expression");
        assertTrue(quality2.getFormula() instanceof MultiplyOperator);
        MultiplyOperator p5 = (MultiplyOperator) quality2.getFormula();
        assertTrue(p5.getRight() instanceof MultiplyOperator);
        assertTrue(p5.getLeft() instanceof MultiplyOperator);
        MultiplyOperator p6 = (MultiplyOperator) p5.getRight();
        assertTrue(p6.getRight() instanceof Atom); // deliveredLateB
        assertTrue(p6.getLeft() instanceof NumericConstant);

        // ========= QUALITY 3 =========
        Quality quality3 = qualities.get(2);
        assertEquals("((0.7 - cost) / (happyCustomer - 0.3))", quality3.getFormula().getFormula(), "quality expression");
        assertTrue(quality3.getFormula() instanceof DivideOperator);
        DivideOperator p7 = (DivideOperator) quality3.getFormula();
        assertTrue(p7.getRight() instanceof MinusOperator);
        assertTrue(p7.getLeft() instanceof MinusOperator);
        MinusOperator p8 = (MinusOperator) p7.getRight();
        assertTrue(p8.getLeft() instanceof Atom); // happyCustomer
        assertTrue(p8.getRight() instanceof NumericConstant);

        // ========= QUALITY 4 =========
        Quality quality4 = qualities.get(3);
        assertEquals("-((-1.0 * deliveredInTimeA))", quality4.getFormula().getFormula(), "quality expression");
        assertTrue(quality4.getFormula() instanceof NegateOperator);
        NegateOperator p9 = (NegateOperator) quality4.getFormula();
        assertNull(p9.getRight());
        assertTrue(p9.getLeft() instanceof MultiplyOperator);
        MultiplyOperator p10 = (MultiplyOperator) p9.getLeft();
        assertTrue(p10.getLeft() instanceof NumericConstant);
        NumericConstant numConstNeg = (NumericConstant) p10.getLeft();
        assertEquals(-1.0, numConstNeg.getContent(), "numConstNeg content");

        assertTrue(p10.getRight() instanceof Atom); // deliveredInTimeA

        // ========= QUALITY 5  =========
        Quality quality5 = qualities.get(4);
        assertEquals("PREVIOUS(deliveredInTimeA)", quality5.getFormula().getFormula(), "quality expression");
        assertTrue(quality5.getFormula() instanceof PreviousOperator);
        PreviousOperator p11 = (PreviousOperator) quality5.getFormula();
        assertNull(p11.getRight());
        assertTrue(p11.getLeft() instanceof Atom);
        Element p12 = ((Atom) p11.getLeft()).getElement();
        assertTrue(p12 instanceof Predicate);
        assertEquals("deliveredInTimeA", p12.getName(), "predicateID");

        // ========= QUALITY 6  =========
        Quality quality6 = qualities.get(5);
        assertEquals("PREVIOUS(cost)", quality6.getFormula().getFormula(), "quality expression");
        assertTrue(quality6.getFormula() instanceof PreviousOperator);
        PreviousOperator p13 = (PreviousOperator) quality6.getFormula();
        assertNull(p13.getRight());
        assertTrue(p13.getLeft() instanceof Atom);
        Element p14 = ((Atom) p13.getLeft()).getElement();
        assertTrue(p14 instanceof Quality);
        assertEquals("cost", p14.getName(), "qualID");

        // ========= QUALITY 7  =========
        Quality quality7 = qualities.get(6);
        assertEquals("PREVIOUS(orderFromSupplierB)", quality7.getFormula().getFormula(), "quality expression");
        assertTrue(quality7.getFormula() instanceof PreviousOperator);
        PreviousOperator p15 = (PreviousOperator) quality7.getFormula();
        assertNull(p15.getRight());
        assertTrue(p15.getLeft() instanceof Atom);
        Element p16 = ((Atom) p15.getLeft()).getElement();
        assertTrue(p16 instanceof Task);
        assertEquals("orderFromSupplierB", p16.getName(), "taskID");

        // ========= QUALITY 8  =========
        Quality quality8 = qualities.get(7);
        assertEquals("PREVIOUS(orderMaterial)", quality8.getFormula().getFormula(), "quality expression");
        assertTrue(quality8.getFormula() instanceof PreviousOperator);
        PreviousOperator p17 = (PreviousOperator) quality8.getFormula();
        assertNull(p17.getRight());
        assertTrue(p17.getLeft() instanceof Atom);
        Element p18 = ((Atom) p17.getLeft()).getElement();
        assertTrue(p18 instanceof Goal);
        assertEquals("orderMaterial", p18.getName(), "goalID");

        // ========= QUALITY 8  =========
        Quality quality9 = qualities.get(8);
        assertEquals("(deliveredLateB_cond + deliveredInTimeB_eff)", quality9.getFormula().getFormula(), "quality expression");
        assertTrue(quality9.getFormula() instanceof PlusOperator);
        PlusOperator p19 = (PlusOperator) quality9.getFormula();
        assertTrue(p19.getLeft() instanceof Atom);
        assertTrue(p19.getRight() instanceof Atom);
        Element deliveredLateB_cond = ((Atom) p19.getLeft()).getElement();
        Element deliveredInTimeB_eff = ((Atom) p19.getRight()).getElement();

        assertTrue(deliveredLateB_cond instanceof Condition);
        assertEquals("deliveredLateB_cond", deliveredLateB_cond.getName(), "conditionID");

        assertTrue(deliveredInTimeB_eff instanceof Effect);
        assertEquals("deliveredInTimeB_eff", deliveredInTimeB_eff.getName(), "effectID");
    }

    @Test
    public void testUnmarshalConditions() {
        // ========= CONDITION 1  =========
        Condition condition = actor.getConditions().get(0);

        assertEquals("((PREVIOUS(neverDeliveredB) AND NOT(deliveredInTimeB)) OR deliveredLateB)", condition.getFormula().getFormula(), "condition expression");
        assertTrue(condition.getFormula() instanceof OROperator);
        OROperator p1 = (OROperator) condition.getFormula();
        assertTrue(p1.getLeft() instanceof ANDOperator);
        assertTrue(p1.getRight() instanceof Atom);

        Atom deliveredLateB = (Atom) p1.getRight();
        assertTrue(deliveredLateB.getElement() instanceof Predicate);

        // ========= CONDITION 2  =========
        Condition condition2 = actor.getConditions().get(1);

        assertEquals("(deliveredInTimeB_eff AND deliveredLateB_cond)", condition2.getFormula().getFormula(), "condition expression");
        assertTrue(condition2.getFormula() instanceof ANDOperator);
        ANDOperator p2 = (ANDOperator) condition2.getFormula();
        assertTrue(p2.getLeft() instanceof Atom);
        assertTrue(p2.getRight() instanceof Atom);

        Atom deliveredInTimeB_eff = (Atom) p2.getLeft();
        Atom deliveredLateB_cond = (Atom) p2.getRight();

        assertTrue(deliveredInTimeB_eff.getElement() instanceof Effect);
        assertTrue(deliveredLateB_cond.getElement() instanceof Condition);
    }

        /**
         * Helper method to get a file from the resources directory.
         */
    private File getResourceFile(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        return new File(classLoader.getResource(fileName).getFile());
    }

}