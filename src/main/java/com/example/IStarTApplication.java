package com.example;

import com.example.objects.*;

import com.example.xml.IStarUnmarshaller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Main application entry point demonstrating the use of the Jackson XML-based unmarshaller with validation.
 * This replaces the original JAXB-based implementation with a more maintainable Jackson XML approach.
 */
public class IStarTApplication {

    // File paths directly to resources
    private static final String XSD_SCHEMA_PATH = "src/main/resources/xsd/istar-rl-schema_v3.xsd";
    private static final String SCHEMATRON_SCHEMA_PATH = "src/main/resources/schematron/istar-rl-schematron2.sch";
    private static final String XML_FILE_PATH = "src/main/resources/xml/figure1a_fixed2.xml";

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
                System.exit(1);
            }

            // Create unmarshaller
            System.out.println("Unmarshalling XML...");
            IStarUnmarshaller unmarshaller = new IStarUnmarshaller();

            // Unmarshal XML to model
            Model model = unmarshaller.unmarshalToModel(xmlFile);

            // Display model information
            printModelInformation(model);

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
            System.out.println("\nActor " + actorCount + ": " + actor.getAtom().getTitleText());

            // Goals
            List<Goal> goals = actor.getGoals();
            System.out.println("  Goals (" + goals.size() + "):");
            for (Goal goal : goals) {
                printGoalInfo(goal, 2);
            }

            // Tasks
            List<Task> tasks = actor.getTasks();
            System.out.println("  Tasks (" + tasks.size() + "):");
            for (Task task : tasks) {
                printTaskInfo(task, 2);
            }

            // Qualities
            List<Quality> qualities = actor.getQualities();
            System.out.println("  Qualities (" + qualities.size() + "):");
            for (Quality quality : qualities) {
                String titleText = quality.getAtom() != null && quality.getAtom().getTitleText() != null ?
                        " (" + quality.getAtom().getTitleText() + ")" : "";
                String formulaText = quality.getFormula() != null ?
                        " [Formula: " + quality.getFormula().getFormula() + "]" : "";

                System.out.println("    - " + quality.getId() + titleText +
                        (quality.isRoot() ? " [ROOT]" : "") + formulaText);
            }
            actorCount++;
        }

        // Print environment information
        System.out.println("\nEnvironment Information:");
        if (model.getEnvironment() != null) {
            List<NonDecompositionElement> nonDecompElements = model.getEnvironment().getNonDecompElements();
            int elementsCount = nonDecompElements != null ? nonDecompElements.size() : 0;
            System.out.println("  Non-decomposition elements: " + elementsCount);

            if (nonDecompElements != null && !nonDecompElements.isEmpty()) {
                // Group elements by type
                List<Condition> conditions = new ArrayList<>();
                List<IndirectEffect> indirectEffects = new ArrayList<>();
                List<Quality> qualities = new ArrayList<>();
                List<Effect> effects = new ArrayList<>();

                for (NonDecompositionElement element : nonDecompElements) {
                    if (element instanceof Condition) {
                        conditions.add((Condition) element);
                    } else if (element instanceof IndirectEffect) {
                        indirectEffects.add((IndirectEffect) element);
                    } else if (element instanceof Quality) {
                        qualities.add((Quality) element);
                    } else if (element instanceof Effect) {
                        effects.add((Effect) element);
                    }
                }

                // Print Conditions (PreBoxes)
                if (!conditions.isEmpty()) {
                    System.out.println("\n  Conditions (PreBoxes): " + conditions.size());
                    for (Condition condition : conditions) {
                        String titleText = condition.getAtom() != null && condition.getAtom().getTitleText() != null ?
                                " (" + condition.getAtom().getTitleText() + ")" : "";
                        String formulaText = condition.getFormula() != null ?
                                " [Formula: " + condition.getFormula().getFormula() + "]" : "";

                        System.out.println("    - " + condition.getId() + titleText + formulaText);
                    }
                }

                // Print Indirect Effects
                if (!indirectEffects.isEmpty()) {
                    System.out.println("\n  Indirect Effects: " + indirectEffects.size());
                    for (IndirectEffect indirectEffect : indirectEffects) {
                        String titleText = indirectEffect.getAtom() != null && indirectEffect.getAtom().getTitleText() != null ?
                                " (" + indirectEffect.getAtom().getTitleText() + ")" : "";
                        String formulaText = indirectEffect.getFormula() != null ?
                                " [Formula: " + indirectEffect.getFormula().getFormula() + "]" : "";
                        String exportedText = indirectEffect.isExported() ? " [Exported]" : "";

                        System.out.println("    - " + indirectEffect.getId() + titleText + exportedText + formulaText);
                    }
                }

                // Print Qualities
                if (!qualities.isEmpty()) {
                    System.out.println("\n  Qualities: " + qualities.size());
                    for (Quality quality : qualities) {
                        String titleText = quality.getAtom() != null && quality.getAtom().getTitleText() != null ?
                                " (" + quality.getAtom().getTitleText() + ")" : "";
                        String formulaText = quality.getFormula() != null ?
                                " [Formula: " + quality.getFormula().getFormula() + "]" : "";
                        String rootText = quality.isRoot() ? " [ROOT]" : "";

                        System.out.println("    - " + quality.getId() + titleText + rootText + formulaText);
                    }
                }

                // Print Effects
                if (!effects.isEmpty()) {
                    System.out.println("\n  Effects: " + effects.size());
                    for (Effect effect : effects) {
                        String titleText = effect.getAtom() != null && effect.getAtom().getTitleText() != null ?
                                " (" + effect.getAtom().getTitleText() + ")" : "";

                        System.out.println("    - " + effect.getId() + titleText +
                                " (Probability: " + effect.getProbability() +
                                ", Satisfying: " + effect.isSatisfying() + ")");
                    }
                }
            }
        } else {
            System.out.println("  No environment information available");
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
                " [Type: " + goal.getDecompType() + "]" + (goal.isRoot() ? "[ROOT]" : ""));

        // Print pre formula if available
        Formula preFormula = goal.getPreFormula();
        if (preFormula != null) {
            System.out.println(indent + "  pre formula: " + preFormula.getFormula());
        } else {
            System.out.println(indent + "  pre formula: none");
        }

        // Print npr formula if available
        Formula nprFormula = goal.getNprFormula();
        if (nprFormula != null) {
            System.out.println(indent + "  npr formula: " + nprFormula.getFormula());
        } else {
            System.out.println(indent + "  npr formula: none");
        }

        // Print child elements if this is a decomposition element
        if (goal.getChildren() != null && !goal.getChildren().isEmpty()) {
            System.out.println(indent + "  Children:");
            for (DecompositionElement child : goal.getChildren()) {
                printDecompInfo(child, indentLevel + 2);
            }
        }
    }

    /**
     * Print information about a decomposition element.
     */
    private static void printDecompInfo(DecompositionElement decomp, int indentLevel) {
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < indentLevel; i++) {
            indent.append("  ");
        }

        // Print the current element's information
        System.out.println(indent + "- " + decomp.getId() +
                (decomp.getAtom() != null ? " (" + decomp.getAtom().getTitleText() + ")" : "") +
                " [Type: " + decomp.getDecompType() + "]");

        // Print pre formula if available
        Formula preFormula = decomp.getPreFormula();
        if (preFormula != null) {
            System.out.println(indent + "  pre formula: " + preFormula.getFormula());
        }

        // Print npr formula if available
        Formula nprFormula = decomp.getNprFormula();
        if (nprFormula != null) {
            System.out.println(indent + "  npr formula: " + nprFormula.getFormula());
        }

        if (decomp.getParent() != null && decomp.getParent().getAtom() != null) {
            System.out.println(indent + "  Parent: " + decomp.getParent().getAtom().getTitleText());
        } else {
            System.out.println(indent + "  Parent: none");
        }

        // Print children (but limit depth to avoid too much output)
        if (decomp.getChildren() != null && !decomp.getChildren().isEmpty() && indentLevel < 8) {
            System.out.println(indent + "  Children:");
            for (DecompositionElement child : decomp.getChildren()) {
                printDecompInfo(child, indentLevel + 2);
            }
        } else if (decomp.getChildren() != null && !decomp.getChildren().isEmpty()) {
            // If we've gone too deep, just print the number of children
            System.out.println(indent + "  Children: " + decomp.getChildren().size() + " elements (depth limit reached)");
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
                " [Deterministic: " + task.isDeterministic() + "]" + (task.isRoot()? "[ROOT]" : "") + " (Type: " + task.getDecompType()+")");

        // Print pre formula if available
        Formula preFormula = task.getPreFormula();
        if (preFormula != null) {
            System.out.println(indent + "  pre formula: " + preFormula.getFormula());
        } else {
            System.out.println(indent + "  pre formula: none");
        }

        // Print npr formula if available
        Formula nprFormula = task.getNprFormula();
        if (nprFormula != null) {
            System.out.println(indent + "  npr formula: " + nprFormula.getFormula());
        } else {
            System.out.println(indent + "  npr formula: none");
        }

        if (task.getParent() != null) {
            System.out.println(indent + "  Parent: " + task.getParent().getAtom().getTitleText());
        } else {
            System.out.println(indent + "  Parent: none");
        }

        // Print effects
        if (task.getEffects() != null && !task.getEffects().isEmpty()) {
            System.out.println(indent + "  Effects:");
            for (Effect effect : task.getEffects()) {
                System.out.println(indent + "    - " + effect.getId() + " (" + effect.getAtom().getTitleText() + ")" +
                        " (Probability: " + effect.getProbability() +
                        ", Satisfying: " + effect.isSatisfying() + ")");

                // Print effect pre formula if available
                Formula effectPreFormula = effect.getPreFormula();
                if (effectPreFormula != null) {
                    System.out.println(indent + "      pre formula: " + effectPreFormula.getFormula());
                }

                // Print effect npr formula if available
                Formula effectNprFormula = effect.getNprFormula();
                if (effectNprFormula != null) {
                    System.out.println(indent + "      npr formula: " + effectNprFormula.getFormula());
                }

                // Print turnsTrue and turnsFalse
                if (effect.getTurnsTrue() != null && !effect.getTurnsTrue().isEmpty()) {
                    System.out.println(indent + "      turns true: " + String.join(", ", effect.getTurnsTrue()));
                }

                if (effect.getTurnsFalse() != null && !effect.getTurnsFalse().isEmpty()) {
                    System.out.println(indent + "      turns false: " + String.join(", ", effect.getTurnsFalse()));
                }
            }
        }
    }
}