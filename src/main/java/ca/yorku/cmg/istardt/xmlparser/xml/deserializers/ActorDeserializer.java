package ca.yorku.cmg.istardt.xmlparser.xml.deserializers;

import ca.yorku.cmg.istardt.xmlparser.objects.*;
import ca.yorku.cmg.istardt.xmlparser.xml.utils.DeserializerUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

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
                LOGGER.info("Processed predicates");
            }

            // Process variables
            if (node.has("variables") && node.get("variables").has("variable")) {
                JsonNode variablesNode = node.get("variables").get("variable");
                List<Variable> variables = DeserializerUtils.deserializeList(variablesNode, p, ctxt, Variable.class);
                actor.setVariables(variables);
                LOGGER.info("Processed variables");
            }

            // Process crossruns
            if (node.has("crossRuns") && node.get("crossRuns").has("crossRun")) {
                JsonNode crossRunNodes = node.get("crossRuns").get("crossRun");
                CrossRunSet crossRunSet = new CrossRunSet();
                actor.setCrossRunSet(crossRunSet);
                if (crossRunNodes.isArray()) {
                    for (JsonNode crossRunNode : crossRunNodes) {
                        processCrossRunNode(crossRunNode, crossRunSet);
                    }
                } else {
                    processCrossRunNode(crossRunNodes, crossRunSet);
                }
                LOGGER.info("Processed crossRuns");
            }

            // Process exports
            if (node.has("exportedSet") && node.get("exportedSet").has("export")) {
                JsonNode exportNodes = node.get("exportedSet").get("export");
                ExportedSet exportedSet = new ExportedSet();
                actor.setExportedSet(exportedSet);
                if (exportNodes.isArray()) {
                    for (JsonNode exportNode : exportNodes) {
                        processExportNode(exportNode, exportedSet);
                    }
                } else {
                    processExportNode(exportNodes, exportedSet);
                }
                LOGGER.info("Processed ExportedSet");
            }

            // Process initializations
            if (node.has("initializations") && node.get("initializations").has("initialization")) {
                JsonNode initNodes = node.get("initializations").get("initialization");
                InitializationSet initializationSet = new InitializationSet();
                actor.setInitializationSet(initializationSet);
                if (initNodes.isArray()) {
                    for (JsonNode initNode : initNodes) {
                        processInitializationNode(initNode, initializationSet);
                    }
                } else {
                    processInitializationNode(initNodes, initializationSet);
                }
                LOGGER.info("Processed Initializations");
            }

            // Process condBoxes
            if (node.has("condBoxes") && node.get("condBoxes").has("condBox")) {
                JsonNode preBoxesNode = node.get("condBoxes").get("condBox");
                List<Condition> conditions = DeserializerUtils.deserializeList(preBoxesNode, p, ctxt, Condition.class);
                actor.setConditions(conditions);
                LOGGER.info("Processed condBoxes");
            }

            // Process qualities
            if (node.has("qualities") && node.get("qualities").has("quality")) {
                JsonNode qualitiesNode = node.get("qualities").get("quality");
                List<Quality> qualities = DeserializerUtils.deserializeList(qualitiesNode, p, ctxt, Quality.class);
                actor.setQualities(qualities);
                LOGGER.info("Processed condBoxes");
            }

            // Process goals
            if (node.has("goals") && node.get("goals").has("goal")) {
                JsonNode goalsNode = node.get("goals").get("goal");
                List<Goal> goals = DeserializerUtils.deserializeList(goalsNode, p, ctxt, Goal.class);
                actor.setGoals(goals);
                LOGGER.info("Processed goals");
            }

            // Process tasks
            if (node.has("tasks") && node.get("tasks").has("task")) {
                JsonNode tasksNode = node.get("tasks").get("task");
                List<Task> tasks = DeserializerUtils.deserializeList(tasksNode, p, ctxt, Task.class);
                actor.setTasks(tasks);
                LOGGER.info("Processed tasks");

            }
        } catch (IOException e) {
            String name = actor.getAtom() != null ? actor.getAtom().getTitleText() : actor.getId();
            DeserializerUtils.handleDeserializationError(LOGGER, "Error deserializing actor " + name, e);
        }
        String name = actor.getAtom() != null ? actor.getAtom().getTitleText() : actor.getId();
        LOGGER.info("Deserialized actor: " + name);
    }

    private void processCrossRunNode(JsonNode crossRunNode, CrossRunSet crossRunSet) {
        if (crossRunNode.has("qualID")) {
            String qualID = crossRunNode.get("qualID").asText();
            crossRunSet.addRefs(qualID);
            LOGGER.info("Added quality reference to CrossRunSet: " + qualID);
        } else if (crossRunNode.has("predicateID")) {
            String predicateID = crossRunNode.get("predicateID").asText();
            crossRunSet.addRefs(predicateID);
            LOGGER.info("Added predicate reference to CrossRunSet: " + predicateID);
        } else if (crossRunNode.has("variableID")) {
            String variableID = crossRunNode.get("variableID").asText();
            crossRunSet.addRefs(variableID);
            LOGGER.info("Added variable reference to CrossRunSet: " + variableID);
        } else {
            LOGGER.warning("Unknown reference type in CrossRun: " + crossRunNode);
        }
    }

    private void processExportNode(JsonNode exportNode, ExportedSet exportedSet) {
        Export export = new Export();
        boolean continuous = DeserializerUtils.getBooleanAttribute(exportNode, "continuous", false);
        export.setContinuous(continuous);
        if (continuous) {
            float minVal = DeserializerUtils.getFloatAttribute(exportNode, "minVal", 0.0f);
            float maxVal = DeserializerUtils.getFloatAttribute(exportNode, "maxVal", 0.0f);
            export.setMinVal(minVal);
            export.setMaxVal(maxVal);
        }

        String refValue = null;
        if (exportNode.has("goalID")) {
            refValue = exportNode.get("goalID").asText();
        } else if (exportNode.has("taskID")) {
            refValue = exportNode.get("taskID").asText();
        } else if (exportNode.has("predicateID")) {
            refValue = exportNode.get("predicateID").asText();
        } else if (exportNode.has("variableID")) {
            refValue = exportNode.get("variableID").asText();
        } else if (exportNode.has("qualID")) {
            refValue = exportNode.get("qualID").asText();
        }

        if (refValue != null) {
            export.setRef(refValue);
            exportedSet.addExport(export);
            LOGGER.info("Added export with reference: " + refValue);
        } else {
            LOGGER.warning("Export node without a valid reference: " + exportNode);
        }
    }
    private void processInitializationNode(JsonNode initNode, InitializationSet initializationSet) {
        Initialization initialization = new Initialization();
        String element = DeserializerUtils.getStringAttribute(initNode, "element", null);
        if (element != null) {
            initialization.setRef(element);
            if (initNode.has("")) {
                initialization.setValue(initNode.get("").asText().trim());
            } else {
                LOGGER.warning("Initialization node without a value: " + initNode);
                return; // skip
            }
            initializationSet.addInitialization(initialization);
            LOGGER.info("Added initialization for element: " + element + " with value: " + initialization.getValue());
        } else {
            LOGGER.warning("Initialization node without an element attribute: " + initNode);
        }
    }
}
