package com.example.xml.deserializers;

import com.example.objects.*;
import com.example.xml.utils.DeserializerUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.logging.Level;
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

        // Process pre formula with detailed logging
        if (node.has("pre")) {
            JsonNode preNode = node.get("pre");
            LOGGER.info("Processing pre node for goal " + goal.getId() + ": " + preNode.toString());

            if (preNode.has("formula")) {
                try {
                    LOGGER.info("Found formula element in pre node: " + preNode.get("formula").toString());
                    Formula formula = ctxt.readValue(preNode.get("formula").traverse(p.getCodec()), Formula.class);
                    goal.setPreFormula(formula);
                    LOGGER.info("Successfully set preFormula: " + formula.getFormula());
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Error processing pre formula for goal " + goal.getId() + ": " + e.getMessage(), e);
                }
            } else {
                LOGGER.warning("Pre node exists but formula element not found in goal " + goal.getId());
            }
        }

        // Process npr formula with detailed logging
        if (node.has("npr")) {
            JsonNode nprNode = node.get("npr");
            LOGGER.info("Processing npr node for goal " + goal.getId() + ": " + nprNode.toString());

            if (nprNode.has("formula")) {
                try {
                    LOGGER.info("Found formula element in npr node: " + nprNode.get("formula").toString());
                    Formula formula = ctxt.readValue(nprNode.get("formula").traverse(p.getCodec()), Formula.class);
                    goal.setNprFormula(formula);
                    LOGGER.info("Successfully set nprFormula: " + formula.getFormula());
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Error processing npr formula for goal " + goal.getId() + ": " + e.getMessage(), e);
                }
            } else {
                LOGGER.warning("Npr node exists but formula element not found in goal " + goal.getId());
            }
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