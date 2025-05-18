package ca.yorku.cmg.istardt.xmlparser.xml.deserializers;

import ca.yorku.cmg.istardt.xmlparser.objects.*;
import ca.yorku.cmg.istardt.xmlparser.xml.utils.DeserializerUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

/**
 * Deserializer for Actor objects.
 */
public class ActorDeserializer extends BaseDeserializer<Actor> {
    private static final Logger LOGGER = Logger.getLogger(ActorDeserializer.class.getName());

    public ActorDeserializer() {
        super(Actor.class);
    }

    @Override
    protected Actor createNewElement() {
        return new Actor();
    }

    @Override
    protected void handleSpecificAttributes(Actor actor, JsonNode node, JsonParser p, DeserializationContext ctxt) throws IOException {
        // The name is already handled by the base deserializer in extractCommonAttributes
        // which sets up the atom from the XML

        try {
            // Process predicates
            if (node.has("predicates") && node.get("predicates").has("predicate")) {
                JsonNode predicatesNode = node.get("predicates").get("predicate");
                List<Predicate> predicates = DeserializerUtils.deserializeList(predicatesNode, p, ctxt, Predicate.class);
                actor.setPredicates(predicates);
            }

            if (node.has("variables") && node.get("variables").has("variable")) {
                JsonNode variablesNode = node.get("variables").get("variable");
                List<Variable> variables = DeserializerUtils.deserializeList(variablesNode, p, ctxt, Variable.class);
                actor.setVariables(variables);
            }


            // Process condBoxes
            if (node.has("condBoxes") && node.get("condBoxes").has("condBox")) {
                JsonNode preBoxesNode = node.get("condBoxes").get("condBox");
                List<Condition> conditions = DeserializerUtils.deserializeList(preBoxesNode, p, ctxt, Condition.class);
                actor.setConditions(conditions);
            }

            // Process qualities
            if (node.has("qualities") && node.get("qualities").has("quality")) {
                JsonNode qualitiesNode = node.get("qualities").get("quality");
                List<Quality> qualities = DeserializerUtils.deserializeList(qualitiesNode, p, ctxt, Quality.class);
                actor.setQualities(qualities);
            }

            // Process goals
            if (node.has("goals") && node.get("goals").has("goal")) {
                JsonNode goalsNode = node.get("goals").get("goal");
                List<Goal> goals = DeserializerUtils.deserializeList(goalsNode, p, ctxt, Goal.class);
                actor.setGoals(goals);
            }

            // Process tasks
            if (node.has("tasks") && node.get("tasks").has("task")) {
                JsonNode tasksNode = node.get("tasks").get("task");
                List<Task> tasks = DeserializerUtils.deserializeList(tasksNode, p, ctxt, Task.class);
                actor.setTasks(tasks);
            }
        } catch (IOException e) {
            // Get name from atom for logging
            String name = actor.getAtom() != null ? actor.getAtom().getTitleText() : actor.getId();
            DeserializerUtils.handleDeserializationError(LOGGER, "Error deserializing actor " + name, e);
        }

        // Log with name from atom for consistency
        String name = actor.getAtom() != null ? actor.getAtom().getTitleText() : actor.getId();
        LOGGER.info("Deserialized actor: " + name);
    }
}