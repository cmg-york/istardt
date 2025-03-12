package com.example;

import com.example.objects.*;
import com.example.xml.IStarUnmarshaller;
import jakarta.xml.bind.JAXBException;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class IStarTApplication {

    public static void main(String[] args) {
        try {
            // Load the XML file from the resources folder using the class loader
            InputStream xmlStream = IStarTApplication.class.getResourceAsStream("/xml/figure1a_fixed.xml");
            if (xmlStream == null) {
                System.err.println("Error: XML resource '/xml/figure1a_fixed.xml' not found in the classpath.");
                System.exit(1);
            }
            System.out.println("Loading XML file from resources: /xml/figure1a_fixed.xml");

            // Create a temporary file and copy the contents of the resource into it
            File tempFile = File.createTempFile("figure1a_fixed", ".xml");
            tempFile.deleteOnExit();
            Files.copy(xmlStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Copied resource to temporary file: " + tempFile.getAbsolutePath());

            // Create unmarshaller
            IStarUnmarshaller unmarshaller = new IStarUnmarshaller();

            // Unmarshal XML to model using the temporary file
            Model model = unmarshaller.unmarshalToModel(tempFile);

            // Display model information
            printModelInformation(model);

        } catch (JAXBException e) {
            System.err.println("Error unmarshalling XML: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Print information about the model structure
     */
    private static void printModelInformation(Model model) {
        System.out.println("\n=== iStar-T Model Information ===");
        System.out.println("Number of actors: " + model.getActors().size());

        // Process each actor
        int actorCount = 1;
        for (Actor actor : model.getActors()) {
            System.out.println("\nActor " + actorCount + ": " + actor.getName());

            // Goals
            System.out.println("  Goals (" + actor.getGoals().size() + "):");
            for (Goal goal : actor.getGoals()) {
                printGoalInfo(goal, 2);
            }

            // Tasks
            System.out.println("  Tasks (" + actor.getTasks().size() + "):");
            for (Task task : actor.getTasks()) {
                printTaskInfo(task, 2);
            }

            // Qualities
            System.out.println("  Qualities (" + actor.getQualities().size() + "):");
            for (Quality quality : actor.getQualities()) {
                String titleText = quality.getAtom() != null && quality.getAtom().getTitleText() != null ?
                        " (" + quality.getAtom().getTitleText() + ")" : "";
                Formula formula = quality.getValueFormula();
                String formulaText = formula != null ? " [Formula: " + formula.getFormula() + "]" : "";

                System.out.println("    - " + quality.getId() + titleText +
                        (quality.isRoot() ? " [ROOT]" : "") + formulaText);
            }
            actorCount++;
        }

        // Print environment information
        System.out.println("\nEnvironment Information:");
        if (model.getEnvironment() != null) {
            int elementsCount = model.getEnvironment().getNonDecompElements() != null
                    ? model.getEnvironment().getNonDecompElements().size() : 0;
            System.out.println("  Non-decomposition elements: " + elementsCount);

            if (elementsCount > 0) {
                int conditionCount = 0;
                int indirectEffectCount = 0;
                int effectCount = 0;
                int qualityCount = 0;  // Counter for qualities

                for (NonDecompositionElement element : model.getEnvironment().getNonDecompElements()) {
                    if (element instanceof Condition) {
                        conditionCount++;
                    } else if (element instanceof IndirectEffect) {
                        indirectEffectCount++;
                    } else if (element instanceof Effect) {
                        effectCount++;
                    } else if (element instanceof Quality) {
                        qualityCount++;
                    }
                }

                System.out.println("    - Conditions (PreBoxes): " + conditionCount);
                System.out.println("    - Indirect Effects: " + indirectEffectCount);
                System.out.println("    - Effects: " + effectCount);
                System.out.println("    - Qualities: " + qualityCount);

                // Print detailed information about Conditions (PreBoxes)
                if (conditionCount > 0) {
                    System.out.println("\n  Conditions (PreBoxes):");
                    for (NonDecompositionElement element : model.getEnvironment().getNonDecompElements()) {
                        if (element instanceof Condition) {
                            Condition condition = (Condition) element;
                            String titleText = condition.getAtom() != null && condition.getAtom().getTitleText() != null ?
                                    " (" + condition.getAtom().getTitleText() + ")" : "";
                            Formula formula = condition.getValueFormula();
                            String formulaText = formula != null ? " [Formula: " + formula.getFormula() + "]" : "";
                            System.out.println("    - " + condition.getId() + titleText + formulaText);
                        }
                    }
                }

                // Print detailed information about IndirectEffects
                if (indirectEffectCount > 0) {
                    System.out.println("\n  Indirect Effects:");
                    for (NonDecompositionElement element : model.getEnvironment().getNonDecompElements()) {
                        if (element instanceof IndirectEffect) {
                            IndirectEffect indirectEffect = (IndirectEffect) element;
                            String titleText = indirectEffect.getAtom() != null && indirectEffect.getAtom().getTitleText() != null ?
                                    " (" + indirectEffect.getAtom().getTitleText() + ")" : "";
                            Formula formula = indirectEffect.getValueFormula();
                            String formulaText = formula != null ? " [Formula: " + formula.getFormula() + "]" : "";
                            System.out.println("    - " + indirectEffect.getId() + titleText +
                                    (indirectEffect.isExported() ? " [Exported]" : "") + formulaText);
                        }
                    }
                }

                // Print detailed information about Effects
                if (effectCount > 0) {
                    System.out.println("\n  Effects:");
                    for (NonDecompositionElement element : model.getEnvironment().getNonDecompElements()) {
                        if (element instanceof Effect) {
                            Effect effect = (Effect) element;
                            String titleText = effect.getAtom() != null && effect.getAtom().getTitleText() != null ?
                                    " (" + effect.getAtom().getTitleText() + ")" : "";
                            System.out.println("    - " + effect.getId() + titleText +
                                    " (Probability: " + effect.getProbability() +
                                    ", Satisfying: " + effect.isSatisfying() + ")");
                        }
                    }
                }

                // Print detailed information about Qualities in the Environment
                if (qualityCount > 0) {
                    System.out.println("\n  Qualities in Environment:");
                    for (NonDecompositionElement element : model.getEnvironment().getNonDecompElements()) {
                        if (element instanceof Quality) {
                            Quality quality = (Quality) element;
                            String titleText = quality.getAtom() != null && quality.getAtom().getTitleText() != null ?
                                    " (" + quality.getAtom().getTitleText() + ")" : "";
                            Formula formula = quality.getValueFormula();
                            String formulaText = formula != null ? " [Formula: " + formula.getFormula() + "]" : "";
                            System.out.println("    - " + quality.getId() + titleText +
                                    (quality.isRoot() ? " [ROOT]" : "") + formulaText);
                        }
                    }
                }
            }
        } else {
            System.out.println("  No environment information available");
        }
    }

    /**
     * Print information about a goal, with proper indentation
     */
    private static void printGoalInfo(Goal goal, int indentLevel) {
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < indentLevel; i++) {
            indent.append("  ");
        }

        String titleText = goal.getAtom() != null && goal.getAtom().getTitleText() != null ?
                " (" + goal.getAtom().getTitleText() + ")" : "";
        System.out.println(indent + "- " + goal.getId() + titleText +
                " [Type: " + goal.getDecompType() + "]");

        // Print child elements if this is a decomposition element
        if (goal.getChildren() != null && !goal.getChildren().isEmpty()) {
            System.out.println(indent + "  Children:");
            for (DecompositionElement child : goal.getChildren()) {
                if (child instanceof Goal) {
                    printGoalInfo((Goal) child, indentLevel + 2);
                } else if (child instanceof Task) {
                    printTaskInfo((Task) child, indentLevel + 2);
                } else {
                    System.out.println(indent + "    - " + child.getId());
                }
            }
        }
    }

    /**
     * Print information about a task, with proper indentation
     */
    private static void printTaskInfo(Task task, int indentLevel) {
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < indentLevel; i++) {
            indent.append("  ");
        }

        String titleText = task.getAtom() != null && task.getAtom().getTitleText() != null ?
                " (" + task.getAtom().getTitleText() + ")" : "";
        System.out.println(indent + "- " + task.getId() + titleText +
                " [Deterministic: " + task.isDeterministic() + "]");

        // Print effects
        if (task.getEffects() != null && !task.getEffects().isEmpty()) {
            System.out.println(indent + "  Effects:");
            for (Effect effect : task.getEffects()) {
                System.out.println(indent + "    - " + effect.getId() +
                        " (Probability: " + effect.getProbability() +
                        ", Satisfying: " + effect.isSatisfying() + ")");
            }
        }
    }
}
