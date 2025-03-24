package com.example.xml.deserializers;

import com.example.objects.*;
import com.example.xml.utils.DeserializerUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Refactored deserializer for Goal objects with support for formula-based pre/npr.
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

        // Set episode length if specified
        String episodeLength = DeserializerUtils.getStringAttribute(node, "episodeLength", null);
        if (episodeLength != null) {
            goal.setEpisodeLength(episodeLength);

            // Convert to runs if possible
            try {
                int runs = Integer.parseInt(episodeLength);
                goal.setRuns(runs);
            } catch (NumberFormatException e) {
                goal.setRuns(1); // Default to 1 if not a valid number
            }
        }

        // Process pre formula
        processPreFormula(goal, node, p, ctxt);

        // Process npr formula
        processNprFormula(goal, node, p, ctxt);

        // Process refinements
        processRefinement(goal, getChildNode(node, "refinement"));
    }

    /**
     * Process the pre formula element for a goal.
     *
     * @param goal The goal to set the pre formula on
     * @param node The parent JSON node
     * @param p The JSON parser
     * @param ctxt The deserialization context
     */
    private void processPreFormula(Goal goal, JsonNode node, JsonParser p, DeserializationContext ctxt) throws IOException {
        if (node.has("pre")) {
            JsonNode preNode = node.get("pre");
            if (preNode.has("formula")) {
                try {
                    Formula formula = ctxt.readValue(preNode.get("formula").traverse(p.getCodec()), Formula.class);
                    goal.setPreFormula(formula);
                } catch (IOException e) {
                    LOGGER.warning("Error processing pre formula for goal: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Process the npr formula element for a goal.
     *
     * @param goal The goal to set the npr formula on
     * @param node The parent JSON node
     * @param p The JSON parser
     * @param ctxt The deserialization context
     */
    private void processNprFormula(Goal goal, JsonNode node, JsonParser p, DeserializationContext ctxt) throws IOException {
        if (node.has("npr")) {
            JsonNode nprNode = node.get("npr");
            if (nprNode.has("formula")) {
                try {
                    Formula formula = ctxt.readValue(nprNode.get("formula").traverse(p.getCodec()), Formula.class);
                    goal.setNprFormula(formula);
                } catch (IOException e) {
                    LOGGER.warning("Error processing npr formula for goal: " + e.getMessage());
                }
            }
        }
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