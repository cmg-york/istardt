package com.example.xml.processing;

import com.example.objects.*;
import com.example.xml.ReferenceResolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

        // Create a name-based lookup map for referencing by name instead of ID
        Map<String, Element> elementsByName = buildNameLookupMap(model);

        // Process each actor and its elements
        for (Actor actor : model.getActors()) {
            // Process goal hierarchies and refinements
            processGoalRefinements(actor.getGoals(), elementsByName);

            // Process task-effect relationships and collect effects
            List<Effect> allEffects = new ArrayList<>();
            for (Task task : actor.getTasks()) {
                processTaskEffects(task);

                // Collect effects
                if (task.getEffects() != null) {
                    nonDecompElements.addAll(task.getEffects());
                    allEffects.addAll(task.getEffects());
                }
            }
            actor.setDirectEffects(allEffects);

            // Process parent-child relationships in decomposition elements
            processDecompositionHierarchy(actor.getGoals());

            // Collect qualities
            if (actor.getQualities() != null) {
                nonDecompElements.addAll(actor.getQualities());
            }
        }

        // Collect all Condition objects (preBoxes) from ReferenceResolver
        collectNonDecompositionElements(nonDecompElements, Condition.class);

        // Collect all IndirectEffect objects from ReferenceResolver
        collectNonDecompositionElements(nonDecompElements, IndirectEffect.class);

        // Add all collected non-decomposition elements to the environment
        for (NonDecompositionElement element : nonDecompElements) {
            environment.addNonDecompElement(element);
        }

        // Log the number of non-decomposition elements
        LOGGER.info("Added " + nonDecompElements.size() + " non-decomposition elements to environment");

        LOGGER.info("Reference processing completed successfully");
    }

    /**
     * Builds a map of elements by their names (atom's titleText) for name-based lookup
     * This allows looking up elements by their XML name attribute rather than UUID
     *
     * @param model The model to process
     * @return A map of elements by name
     */
    private Map<String, Element> buildNameLookupMap(Model model) {
        Map<String, Element> elementsByName = new HashMap<>();

        for (Actor actor : model.getActors()) {
            // Add actor
            if (actor.getName() != null) {
                elementsByName.put(actor.getName(), actor);
            }

            // Add goals
            for (Goal goal : actor.getGoals()) {
                addElementByNameToMap(goal, elementsByName);
            }

            // Add tasks
            for (Task task : actor.getTasks()) {
                addElementByNameToMap(task, elementsByName);

                // Add effects
                if (task.getEffects() != null) {
                    for (Effect effect : task.getEffects()) {
                        addElementByNameToMap(effect, elementsByName);
                    }
                }
            }

            // Add qualities
            for (Quality quality : actor.getQualities()) {
                addElementByNameToMap(quality, elementsByName);
            }
        }

        // Add elements from the ReferenceResolver that might not be in actors
        for (String id : ReferenceResolver.getInstance().getAllElementIds()) {
            Element element = ReferenceResolver.getInstance().getElementById(id);
            addElementByNameToMap(element, elementsByName);
        }

        return elementsByName;
    }

    /**
     * Helper method to add an element to the name lookup map
     */
    private void addElementByNameToMap(Element element, Map<String, Element> map) {
        if (element != null && element.getAtom() != null && element.getAtom().getTitleText() != null) {
            map.put(element.getAtom().getTitleText(), element);
        }
    }

    /**
     * Collects elements of the specified type from the ReferenceResolver and adds them to the list.
     *
     * @param nonDecompElements The list to add the elements to
     * @param elementClass The class of elements to collect
     */
    private <T extends NonDecompositionElement> void collectNonDecompositionElements(
            List<NonDecompositionElement> nonDecompElements, Class<T> elementClass) {
        ReferenceResolver resolver = ReferenceResolver.getInstance();

        // Get all elements registered in the resolver
        for (String id : resolver.getAllElementIds()) {
            Element element = resolver.getElementById(id);
            if (elementClass.isInstance(element)) {
                nonDecompElements.add((NonDecompositionElement) element);
                LOGGER.fine("Added " + elementClass.getSimpleName() + ": " +
                        (element.getAtom() != null ? element.getAtom().getTitleText() : id));
            }
        }
    }

    /**
     * Process goal refinements to establish parent-child relationships.
     * Uses name-based lookup rather than ID-based lookup.
     *
     * @param goals The list of goals to process
     * @param elementsByName The map of elements by name for lookup
     */
    private void processGoalRefinements(List<Goal> goals, Map<String, Element> elementsByName) {
        if (goals == null) return;

        for (Goal goal : goals) {
            // Look for refinement references
            List<String> childGoalRefs = goal.getChildGoalRefs();
            List<String> childTaskRefs = goal.getChildTaskRefs();

            // Process child goal references
            if (childGoalRefs != null) {
                for (String ref : childGoalRefs) {
                    Element element = elementsByName.get(ref);
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
                    Element element = elementsByName.get(ref);
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