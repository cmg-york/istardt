package com.example.xml.processing;

import com.example.objects.*;
import com.example.xml.ReferenceResolver;

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

        Environment environment = model.getEnvironment();
        if (environment == null) {
            environment = new Environment();
            model.setEnvironment(environment);
        }

        // List to collect all non-decomposition elements
        List<NonDecompositionElement> nonDecompElements = new ArrayList<>();

        // Process each actor and its elements
        for (Actor actor : model.getActors()) {
            // Process goal hierarchies and refinements
            processGoalRefinements(actor.getGoals());

            // Process task-effect relationships and collect effects
            for (Task task : actor.getTasks()) {
                processTaskEffects(task);

                // Collect effects
                if (task.getEffects() != null) {
                    nonDecompElements.addAll(task.getEffects());
                }
            }

            // Process parent-child relationships in decomposition elements
            processDecompositionHierarchy(actor.getGoals());

            // Collect qualities
            if (actor.getQualities() != null) {
                nonDecompElements.addAll(actor.getQualities());
            }
        }

        // Collect all Condition objects (preBoxes) from ReferenceResolver
        collectConditions(nonDecompElements);

        // Collect all IndirectEffect objects from ReferenceResolver
        collectIndirectEffects(nonDecompElements);

        // Add all collected non-decomposition elements to the environment
        for (NonDecompositionElement element : nonDecompElements) {
            environment.addNonDecompElement(element);
        }

        // Log the number of non-decomposition elements
        LOGGER.info("Added " + nonDecompElements.size() + " non-decomposition elements to environment");

        LOGGER.info("Reference processing completed successfully");
    }

    /**
     * Collect all Condition objects from the ReferenceResolver and add them to the list.
     *
     * @param nonDecompElements The list to add the conditions to
     */
    private void collectConditions(List<NonDecompositionElement> nonDecompElements) {
        ReferenceResolver resolver = ReferenceResolver.getInstance();

        // Get all elements registered in the resolver
        for (String id : resolver.getAllElementIds()) {
            Element element = resolver.getElementById(id);
            if (element instanceof Condition) {
                nonDecompElements.add((Condition) element);
                LOGGER.fine("Added condition: " + id);
            }
        }
    }

    /**
     * Collect all IndirectEffect objects from the ReferenceResolver and add them to the list.
     *
     * @param nonDecompElements The list to add the indirect effects to
     */
    private void collectIndirectEffects(List<NonDecompositionElement> nonDecompElements) {
        ReferenceResolver resolver = ReferenceResolver.getInstance();

        // Get all elements registered in the resolver
        for (String id : resolver.getAllElementIds()) {
            Element element = resolver.getElementById(id);
            if (element instanceof IndirectEffect) {
                nonDecompElements.add((IndirectEffect) element);
                LOGGER.fine("Added indirect effect: " + id);
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
            // Look for refinement references
            List<String> childGoalRefs = goal.getChildGoalRefs();
            List<String> childTaskRefs = goal.getChildTaskRefs();

            // Process child goal references
            if (childGoalRefs != null) {
                for (String ref : childGoalRefs) {
                    Element element = ReferenceResolver.getInstance().getElementById(ref);
                    if (element instanceof Goal) {
                        if (goal.getDecompType() == DecompType.AND) {
                            goal.addANDChild((Goal) element);
                        } else if (goal.getDecompType() == DecompType.OR) {
                            goal.addORChild((Goal) element);
                        }
                    }
                }
            }

            // Process child task references
            if (childTaskRefs != null) {
                for (String ref : childTaskRefs) {
                    Element element = ReferenceResolver.getInstance().getElementById(ref);
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
     * Process task effects to establish the two-way relationship between tasks and effects.
     *
     * @param task The task to process
     */
    private void processTaskEffects(Task task) {
        if (task == null || task.getEffects() == null) return;

        for (Effect effect : task.getEffects()) {
            // Set the task as the parent of the effect
            effect.setTask(task);
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
}