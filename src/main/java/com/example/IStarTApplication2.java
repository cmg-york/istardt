package com.example;

import com.example.objects.*;
import com.example.xml.IStarUnmarshaller2;
import jakarta.xml.bind.JAXBException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Main application entry point demonstrating the use of the iStar-T unmarshaller with validation.
 */
public class IStarTApplication2 {

    // File paths directly to resources
    private static final String XSD_SCHEMA_PATH = "src/main/resources/xsd/istar-rl-schema_v2.xsd";
    private static final String SCHEMATRON_SCHEMA_PATH = "src/main/resources/schematron/istar-rl-schematron2.sch";
    private static final String XML_FILE_PATH = "src/main/resources/xml/figure1a_fixed.xml";

    public static void main(String[] args) {
        try {
            // Get file objects for resources
            File xmlFile = new File(XML_FILE_PATH);
            File xsdFile = new File(XSD_SCHEMA_PATH);
            File schematronFile = new File(SCHEMATRON_SCHEMA_PATH);

            // Verify the files exist
            if (!xmlFile.exists()) {
                System.err.println("Error: XML file not found: " + XML_FILE_PATH);
                System.exit(1);
            }

            if (!xsdFile.exists()) {
                System.err.println("Error: XSD schema file not found: " + XSD_SCHEMA_PATH);
                System.exit(1);
            }

            if (!schematronFile.exists()) {
                System.err.println("Error: Schematron schema file not found: " + SCHEMATRON_SCHEMA_PATH);
                System.exit(1);
            }

            System.out.println("Processing XML file: " + xmlFile.getAbsolutePath());

            // Validate XML against XSD schema
            System.out.println("Validating XML against XSD schema...");
            try {
                XmlValidation.validate("xsd", xsdFile.getAbsolutePath(), xmlFile.getAbsolutePath());
            } catch (Exception e) {
                System.err.println("XSD validation failed:");
                System.err.println(e.getMessage());
                System.exit(1);
            }

            // Validate XML against Schematron schema
            System.out.println("Validating XML against Schematron schema...");
            try {
                XmlValidation.validate("schematron", schematronFile.getAbsolutePath(), xmlFile.getAbsolutePath());
            } catch (Exception e) {
                System.err.println("Schematron validation failed:");
                System.err.println(e.getMessage());
                // Optional: You could choose to continue despite Schematron validation failures
                // by commenting out the following line
                System.exit(1);
            }

            // Create unmarshaller
            System.out.println("Unmarshalling XML...");
            IStarUnmarshaller2 unmarshaller = new IStarUnmarshaller2();

            // Unmarshal XML to model
            Model model = unmarshaller.unmarshalToModel(xmlFile);

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
     * Print information about the model structure.
     *
     * @param model The model to print information about
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

                // Print detailed information about conditions
                printDetailedConditionInfo(model);

                // Print detailed information about IndirectEffects
                printDetailedIndirectEffectInfo(model);

                // Print detailed information about Effects
                printDetailedEffectInfo(model);

                // Print detailed information about Qualities in the Environment
                printDetailedQualityInfo(model);
            }
        } else {
            System.out.println("  No environment information available");
        }
    }

    /**
     * Print detailed information about conditions in the environment.
     */
    private static void printDetailedConditionInfo(Model model) {
        List<NonDecompositionElement> elements = model.getEnvironment().getNonDecompElements();
        if (elements == null) return;

        List<Condition> conditions = new ArrayList<>();
        for (NonDecompositionElement element : elements) {
            if (element instanceof Condition) {
                conditions.add((Condition) element);
            }
        }

        if (!conditions.isEmpty()) {
            System.out.println("\n  Conditions (PreBoxes):");
            for (Condition condition : conditions) {
                String titleText = condition.getAtom() != null && condition.getAtom().getTitleText() != null ?
                        " (" + condition.getAtom().getTitleText() + ")" : "";
                Formula formula = condition.getValueFormula();
                String formulaText = formula != null ? " [Formula: " + formula.getFormula() + "]" : "";
                System.out.println("    - " + condition.getId() + titleText + formulaText);
            }
        }
    }

    /**
     * Print detailed information about indirect effects in the environment.
     */
    private static void printDetailedIndirectEffectInfo(Model model) {
        List<NonDecompositionElement> elements = model.getEnvironment().getNonDecompElements();
        if (elements == null) return;

        List<IndirectEffect> indirectEffects = new ArrayList<>();
        for (NonDecompositionElement element : elements) {
            if (element instanceof IndirectEffect) {
                indirectEffects.add((IndirectEffect) element);
            }
        }

        if (!indirectEffects.isEmpty()) {
            System.out.println("\n  Indirect Effects:");
            for (IndirectEffect indirectEffect : indirectEffects) {
                String titleText = indirectEffect.getAtom() != null && indirectEffect.getAtom().getTitleText() != null ?
                        " (" + indirectEffect.getAtom().getTitleText() + ")" : "";
                Formula formula = indirectEffect.getValueFormula();
                String formulaText = formula != null ? " [Formula: " + formula.getFormula() + "]" : "";
                System.out.println("    - " + indirectEffect.getId() + titleText +
                        (indirectEffect.isExported() ? " [Exported]" : "") + formulaText);
            }
        }
    }

    /**
     * Print detailed information about effects in the environment.
     */
    private static void printDetailedEffectInfo(Model model) {
        List<NonDecompositionElement> elements = model.getEnvironment().getNonDecompElements();
        if (elements == null) return;

        List<Effect> effects = new ArrayList<>();
        for (NonDecompositionElement element : elements) {
            if (element instanceof Effect) {
                effects.add((Effect) element);
            }
        }

        if (!effects.isEmpty()) {
            System.out.println("\n  Effects:");
            for (Effect effect : effects) {
                String titleText = effect.getAtom() != null && effect.getAtom().getTitleText() != null ?
                        " (" + effect.getAtom().getTitleText() + ")" : "";
                System.out.println("    - " + effect.getId() + titleText +
                        " (Probability: " + effect.getProbability() +
                        ", Satisfying: " + effect.isSatisfying() + ")");
            }
        }
    }

    /**
     * Print detailed information about qualities in the environment.
     */
    private static void printDetailedQualityInfo(Model model) {
        List<NonDecompositionElement> elements = model.getEnvironment().getNonDecompElements();
        if (elements == null) return;

        List<Quality> qualities = new ArrayList<>();
        for (NonDecompositionElement element : elements) {
            if (element instanceof Quality) {
                qualities.add((Quality) element);
            }
        }

        if (!qualities.isEmpty()) {
            System.out.println("\n  Qualities in Environment:");
            for (Quality quality : qualities) {
                String titleText = quality.getAtom() != null && quality.getAtom().getTitleText() != null ?
                        " (" + quality.getAtom().getTitleText() + ")" : "";
                Formula formula = quality.getValueFormula();
                String formulaText = formula != null ? " [Formula: " + formula.getFormula() + "]" : "";
                System.out.println("    - " + quality.getId() + titleText +
                        (quality.isRoot() ? " [ROOT]" : "") + formulaText);
            }
        }
    }

    /**
     * Print information about a goal, with proper indentation.
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
     * Print information about a task, with proper indentation.
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