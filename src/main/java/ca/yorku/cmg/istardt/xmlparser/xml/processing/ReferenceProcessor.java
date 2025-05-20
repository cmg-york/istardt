package ca.yorku.cmg.istardt.xmlparser.xml.processing;

import ca.yorku.cmg.istardt.xmlparser.objects.*;
import ca.yorku.cmg.istardt.xmlparser.xml.ReferenceResolver;
import ca.yorku.cmg.istardt.xmlparser.xml.deserializers.FormulaDeserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Post-processor for resolving references after deserialization.
 * This class handles establishing relationships between objects
 * that were unmarshalled from XML.
 */
public class ReferenceProcessor {
    private static final Logger LOGGER = Logger.getLogger(ReferenceProcessor.class.getName());

    /**
     * Process the model to resolve all references between objects.
     * This connects parent-child relationships and establishes cross-references.
     * It also collects all non-decomposition elements and adds them to the environment.
     *
     * @param model The model to process
     */
    public void processReferences(Model model) {
        if (model == null) {
            LOGGER.warning("Cannot process references for null model");
            return;
        }
        // Process each actor and its elements
        for (Actor actor : model.getActors()) {
            // Process goal hierarchies and refinements
            processGoalRefinements(actor.getGoals());

            // Collect effects
            List<Effect> allEffects = new ArrayList<>();
            for (Task task : actor.getTasks()) {
                if (task.getEffects() != null) {
                    allEffects.addAll(task.getEffects());
                }
            }
            actor.setEffects(allEffects);

            // Process parent-child relationships in decomposition elements
            processDecompositionHierarchy(actor.getGoals());

            processCrossRunSets(actor);
            processExportedSet(actor);
            processInitializationSet(actor);
        }
        processAllFormulas(model);
        LOGGER.info("Reference processing completed successfully");
    }

    /**
     * Process CrossRunSet references to resolve them to elements.
     */
    private void processCrossRunSets(Actor actor) {
        CrossRunSet crossRunSet = actor.getCrossRunSet();
        if (crossRunSet == null || crossRunSet.getRefs().isEmpty()) {
            return;
        }
        LOGGER.info("Processing CrossRunSet references");
        for (String ref : crossRunSet.getRefs()) {
            Element element = ReferenceResolver.getInstance().getElementByName(ref);
            if (element != null) {
                crossRunSet.addElement(element);
                LOGGER.info("Resolved CrossRunSet reference: " + ref + " to element: " + element.getName());
            } else {
                LOGGER.warning("Failed to resolve CrossRunSet reference: " + ref);
            }
        }
    }


    /**
     * Process ExportedSet references to resolve them to elements.
     */
    private void processExportedSet(Actor actor) {
        ExportedSet exportedSet = actor.getExportedSet();
        if (exportedSet == null || exportedSet.getExports().isEmpty()) {
            return;
        }
        LOGGER.info("Processing ExportedSet references for actor: " + actor.getId());
        for (Export export : exportedSet.getExports()) {
            String ref = export.getRef();
            Element element = ReferenceResolver.getInstance().getElementByName(ref);
            if (element != null) {
                export.setElement(element);
                LOGGER.info("Resolved export reference: " + ref + " to element: " + element.getId());
            } else {
                LOGGER.warning("Failed to resolve export reference: " + ref);
            }
        }
    }

    /**
     * Process InitializationSet references to resolve them to elements.
     */
    private void processInitializationSet(Actor actor) {
        InitializationSet initializationSet = actor.getInitializationSet();
        if (initializationSet == null || initializationSet.getInitializations().isEmpty()) {
            return;
        }
        LOGGER.info("Processing InitializationSet references for actor: " + actor.getId());
        for (Initialization init : initializationSet.getInitializations()) {
            String ref = init.getRef();
            Element element = ReferenceResolver.getInstance().getElementByName(ref);
            if (element != null) {
                init.setElement(element);
                LOGGER.info("Resolved initialization reference: " + ref +
                        " to element: " + element.getName() +
                        " with value: " + init.getValue());
            } else {
                LOGGER.warning("Failed to resolve initialization reference: " + ref);
            }
        }
    }

    /**
     * Process goal refinements to establish parent-child relationships.
     * Uses name-based lookup rather than ID-based lookup.
     *
     * @param goals The list of goals to process
     */
    private void processGoalRefinements(List<Goal> goals) {
        if (goals == null) return;

        for (Goal goal : goals) {
            // Look for refinement ref
            List<String> childGoalRefs = goal.getChildGoalRefs();
            List<String> childTaskRefs = goal.getChildTaskRefs();

            // Process child goal refs
            if (childGoalRefs != null) {
                for (String ref : childGoalRefs) {
                    Element element = ReferenceResolver.getInstance().getElementByName(ref);
                    if (element instanceof Goal) {
                        if (goal.getDecompType() == DecompType.AND) {
                            goal.addANDChild((Goal) element);
                        } else if (goal.getDecompType() == DecompType.OR) {
                            goal.addORChild((Goal) element);
                        }
                    }
                }
            }

            // Process child task refs
            if (childTaskRefs != null) {
                for (String ref : childTaskRefs) {
                    Element element = ReferenceResolver.getInstance().getElementByName(ref);
                    if (element instanceof Task) {
                        if (goal.getDecompType() == DecompType.AND) {
                            goal.addANDChild((Task) element);
                        } else if (goal.getDecompType() == DecompType.OR) {
                            goal.addORChild((Task) element);
                        }
                    }
                }
            }
        }
    }

    /**
     * Process decomposition hierarchy to establish parent-child relationships
     * between decomposition elements.
     *
     * @param goals The list of root goals to process
     */
    private void processDecompositionHierarchy(List<Goal> goals) {
        if (goals == null) return;

        for (Goal goal : goals) {
            if (goal.getChildren() != null) {
                for (DecompositionElement child : goal.getChildren()) {
                    // Set parent-child relationships
                    child.setParent(goal);
                }
            }
        }
    }

    private void processAllFormulas(Model model) {
        LOGGER.info("Processing formulas...");
        for (Actor actor : model.getActors()) {
            // Process goal formulas
            for (Goal goal : actor.getGoals()) {
                processElementFormulas(goal);
            }

            // Process task formulas
            for (Task task : actor.getTasks()) {
                processElementFormulas(task);

                // Process effect formulas
                for (Effect effect : task.getEffects()) {
                    processElementFormulas(effect);
                }
            }

            // Process quality formulas
            for (Quality quality : actor.getQualities()) {
                if (quality.getRawFormulaNode() != null) {
                    Formula formula = deserializeFormula(quality.getRawFormulaNode());
                    if (formula != null) {
                        quality.setFormula(formula);
                        LOGGER.info("Set formula for quality: " + quality.getId());
                    }
                }
            }

            // Process condition formulas
            for (Condition condition : actor.getConditions()) {
                if (condition.getRawFormulaNode() != null) {
                    Formula formula = deserializeFormula(condition.getRawFormulaNode());
                    if (formula != null) {
                        condition.setFormula(formula);
                        LOGGER.info("Set formula for condition: " + condition.getId());
                    }
                }
            }
        }
        LOGGER.info("Formula processing completed");
    }

    /**
     * Process pre and npr formulas for decomposition elements.
     */
    private void processElementFormulas(Object element) {
        JsonNode preNode = null;
        JsonNode nprNode = null;

        // Extract raw formula nodes based on element type
        if (element instanceof DecompositionElement) {
            DecompositionElement decompElement = (DecompositionElement) element;
            preNode = decompElement.getRawPreFormulaNode();
            nprNode = decompElement.getRawNprFormulaNode();
        }

        // Process pre formula
        if (preNode != null) {
            Formula preFormula = deserializeFormula(preNode);
            if (preFormula != null) {
                if (element instanceof DecompositionElement) {
                    ((DecompositionElement) element).setPreFormula(preFormula);
                    LOGGER.info("Set pre formula for element: " + ((DecompositionElement) element).getId());
                }
            }
        }

        // Process npr formula
        if (nprNode != null) {
            Formula nprFormula = deserializeFormula(nprNode);
            if (nprFormula != null) {
                if (element instanceof DecompositionElement) {
                    ((DecompositionElement) element).setNprFormula(nprFormula);
                    LOGGER.info("Set npr formula for element: " + ((DecompositionElement) element).getId());
                }
            }
        }
    }

    /**
     * Deserialize a formula from a JSON node.
     */
    private Formula deserializeFormula(JsonNode node) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonParser parser = mapper.treeAsTokens(node);
            DeserializationContext ctxt = mapper.getDeserializationContext();
            FormulaDeserializer deserializer = new FormulaDeserializer();
            return deserializer.deserialize(parser, ctxt);
        } catch (Exception e) {
            LOGGER.warning("Error deserializing formula: " + e.getMessage());
            return null;
        }
    }
}