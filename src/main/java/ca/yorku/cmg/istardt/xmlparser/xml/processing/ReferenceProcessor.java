package ca.yorku.cmg.istardt.xmlparser.xml.processing;

import ca.yorku.cmg.istardt.xmlparser.objects.*;
import ca.yorku.cmg.istardt.xmlparser.xml.ReferenceResolver;
import ca.yorku.cmg.istardt.xmlparser.xml.utils.CustomLogger;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Post-processor for resolving references after deserialization.
 */
public class ReferenceProcessor {
    private static final CustomLogger LOGGER = CustomLogger.getInstance();
    private final XmlMapper xmlMapper;

    public ReferenceProcessor(XmlMapper xmlMapper) {
        this.xmlMapper = xmlMapper;
    }

    /**
     * Process the model to resolve all references between objects.
     *
     * @param model The model to process
     */
    public void processReferences(Model model) {
        if (model == null) {
            LOGGER.warning(getClass(),"Cannot process references for null model");
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

            processEffectSet(actor);
        }
        processAllFormulas(model);
        LOGGER.info(getClass(),"Reference processing completed successfully");
    }

    private void processEffectSet(Actor actor) {
        Map<Variable, Float> variableMap = new HashMap<>();
        for (Effect effect : actor.getEffects()){
            Map<String, Float> variableNameSet = effect.getVariableNameSet();
            for(String name: variableNameSet.keySet()){
                variableMap.put((Variable) ReferenceResolver.getInstance().getElementByName(name), variableNameSet.get(name));
            }
            effect.setVariableSet(variableMap);
        }
    }

    /**
     * Process CrossRunSet references to resolve them to elements.
     */
    private void processCrossRunSets(Actor actor) {
        CrossRunSet crossRunSet = actor.getCrossRunSet();
        if (crossRunSet == null || crossRunSet.getRefs().isEmpty()) {
            return;
        }
        LOGGER.info(getClass(),"Processing CrossRunSet references");
        for (String ref : crossRunSet.getRefs()) {
            Element element = ReferenceResolver.getInstance().getElementByName(ref);
            if (element != null) {
                crossRunSet.addElement(element);
                LOGGER.info(getClass(), "Resolved CrossRunSet reference: " + ref + " to element: " + element.getClass().getSimpleName());
            } else {
                LOGGER.warning(getClass(), "Failed to resolve CrossRunSet reference: " + ref);
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
        LOGGER.info(getClass(), "Processing ExportedSet references for actor: " + actor.getId());
        for (Export export : exportedSet.getExports()) {
            String ref = export.getRef();
            Element element = ReferenceResolver.getInstance().getElementByName(ref);
            if (element != null) {
                export.setElement(element);
                LOGGER.info(getClass(),"Resolved export reference: " + ref + " to element: " + element.getId());
            } else {
                LOGGER.warning(getClass(),"Failed to resolve export reference: " + ref);
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
        LOGGER.info(getClass(),"Processing InitializationSet references for actor: " + actor.getId());
        for (Initialization init : initializationSet.getInitializations()) {
            String ref = init.getRef();
            Element element = ReferenceResolver.getInstance().getElementByName(ref);
            if (element != null) {
                init.setElement(element);
                LOGGER.info(getClass(),"Resolved initialization reference: " + ref +
                        " to element: " + element.getName() +
                        " with value: " + init.getValue());
            } else {
                LOGGER.warning(getClass(),"Failed to resolve initialization reference: " + ref);
            }
        }
    }

    /**
     * Process goal refinements to establish parent-child relationships.
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
        LOGGER.info(getClass(),"Processing formulas...");
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
                        LOGGER.info(getClass(),"Set formula for quality: " + quality.getId());
                    }
                }
            }

            // Process condition formulas
            for (Condition condition : actor.getConditions()) {
                if (condition.getRawFormulaNode() != null) {
                    Formula formula = deserializeFormula(condition.getRawFormulaNode());
                    if (formula != null) {
                        condition.setFormula(formula);
                        LOGGER.info(getClass(),"Set formula for condition: " + condition.getName());
                    }
                }
            }
        }
        LOGGER.info(getClass(),"Formula processing completed");
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
                    LOGGER.info(getClass(),"Set pre formula for element: " + ((DecompositionElement) element).getId());
                }
            }
        }

        // Process npr formula
        if (nprNode != null) {
            Formula nprFormula = deserializeFormula(nprNode);
            if (nprFormula != null) {
                if (element instanceof DecompositionElement) {
                    ((DecompositionElement) element).setNprFormula(nprFormula);
                    LOGGER.info(getClass(),"Set npr formula for element: " + ((DecompositionElement) element).getId());
                }
            }
        }
    }


    /**
     * Deserialize a formula from a JSON node.
     */
    private Formula deserializeFormula(JsonNode node) {
        try {
            return xmlMapper.convertValue(node, Formula.class);
        } catch (Exception e) {
            LOGGER.error(getClass(),"Error deserializing formula: " + e.getMessage(), e);
            return null;
        }
    }
}