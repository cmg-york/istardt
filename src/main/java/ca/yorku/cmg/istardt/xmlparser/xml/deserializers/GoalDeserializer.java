package ca.yorku.cmg.istardt.xmlparser.xml.deserializers;

import ca.yorku.cmg.istardt.xmlparser.xml.utils.DeserializerUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import ca.yorku.cmg.istardt.xmlparser.objects.DecompType;
import ca.yorku.cmg.istardt.xmlparser.objects.Goal;

import java.io.IOException;
import java.util.logging.Logger;

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

        // Store formula nodes for future processing
        if (node.has("pre")) {
            goal.setRawPreFormulaNode(node.get("pre"));
        }
        if (node.has("npr")) {
            goal.setRawNprFormulaNode(node.get("npr"));
        }

        // Process refinements
        processRefinement(goal, getChildNode(node, "refinement"));
    }

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