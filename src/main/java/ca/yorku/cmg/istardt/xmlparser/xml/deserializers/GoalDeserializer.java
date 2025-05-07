package ca.yorku.cmg.istardt.xmlparser.xml.deserializers;

import ca.yorku.cmg.istardt.xmlparser.xml.utils.DeserializerUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import ca.yorku.cmg.istardt.xmlparser.objects.DecompType;
import ca.yorku.cmg.istardt.xmlparser.objects.Formula;
import ca.yorku.cmg.istardt.xmlparser.objects.Goal;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Enhanced deserializer for Goal objects with improved formula handling
 */
public class GoalDeserializer extends BaseDeserializer<Goal> {
    private static final Logger LOGGER = Logger.getLogger(GoalDeserializer.class.getName());

    public GoalDeserializer() {
        super(Goal.class);
    }

    @Override
    protected Goal createNewElement() {
        return new Goal();
    }

    @Override
    protected void handleSpecificAttributes(Goal goal, JsonNode node, JsonParser p, DeserializationContext ctxt) throws IOException {
        // Set specific attributes
        boolean root = DeserializerUtils.getBooleanAttribute(node, "root", false);
        goal.setRoot(root);

        // Set episode length (runs)
        int episodeLength = DeserializerUtils.getIntAttribute(node, "episodeLength", 1);
        goal.setRuns(episodeLength);

        // Process pre formula with detailed logging
        DeserializerUtils.logInfo(LOGGER, "Processing goal: " + goal.getId());
        Formula preFormula = DeserializerUtils.processFormula("pre", node, p, ctxt, LOGGER);
        if (preFormula != null) {
            goal.setPreFormula(preFormula);
            DeserializerUtils.logInfo(LOGGER, "Set pre formula for goal: " + goal.getId());
        }

        // Process npr formula with detailed logging
        Formula nprFormula = DeserializerUtils.processFormula("npr", node, p, ctxt, LOGGER);
        if (nprFormula != null) {
            goal.setNprFormula(nprFormula);
            DeserializerUtils.logInfo(LOGGER, "Set npr formula for goal: " + goal.getId());
        }

        // Process refinements
        processRefinement(goal, getChildNode(node, "refinement"));
    }

    /**
     * Process refinement information for the goal.
     *
     * @param goal The goal to process refinement for
     * @param refinementNode The JSON node containing refinement information
     */
    private void processRefinement(Goal goal, JsonNode refinementNode) {
        if (refinementNode == null) {
            return;
        }

        // Set decomposition type
        String type = DeserializerUtils.getStringAttribute(refinementNode, "type", "TERM");
        if ("AND".equalsIgnoreCase(type)) {
            goal.setDecompType(DecompType.AND);
        } else if ("OR".equalsIgnoreCase(type)) {
            goal.setDecompType(DecompType.OR);
        } else {
            goal.setDecompType(DecompType.TERM);
        }

        // Process child goal references
        if (refinementNode.has("childGoal")) {
            goal.setChildGoalRefs(extractReferences(refinementNode.get("childGoal")));
        }

        // Process child task references
        if (refinementNode.has("childTask")) {
            goal.setChildTaskRefs(extractReferences(refinementNode.get("childTask")));
        }
    }
}