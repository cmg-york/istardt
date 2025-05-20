package ca.yorku.cmg.istardt.translators.dttranslator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import ca.yorku.cmg.istardt.xmlparser.objects.*;

public class DTTranslator {
	private Model model;
	private Formatter formatter;
	
	private String outputFile;
	
	private String modelName = "Spec";
	private String header = ""; 
	private String numRuns = "";
	
	private String crossRunSet = "transStateStructure([";
	private String initList = "init([";
	
	private String agentActionList = "agentActionList([";
	private String agentActions = "";
	private String stochasticActionList = "stochasticActionList([";
	private String stochasticActions = "";
	private String nonDetActions = "";
	private String fluentList = "fluentList([";
	private String discreteExportedSet = "discreteExportedSet([";
	
	
	private String procedures = "";
	private String probabilities = "";
	private String successorStateAxioms = "";
	private String preconditionAxioms = "";
	private String successorStateAxiomsPre = "";
	private String rewardFormulae = "";
	private String satisfactionFormulae = "";
	private String satisfactionFormulaePre = "";
	private String attemptFormulae = "";
	private String senseConditions = "";
	private String restoreSitArg = "";	

	public DTTranslator(Model m, String path) {
		this.model = m;
		this.formatter = new Formatter();
		// outputFile = path + m.getModelHeader().getNote();
		outputFile = path + "/" + this.modelName + ".pl";
	}

	public Actor getActor() {
		return model.getActors().get(0);
	}

	public void translate() {


		Actor a = getActor();
		
		/* 
		 *  Preliminary
		 */

		header = getHeader();
		
		/* 
		 *  Preliminary
		 */

		// Number of Runs
		numRuns = "getNumRuns(" + a.getRoot().getRuns() + ").\n\n";
		
		// TODO
		// Configure Reward style
		// Configure infeasible penalty
		
		// TODO
		// Handle the DISCRETE EXPORTED SET
		
		
		// TODO
		// Handle the CONTINUOUS EXPORTED SET
				
		// TODO
		// Handle the CROSS RUN SET
		
		/* 
		 * RULES: 
		 * 1. Cross run set can include any fluent, variable or quality. Nothing to check really.
		 * 2. If not discrete exported set is given, add all of the fluents.
		 */
		
		
		
		/* 
		 *  Process Tasks
		 */
		for (Task t:a.getTasks()) {
			String taskID = formatter.toCamelCase(t.getName());

			agentActionList += taskID + ",";
			agentActions += "agentAction(" + taskID + ").\n";
			nonDetActions += "nondetActions(" + taskID + ",_,[";
			
			if (isExported(t)) {
				discreteExportedSet += formatter.toSat(taskID) + ",";
			}
			
			String localSatFormula = "";
			String localPreFormula = "";
			String localAttFormula = "";

			for (Effect e: t.getEffects()) {
				String effectID = formatter.toCamelCase(e.getName());
				stochasticActionList += effectID + ",";
				stochasticActions += "stochasticAction(" + effectID + ").\n";
				nonDetActions += effectID + ",";
				probabilities += "prob(" + effectID + "," + e.getProbability() + ",_).\n";
				preconditionAxioms += "poss(" + effectID + ",S).\n";
				
				for(String pred : e.getTurnsTrue()) {
					fluentList += formatter.toFluent(pred) + ",";
					//fluentList += formatter.toPreFluent(pred) + ","; // PRE
					successorStateAxioms += formatter.toFluent(pred) + "(do(A,S)) :- " + 
							formatter.toFluent(pred) + "(S); A=" + effectID + ".\n";
					//successorStateAxiomsPre += formatter.toPreFluent(pred) + "(do(A,S)) :- " + 
					//		formatter.toFluent(pred) + "(S).\n"; //PRE
					senseConditions += "senseCondition(" + effectID + "," + formatter.toFluent(effectID) + ").\n";
					//senseConditions += "senseCondition(" + effectID + "," + formatter.toPreFluent(effectID) + ").\n";
					restoreSitArg += "restoreSitArg(" + formatter.toFluent(pred) + ",S," + formatter.toFluent(pred) + "(S)).\n";
					//restoreSitArg += "restoreSitArg(" + formatter.toPreFluent(effectID) + ",S," + formatter.toPreFluent(effectID) + "(S)).\n";
					if (isExported(t)) {
						discreteExportedSet += pred + "_fl,";
					}
					
					localAttFormula += formatter.toFluent(pred) + "(S);";
					
					if (e.isSatisfying()) {
						localSatFormula += formatter.toFluent(pred) + "(S);";
						localPreFormula += formatter.toPreFluent(pred) + "(S);";
					}
				}
				

				
				if (e.getFormula() != null) {
					System.out.println("HELLLO:" + t.getPreFormula().toString());
				}

			} // Next effect
			localSatFormula = formatter.toSat(taskID) + "(S) :- " +  localSatFormula.substring(0, localSatFormula.length() - 1) + ".\n";
			localPreFormula = formatter.toPreFluent(taskID) + "(S) :- " +  localPreFormula.substring(0, localPreFormula.length() - 1) + ".\n";
			localAttFormula = formatter.toAtt(taskID) + "(S) :- " +  localAttFormula.substring(0, localAttFormula.length() - 1) + ".\n";

			restoreSitArg += "restoreSitArg(" + formatter.toSat(taskID) + ",S," + formatter.toSat(taskID) + "(S)).\n";
			//restoreSitArg += "restoreSitArg(" + formatter.toPreFluent(taskID) + ",S," + formatter.toPreFluent(taskID) + "(S)).\n";
			restoreSitArg += "restoreSitArg(" + formatter.toAtt(taskID) + ",S," + formatter.toAtt(taskID) + "(S)).\n";
			
			nonDetActions = nonDetActions.substring(0, nonDetActions.length() - 1) + "]).\n";
			
			satisfactionFormulae += localSatFormula;
			satisfactionFormulaePre += localPreFormula;
			attemptFormulae += localAttFormula;

			if (t.getPreFormula() != null) {
				System.out.println(parseBooleanFormula(t.getPreFormula()));
			}

			// TODO
			// 1. Preconditions
			// 2. maps

		}

		// Task post-processing
		agentActionList = agentActionList.substring(0, agentActionList.length() - 1) + "]).\n";
		stochasticActionList = stochasticActionList.substring(0, stochasticActionList.length() - 1) + "]).\n";
		fluentList = fluentList.substring(0, fluentList.length() - 1) + "]).\n";

		
		/* 
		 *  Process Goals
		 */
		for (Goal g:a.getGoals()) {
			String procOp = "";
			String formOp = "";
			String goalID =  formatter.toCamelCase(g.getName());
			
			if (isExported(g)) {
				discreteExportedSet += formatter.toSat(goalID) + ",";
			}
			
			if (g.getDecompType() == DecompType.AND) { // AND Decomposed
				procOp = " : ";
				formOp = ",";
			} else if (g.getDecompType() == DecompType.OR) { //OR Decomposed
				procOp = " # ";
				formOp = ";";
			}
			
			String procLine = "proc(" + goalID + ",";
			String localSatFormula = goalID + "_Sat(S) :- ";
			String localAttFormula = goalID + "_Att(S) :- ";
			String localPreFormula = goalID + "_Pre(S) :- ";
			for (DecompositionElement l: g.getChildren()) {
				String childID = formatter.toCamelCase(l.getName());
				procLine += childID + procOp;
				localSatFormula += childID + "_Sat(S)" + formOp;
				localPreFormula += childID + "_Pre(S)" + formOp;
				localAttFormula += childID + "_Sat(S)" + ";";
			}
			localSatFormula = localSatFormula.substring(0, localSatFormula.length() - 1) + ".\n";
			localAttFormula = localAttFormula.substring(0, localAttFormula.length() - 1) + ".\n";
			localPreFormula = localPreFormula.substring(0, localPreFormula.length() - 1) + ".\n";
			procLine = procLine.substring(0, procLine.length() - 3) + ").\n";

			restoreSitArg += "restoreSitArg(" + formatter.toSat(goalID) + ",S," + formatter.toSat(goalID) + "(S)).\n";
			//restoreSitArg += "restoreSitArg(" + formatter.toPreFluent(taskID) + ",S," + formatter.toPreFluent(taskID) + "(S)).\n";
			restoreSitArg += "restoreSitArg(" + formatter.toAtt(goalID) + ",S," + formatter.toAtt(goalID) + "(S)).\n";
			
			satisfactionFormulae += localSatFormula;
			attemptFormulae += localAttFormula;
			satisfactionFormulaePre += localPreFormula;
			procedures += procLine;
		}
		
		
		/* 
		 *  Process Qualities
		 */
		String rewardTotal = "";
		for (Quality o:a.getQualities()) {
			rewardFormulae += "reward_" + o.getName() + "(V,S) :- V = 10.\n";
			if (o.isRoot()) {
				rewardTotal = "rewardInst(R,S) :- reward_" + o.getName() + "(R,S).\n";
			}
		}
		rewardFormulae += "\n" + rewardTotal + "\n";

		
		discreteExportedSet = discreteExportedSet.substring(0, discreteExportedSet.length() - 1) + "]).\n";
		
		
		printToFile(getSpecFromVars());
		

	}

	
	/**
	 * Reads all the initialization tags and gets the init([]) clause
	 * @return 
	 * TODO
	 */
	private String getInitials() {
		return "";
	}

	/**
	 * Reads the continuous export tags and creates the ccStateShapeInfo([]) clause
	 * @return 
	 * TODO
	 */
	private String getShapeInfo() {
		return "";
	}

	/**
	 * Reads the continuous export tags and creates the ccStateShapeInfo([]) clause
	 * @return 
	 * TODO
	 */
	private String getCrossState() {
		return "";
	}
	
	
	
	private boolean isExported(Element t) {
		List<String> list = new ArrayList<>();
		list.add("deliveredInTimeDom");
		list.add("deliveredInTimeFrgn");
		list.add("deliveredGoodQualityInH");
		list.add("sourceDomestically");
		list.add("materialOrdered");
		//System.out.println("Checking: " + formatter.toCamelCase(t.getId()));
		return list.contains(formatter.toCamelCase(t.getName()));
	}

	
	private String	getCurrentTime(){
	    LocalDateTime now = LocalDateTime.now();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	    return(now.format(formatter));
	}

	private String getSpecFromVars() {
		return (
		
		"% DT-Golog Specification for Model: " + modelName + " \n" +
		"% Date Translated: " + getCurrentTime() + " \n" +
		"% From source: " + modelName + " \n" +
		"% Using DTTRanslate \n" +
		header + "\n\n" +
		
		"%\n% OPTIONS \n% \n\n" +
		numRuns + "\n\n" +
		
		"\n\n%\n% CROSS-RUN ELEMENTS \n% \n\n" +
		crossRunSet + "\n\n" +
		
		"\n\n%\n% EXPORTED STATE ELEMENTS \n% \n\n" +
		discreteExportedSet + "\n\n" +
		
		"\n\n%\n% ACTION LISTS \n% \n\n" +
		agentActionList + "\n" +
		agentActions + "\n" +
		stochasticActionList + "\n" +
		stochasticActions + "\n" +
		nonDetActions + "\n" +
		probabilities + "\n" +

		"\n\n%\n% PROCEDURES \n% \n\n" +
		procedures + "\n" +
		
		"\n\n%\n% FLUENT LISTS \n% \n\n" +
		fluentList + "\n" +
		
		"%\n% SUCCESSOR STATE AXIOMS \n% \n\n" +
		successorStateAxioms + "\n" +

		"%\n% PRECONDITION AXIOMS \n% \n\n" +
		preconditionAxioms + "\n" +
		
		"%\n% SATISFACTION FORMULAE \n% \n\n" +
		satisfactionFormulae + "\n" +
		
		"%\n% ATTEMPT FORMULAE \n% \n\n" +
		attemptFormulae + "\n" +
		
		"%\n% REWARD FORMULAE \n% \n\n" +
		rewardFormulae + "\n" +
		
		"%\n% SENSE CONDITIONS \n% \n\n" +
		senseConditions + "\n" +
		
		"%\n% RESTORE SITUATION ARGUMENT \n% \n\n" +
		restoreSitArg + "\n");
	}
	

	
	private String getHeader() {
		return (":-consult(\"DT-Golog-Iface.pl\").\n"
				+ ":-style_check(-discontiguous).\n"
				+ ":-style_check(-singleton).\n"
				+ ":- multifile getRewardMode/1.\n"
				+ ":- multifile getRewardModeDTG/1.\n"
				+ ":- multifile penalizeDeadlock/1.\n"
				+ ":- multifile deadlockPenalty/1.\n"
				+ ":- multifile getInfeasiblePenalty/1.\n"
				+ ":-dynamic(init/1).\n\n");
	}

	
	private void printToFile(String s) {
		
        File outFile = new File(this.outputFile);

        // Ensure the "output" directory exists
        File parentDir = outFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outFile))) {
	    	writer.write(s);
	        System.out.println("Written to file successfully.");
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	

	
	private String parseBooleanFormula(Formula f) {
		if (f instanceof Atom) {
		    return f.getFormula() + "(S)";
		} else if (f instanceof ANDOperator) {
		    return (parseBooleanFormula(((ANDOperator) f).getLeft())
		    		+ ", " + 
		    		parseBooleanFormula(((ANDOperator) f).getRight())
		    		);
		} else if (f instanceof OROperator) {
		    return (parseBooleanFormula(((OROperator) f).getLeft())
		    		+ "; " + 
		    		parseBooleanFormula(((OROperator) f).getRight())
		    		);
		} else if (f instanceof NOTOperator) {
			return (parseBooleanFormula(((NOTOperator) f).getLeft()));
		} else if (f instanceof GTOperator) {
		    return (parseBooleanFormula(((GTOperator) f).getLeft())
		    		+ ">" + 
		    		parseBooleanFormula(((GTOperator) f).getRight())
		    		); 
		} else {
			return f.getFormula();
		}
	}
	
}
