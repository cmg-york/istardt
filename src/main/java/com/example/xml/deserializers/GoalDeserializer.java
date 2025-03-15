package com.example.xml.deserializers;

import com.example.objects.*;
import com.example.xml.ReferenceResolver;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Deserializer for Goal objects.
 */
public class GoalDeserializer extends BaseDeserializer<Goal> {
    private static final Logger LOGGER = Logger.getLogger(GoalDeserializer.class.getName());

    public GoalDeserializer() {
        super(Goal.class);
    }

    @Override
    public Goal deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        // Create new Goal
        Goal goal = new Goal();

        // Set basic properties
        String name = getName(node);
        String description = getDescription(node);
        boolean root = getBooleanAttribute(node, "root", false);

        goal.setId(name);
        goal.setRoot(root);

        // Create an atom for the goal
        Atom atom = createAtom(name, description);
        goal.setRepresentation(atom);

        // Set episode length if specified
        if (node.has("episodeLength")) {
            String episodeLength = node.get("episodeLength").asText();
            goal.setEpisodeLength(episodeLength);

            // Convert to runs if possible
            try {
                int runs = Integer.parseInt(episodeLength);
                goal.setRuns(runs);
            } catch (NumberFormatException e) {
                goal.setRuns(1); // Default to 1 if not a valid number
            }
        }

        // Process preconditions
        List<String> preconditions = getStringList(node, "pre");
        for (String pre : preconditions) {
            goal.addPrecondition(pre);
        }

        // Process negative preconditions
        List<String> negPreconditions = getStringList(node, "npr");
        for (String npr : negPreconditions) {
            goal.addNegPrecondition(npr);
        }

        // Process refinements
        if (node.has("refinement")) {
            JsonNode refinementNode = node.get("refinement");
            processRefinement(goal, refinementNode);
        }

        // Register the goal for reference resolution
        registerElement(goal);

        return goal;
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
        if (refinementNode.has("type")) {
            String type = refinementNode.get("type").asText();
            if ("AND".equalsIgnoreCase(type)) {
                goal.setDecompType(DecompType.AND);
            } else if ("OR".equalsIgnoreCase(type)) {
                goal.setDecompType(DecompType.OR);
            } else {
                goal.setDecompType(DecompType.TERM);
            }
        } else {
            goal.setDecompType(DecompType.TERM);
        }

        // Process child goal references
        if (refinementNode.has("childGoal")) {
            List<String> childGoalRefs = new ArrayList<>();
            JsonNode childGoalNodes = refinementNode.get("childGoal");

            if (childGoalNodes.isArray()) {
                for (JsonNode childNode : childGoalNodes) {
                    if (childNode.has("ref")) {
                        childGoalRefs.add(childNode.get("ref").asText());
                    }
                }
            } else if (childGoalNodes.has("ref")) {
                childGoalRefs.add(childGoalNodes.get("ref").asText());
            }

            goal.setChildGoalRefs(childGoalRefs);
        }

        // Process child task references
        if (refinementNode.has("childTask")) {
            List<String> childTaskRefs = new ArrayList<>();
            JsonNode childTaskNodes = refinementNode.get("childTask");

            if (childTaskNodes.isArray()) {
                for (JsonNode childNode : childTaskNodes) {
                    if (childNode.has("ref")) {
                        childTaskRefs.add(childNode.get("ref").asText());
                    }
                }
            } else if (childTaskNodes.has("ref")) {
                childTaskRefs.add(childTaskNodes.get("ref").asText());
            }

            goal.setChildTaskRefs(childTaskRefs);
        }
    }
}