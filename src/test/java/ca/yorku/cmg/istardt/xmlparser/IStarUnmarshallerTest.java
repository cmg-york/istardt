//package ca.yorku.cmg.istardt.xmlparser;
//
//import ca.yorku.cmg.istardt.xmlparser.objects.Actor;
//import ca.yorku.cmg.istardt.xmlparser.objects.Goal;
//import ca.yorku.cmg.istardt.xmlparser.objects.Model;
//import ca.yorku.cmg.istardt.xmlparser.objects.Task;
//import ca.yorku.cmg.istardt.xmlparser.xml.IStarUnmarshaller;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.BeforeEach;
//
//import java.io.File;
//import java.io.IOException;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class IStarUnmarshallerTest {
//
//    private IStarUnmarshaller unmarshaller;
//
//    @BeforeEach
//    public void setUp() {
//        unmarshaller = new IStarUnmarshaller();
//    }
//
//    @Test
//    public void testUnmarshalValidXmlFile() throws IOException {
//        // Get the XML file from resources
//        File xmlFile = getResourceFile("xml/figure1a_fixed.xml");
//
//        // Ensure the file exists
//        assertTrue(xmlFile.exists(), "Test XML file should exist");
//
//        // Unmarshal the file
//        Model model = unmarshaller.unmarshalToModel(xmlFile);
//
//        // Verify the model structure
//        assertNotNull(model, "Model should not be null");
//        assertEquals(1, model.getActors().size(), "Model should have one actor");
//
//        Actor actor = model.getActors().get(0);
//        assertEquals("Manufacturer", actor.getName(), "Actor name should be 'Manufacturer'");
//
//
//        // Verify goals
//        assertFalse(actor.getGoals().isEmpty(), "Actor should have goals");
//        Goal rootGoal = null;
//        for (Goal goal : actor.getGoals()) {
//            if (goal.isRoot()) {
//                rootGoal = goal;
//                break;
//            }
//        }
//        assertNotNull(rootGoal, "Actor should have a root goal");
//        assertEquals("ProductManufactured", rootGoal.getName(), "Root goal should be 'ProductManufactured'");
//
//        // Verify tasks
//        assertFalse(actor.getTasks().isEmpty(), "Actor should have tasks");
//        boolean hasBuildInHouseTask = false;
//        for (Task task : actor.getTasks()) {
//            if ("BuildInHouse".equals(task.getAtom().getTitleText())) {
//                hasBuildInHouseTask = true;
//                // Verify task details
//                assertFalse(task.getEffects().isEmpty(), "BuildInHouse task should have effects");
//                assertEquals(2, task.getEffects().size(), "BuildInHouse task should have 2 effects");
//                break;
//            }
//        }
//        assertTrue(hasBuildInHouseTask, "Actor should have a 'BuildInHouse' task");
//    }
//
//    @Test
//    public void testUnmarshalWithMissingFile() {
//        File nonExistentFile = new File("non_existent_file.xml");
//
//        // Verify that attempting to unmarshal a non-existent file throws IOException
//        assertThrows(IOException.class, () -> {
//            unmarshaller.unmarshalToModel(nonExistentFile);
//        });
//    }
//
//    /**
//     * Helper method to get a file from the resources directory.
//     */
//    private File getResourceFile(String fileName) {
//        ClassLoader classLoader = getClass().getClassLoader();
//        return new File(classLoader.getResource(fileName).getFile());
//    }
//}