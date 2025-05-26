package ca.yorku.cmg.istardt.xmlparser;

import ca.yorku.cmg.istardt.translators.dttranslator.DTTranslator;
import ca.yorku.cmg.istardt.xmlparser.objects.*;
import ca.yorku.cmg.istardt.xmlparser.xml.IStarUnmarshaller;

import java.io.File;
import java.util.List;

/**
 * Main application entry point demonstrating the use of the Jackson XML-based unmarshaller with validation.
 */
public class dtx2dtg {

    private static final String XSD_SCHEMA_PATH = "src/main/resources/xsd/istar-rl-schema_v4.xsd";
    private static final String SCHEMATRON_SCHEMA_PATH = "src/main/resources/schematron/istar-rl-schematron4.sch";
    private static final String XML_FILE_PATH = "src/main/resources/xml/Order.istardt";
    private static final String OUTPUT_FILE_PATH = "F:/Dropbox/Private/Others/Software/gReason-2025/Nina/dtg2sim-new/src";

	static String inputFile = "";
	static String outputFile ="";
	static boolean printUsage = false;
    
	
	private static String printUsage() {
		String s = "";
		s = "Usage: dtx2dtg [-options]\n" + 
				"where options are:\n" + 
				"    -f filename \t iStarDT-X XML file \n" + 
				"    -o filename \t DT-Golog PL file \n" + 
				"    -h \t\t\t prints this help \n";
		return(s);
	}
	
	private static void processArgs(String[] args) throws Exception {
	       for (int i = 0; i < args.length; i++) {
	            String arg = args[i];
	            if (arg.startsWith("-")) {
	                char option = arg.charAt(1);
	                switch (option) {
		                case 'f':
		                    if (i + 1 < args.length) {
		                        inputFile = args[i + 1];
		                        i++;
		                    } else {
		                    	printUsage = true;
			                	throw new Exception("Option -f requires a file name.");
		                    }
		                	break;
		            	case 'o':
		                    if (i + 1 < args.length) {
		                        outputFile = args[i + 1];
		                        i++;
		                    } else {
		                    	printUsage = true;
			                	throw new Exception("Option -o requires a file name.");
		                    }
		                    break;
		            	case 'h':
		                    System.out.println(printUsage());
		                    System.exit(0);
		                default:
	                    	printUsage = true;
		                	throw new Exception("Unknown option: " + option);
	                }
	            } else {
	            	printUsage = true;
	                throw new Exception("Invalid argument: " + arg);
	            }
	        }
	       
	       if (args.length == 0) {
	       		printUsage = true;
	       		throw new Exception("No arguments provided. Use -h for help.");
	       }

	       if (inputFile.isEmpty()) {
	    	   printUsage = true;
	    	   throw new Exception("No input file specified. Use -f to specify input file.");
	       }
	       
	       File inpF = new File(inputFile);
	       if (!inpF.exists()) {
	           throw new Exception("Input file does not exist: " + inputFile);
	       }
		}
		
		

    
    
    public static void main(String[] args) {
    	
    	
        try {
        	processArgs(args);
        	File xmlFile = new File(inputFile);
            File xsdFile = new File(XSD_SCHEMA_PATH);
            File schematronFile = new File(SCHEMATRON_SCHEMA_PATH);
            
            if (!xsdFile.exists()) {
                System.err.println("Error: XSD schema file not found: " + XSD_SCHEMA_PATH);
                System.exit(1);
            }

            if (!schematronFile.exists()) {
                System.err.println("Error: Schematron schema file not found: " + SCHEMATRON_SCHEMA_PATH);
                System.exit(1);
            }


//            // Validate XML against XSD schema
//            System.out.println("Validating XML against XSD schema...");
//            try {
//                XmlValidation.validate("xsd", xsdFile.getAbsolutePath(), xmlFile.getAbsolutePath());
//            } catch (Exception e) {
//                System.err.println("XSD validation failed:");
//                System.err.println(e.getMessage());
//                System.exit(1);
//            }
//
//            // Validate XML against Schematron schema
//            System.out.println("Validating XML against Schematron schema...");
//            try {
//                XmlValidation.validate("schematron", schematronFile.getAbsolutePath(), xmlFile.getAbsolutePath());
//            } catch (Exception e) {
//                System.err.println("Schematron validation failed:");
//                System.err.println(e.getMessage());
//                System.exit(1);
//            }


            // Create unmarshaller
            System.out.println("Unmarshalling XML...");
            IStarUnmarshaller unmarshaller = new IStarUnmarshaller();

            // Unmarshal XML to model
            Model model = unmarshaller.unmarshalToModel(xmlFile);

            // Display model information
            //printModelInformation(model);

            /**
             * SOTIRIOS added tests
             */
            //DTTranslator trans = new DTTranslator(model,outputFile);
            //trans.exportedSetTest();
            //trans.initializationTest();
            //trans.crossRunTest();
            //trans.conditionExpressionTest();
            //trans.translate();


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
        System.out.println("\n=== iStar-DT-X Model Information ===");
        System.out.println("Number of actors: " + model.getActors().size());

        // Process each actor
        int actorCount = 1;
        for (Actor actor : model.getActors()) {
            String titleText = actor.getAtom() != null ? actor.getAtom().getTitleText() : "Unknown";
            String description = actor.getAtom() != null && actor.getAtom().getDescription() != null ?
                    "\n  Description: " + actor.getAtom().getDescription() : "";

            System.out.println("\nActor " + actorCount + ": " + titleText + description);

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
                String qualityTitleText = quality.getAtom() != null && quality.getAtom().getTitleText() != null ?
                        " (" + quality.getAtom().getTitleText() + ")" : "";
                String qualityDescription = quality.getAtom() != null && quality.getAtom().getDescription() != null ?
                        "\n      Description: " + quality.getAtom().getDescription() : "";
                String formulaText = quality.getFormula() != null ?
                        " [Formula: " + quality.getFormula().getFormula() + "]" : "";

                System.out.println("    - " + quality.getId() + qualityTitleText +
                        (quality.isRoot() ? " [ROOT]" : "") + formulaText + qualityDescription);
            }


                List<Condition> conditions = actor.getConditions();
                List<Quality> qualitiesNonDecomp = actor.getQualities();
                List<Effect> effects = actor.getEffects();

                // Print Conditions (PreBoxes)
                if (!conditions.isEmpty()) {
                    System.out.println("\n  Conditions (PreBoxes): " + conditions.size());
                    for (Condition condition : conditions) {
                        String conditionTitleText = condition.getAtom() != null && condition.getAtom().getTitleText() != null ?
                                " (" + condition.getAtom().getTitleText() + ")" : "";
                        String conditionDescription = condition.getAtom() != null && condition.getAtom().getDescription() != null ?
                                "\n      Description: " + condition.getAtom().getDescription() : "";
                        String formulaText = condition.getFormula() != null ?
                                " [Formula: " + condition.getFormula().getFormula() + "]" : "";

                        System.out.println("    - " + condition.getId() + conditionTitleText + formulaText + conditionDescription);
                    }
                }

                // Print Qualities
                if (!qualitiesNonDecomp.isEmpty()) {
                    System.out.println("\n  Qualities: " + qualitiesNonDecomp.size());
                    for (Quality quality : qualitiesNonDecomp) {
                        String qualityTitleText = quality.getAtom() != null && quality.getAtom().getTitleText() != null ?
                                " (" + quality.getAtom().getTitleText() + ")" : "";
                        String qualityDescription = quality.getAtom() != null && quality.getAtom().getDescription() != null ?
                                "\n      Description: " + quality.getAtom().getDescription() : "";
                        String formulaText = quality.getFormula() != null ?
                                " [Formula: " + quality.getFormula().getFormula() + "]" : "";
                        String rootText = quality.isRoot() ? " [ROOT]" : "";

                        System.out.println("    - " + quality.getId() + qualityTitleText + rootText + formulaText + qualityDescription);
                    }
                }

                // Print Effects
                if (!effects.isEmpty()) {
                    System.out.println("\n  Effects: " + effects.size());
                    for (Effect effect : effects) {
                        String effectTitleText = effect.getAtom() != null && effect.getAtom().getTitleText() != null ?
                                " (" + effect.getAtom().getTitleText() + ")" : "";
                        String effectDescription = effect.getAtom() != null && effect.getAtom().getDescription() != null ?
                                "\n      Description: " + effect.getAtom().getDescription() : "";

                        System.out.println("    - " + effect.getId() + effectTitleText +
                                " (Probability: " + effect.getProbability() +
                                ", Satisfying: " + effect.isSatisfying() + ")" + effectDescription);
                    }
                }
            }

            actorCount++;
    }

    /**
     * Print information about a goal.
     */
    private static void printGoalInfo(Goal goal, int indentLevel) {
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < indentLevel; i++) {
            indent.append("  ");
        }

        String titleText = goal.getAtom() != null && goal.getAtom().getTitleText() != null ?
                " (" + goal.getAtom().getTitleText() + ")" : "";
        String description = goal.getAtom() != null && goal.getAtom().getDescription() != null ?
                "\n" + indent + "  Description: " + goal.getAtom().getDescription() : "";

        System.out.println(indent + "- " + goal.getId() + titleText +
                " [Type: " + goal.getDecompType() + "]" + (goal.isRoot() ? "[ROOT]" : "") + description);

        System.out.println(indent + "  runs: " + goal.getRuns());

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
        String titleText = decomp.getAtom() != null ? " (" + decomp.getAtom().getTitleText() + ")" : "";
        String description = decomp.getAtom() != null && decomp.getAtom().getDescription() != null ?
                "\n" + indent + "  Description: " + decomp.getAtom().getDescription() : "";

        System.out.println(indent + "- " + decomp.getId() + titleText +
                " [Type: " + decomp.getDecompType() + "]" + description);

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
     * Print information about a task.
     */
    private static void printTaskInfo(Task task, int indentLevel) {
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < indentLevel; i++) {
            indent.append("  ");
        }

        String titleText = task.getAtom() != null && task.getAtom().getTitleText() != null ?
                " (" + task.getAtom().getTitleText() + ")" : "";
        String description = task.getAtom() != null && task.getAtom().getDescription() != null ?
                "\n" + indent + "  Description: " + task.getAtom().getDescription() : "";

        System.out.println(indent + "- " + task.getId() + titleText +
                " [Deterministic: " + task.isDeterministic() + "]" + (task.isRoot()? "[ROOT]" : "") +
                " (Type: " + task.getDecompType()+")" + description);

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
                String effectTitleText = effect.getAtom() != null && effect.getAtom().getTitleText() != null ?
                        " (" + effect.getAtom().getTitleText() + ")" : "";
                String effectDescription = effect.getAtom() != null && effect.getAtom().getDescription() != null ?
                        "\n" + indent + "      Description: " + effect.getAtom().getDescription() : "";

                System.out.println(indent + "    - " + effect.getId() + effectTitleText +
                        " (Probability: " + effect.getProbability() +
                        ", Satisfying: " + effect.isSatisfying() + ")" + effectDescription);

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