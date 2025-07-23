package ca.yorku.cmg.istardt.translators.dtx2dtg;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import ca.yorku.cmg.istardt.xmlparser.objects.*;

public class com2dtg {
	
	private Model model;
	private Formatter formatter;
	private FormulaParser parser;
	
	private String outputFile;
	
	private String modelName = "Spec";
	private String header = ""; 
	private String numRuns = "";
	private String problemType = "";
	
	private String crossRunSet = "transStateStructure([";

	private String initList = "init([";
	
	private String agentActionList = "agentActionList([";
	private String agentActions = "";
	private String stochasticActionList = "stochasticActionList([";
	private String stochasticActions = "";
	private String nonDetActions = "";
	private String fluentList = "fluentList([";

	private String discreteExportedSet = "discreteExportedSet([";
	private String continuousExportedSet = "continuousExportedSet([";
	
	private String procedures = "";
	private String probabilities = "";
	private String successorStateAxioms = "";
	private String preconditionAxioms = "";
	private String preconditionAxiomsTasks = "";
//	private String successorStateAxiomsPre = "";
	private String rewardFormulae = "";
	private String satisfactionFormulae = "";
	private String satisfactionFormulae_Effects = "";
	private String rootSat = "";
//	private String satisfactionFormulaePre = "";
	private String attemptFormulae = "";
	private String senseConditions = "";
	private String restoreSitArg = "";	

	public com2dtg(Model m, String path) {
		this.model = m;
		this.formatter = new Formatter();
		this.parser = new FormulaParser();
		outputFile = path;
	}

	public Actor getActor() {
		return model.getActors().get(0);
	}
	
	
	public Boolean isContinuous(ExportedSet exp) {
		Boolean contProblem = false;
		if (exp != null) { //if exp is null default is discrete
			for (Export rt: exp.getExports()) {
				String descr = rt.getElement().getName(); //CAUTION: maybe this is getRef
				if ((rt.getElement() instanceof Quality) || (rt.getElement() instanceof Variable))  {
					contProblem = true;
				}
			}
		}
		return(contProblem);
	}
	
	public String getDiscreteExportedSet(ExportedSet exp) {
		String discreteExport = "%\n% D I S C R E T E   E X P O R T S\n%\n";
		discreteExport += "discreteExportedSet([";
		Boolean empty = true;
		if (exp != null) {
			for (Export rt: exp.getExports()) {
				String descr = rt.getElement().getName(); //CAUTION: maybe this is getRef
				if ((rt.getElement() instanceof Goal) || (rt.getElement() instanceof Task))  {
					descr += "_Sat";
					discreteExport += descr + ",";
					empty = false;
				} else if (rt.getElement() instanceof Predicate) {
					descr += "_fl";
					discreteExport += descr + ",";
					empty = false;
				}
			}
		}
		
		if (!empty) {
			discreteExport = discreteExport.substring(0, discreteExport.length() - 1) + "]).\n";
		} else {
			discreteExport += "]).\n";
			//discreteExport = "discreteExportedSet(X) :- fluentList(X),!.\n";
		}
		
		return(discreteExport);
	}
	
	public String getContinuousExportedSet(ExportedSet exp) {
		
		String continuousExport = "%\n% C O N T I N U O U S   E X P O R T S\n%\n";
		continuousExport += "continuousExportedSet([";
		Boolean empty = true;
		
		if (exp != null) {
			for (Export rt: exp.getExports()) {
				String descr = rt.getElement().getName(); //CAUTION: maybe this is getRef
				if ((rt.getElement() instanceof Quality) || (rt.getElement() instanceof Variable))  {
					descr += "(_)," + rt.getMinVal() + "," + rt.getMaxVal();
					continuousExport += "[" + descr + "], ";
					empty = false;
				}
			}
		}
		
		if (!empty) {
			continuousExport = continuousExport.substring(0, continuousExport.length() - 2) + "]).\n";
		} else {
			continuousExport += "]).\n";
		}
		return(continuousExport);
	}

	public String getCrossRunState(CrossRunSet crs) {
		String crossState = "%\n% C R O S S   S T A T E \n%\n";
		crossState += "transStateStructure([";
		for (Element cr: crs.getElements()) {
			String descr = cr.getName(); //CAUTION: maybe this is getRef
			if ((cr instanceof Predicate))  {
				crossState += descr + "_fl,";
			} else if ((cr instanceof Variable) || (cr instanceof Quality)) {
				crossState += descr + "(_),";
			} else {
				System.err.println("ERROR: initialization should be either a predicate, variable, or quality");
			}
		}
		crossState  = crossState .substring(0, crossState .length() - 1) + "]).\n";
	
		return(crossState);
	}
	
	public String getInitializations (InitializationSet init) {
		String initLine = "init([";
		
		for (Initialization it: init.getInitializations()) {
			String descr = it.getRef(); //CAUTION: maybe this is getRef

			if ((it.getElement() instanceof Predicate))  {
				initLine += descr + "_fl,";
			} else if (it.getElement() instanceof Variable) {
				initLine += descr + "_fl(" + it.getValue() +  "),";
			} else if (it.getElement() instanceof Quality) {
				initLine += descr + "(" + it.getValue() +  "),";
			} else {
				System.err.println("ERROR: initialization should be a predicate, a variable or a quality");
			}
		}
		initLine = initLine.substring(0, initLine.length() - 1) + "]).\n";
		return (initLine);
	}
	
	
	private ArrayList<DecompositionElement> getORSiblings(DecompositionElement e, ArrayList<DecompositionElement> l) {
		if (e.isRoot()) {
			return(l);
		}
		
		DecompositionElement parent = e.getParent();

		if (parent.getDecompType() == DecompType.OR) {
			for (DecompositionElement ch : e.getSiblings()) {
				l.add(ch);
			}
		}
		return getORSiblings(parent,l);
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
		numRuns = "getNumRuns(" + a.getGoalRoot().getRuns() + ").";
		// Problem type
		problemType = isContinuous(a.getExportedSet()) ? "getObsType(continuous).\n" : "getObsType(discrete).\n";  
		 
		
		// TODO
		// Configure Reward style
		// Configure infeasible penalty
		
		// DISCRETE EXPORTED SET
		this.discreteExportedSet = getDiscreteExportedSet(a.getExportedSet());
		
		// CONTINUOUS EXPORTED SET
		this.continuousExportedSet = getContinuousExportedSet(a.getExportedSet());

		// CROSS RUN SET
		this.crossRunSet = getCrossRunState(a.getCrossRunSet());

		// INITIALIZATIONS
		this.initList = getInitializations(a.getInitializationSet());

		
		/* 
		 *  Process Tasks
		 */
		
		for (Task t:a.getTasks()) {
			String taskID = formatter.toCamelCase(t.getName());
			String taskPrecond = "";
			
			agentActionList += taskID + ",";
			agentActions += "agentAction(" + taskID + ").\n";
			nonDetActions += "nondetActions(" + taskID + ",_,[";
			
			String localSatFormula = "";
			String localPreFormula = "";
			String localAttFormula = "";
			
			for (Effect e: t.getEffects()) {
				String effectID = e.getName(); //formatter.toCamelCase(e.getName());
				ArrayList<String> localPreconditions = new ArrayList<String>();
				
				taskPrecond += "poss(" + effectID + ",S);";
				
				stochasticActionList += effectID + ",";
				stochasticActions += "stochasticAction(" + effectID + ").\n";
				nonDetActions += effectID + ",";
				probabilities += "prob(" + effectID + "," + e.getProbability() + ",_).\n";
				
				String effectSatFormula = effectID + "_Sat(S) :- ";

				for(String pred : e.getTurnsTrue()) {
					fluentList += formatter.toFluent(pred) + ",";
					successorStateAxioms += formatter.toFluent(pred) + "(do(A,S)) :- " + 
							formatter.toFluent(pred) + "(S); A=" + effectID + ".\n";
					senseConditions += "senseCondition(" + effectID + "," + effectID + "_Occured).\n";
					restoreSitArg += "restoreSitArg(" + formatter.toFluent(pred) + ",S," + formatter.toFluent(pred) + "(S)).\n";
					
					effectSatFormula += formatter.toFluent(pred) + "(S),";
					localAttFormula += formatter.toFluent(pred) + "(S);";
					
					if (e.isSatisfying()) {
						localSatFormula += formatter.toFluent(pred) + "(S);";
						localPreFormula += formatter.toPreFluent(pred) + "(S);";
					}
					
					
				} //Next predicate
				
				// TODO: Process getTurnsFalse 
				
				satisfactionFormulae_Effects += formatter.trimTrailingCharacter(effectSatFormula) + ".\n";  
				
				
				//
				// EFECT PRECONDITIONS
				//
				
				//Parent task
				localPreconditions.add("\\+ " + taskID + "_Att(S)");

				//
				// Preconditions due to mutual XOR
				//
				for (DecompositionElement de: getORSiblings(t, new ArrayList<DecompositionElement>())) {
					localPreconditions.add("\\+ " + de.getName() + "_Att(S)");
				}
				
				//
				// TODO: Preconditions due to PRE LINKS
				//

				//
				// TODO: Preconditions due to NPR LINKS
				//

				//
				// Precondition Rendering
				//
				preconditionAxioms += "poss(" + effectID + ",S) :- ";
				
				for (String prec:localPreconditions) {
					preconditionAxioms += prec + ","; 
				}
				preconditionAxioms = preconditionAxioms.substring(0, preconditionAxioms.length() - 1) + ".\n";
				

			} // Next effect

			localSatFormula = formatter.toSat(taskID) + "(S) :- " +  localSatFormula.substring(0, localSatFormula.length() - 1) + ".\n";
			localPreFormula = formatter.toPreFluent(taskID) + "(S) :- " +  localPreFormula.substring(0, localPreFormula.length() - 1) + ".\n";
			localAttFormula = formatter.toAtt(taskID) + "(S) :- " +  localAttFormula.substring(0, localAttFormula.length() - 1) + ".\n";

			if (!taskPrecond.equals("")) {
				taskPrecond = taskPrecond.substring(0, taskPrecond.length() - 1);
				taskPrecond = "poss(" + taskID + ",S) :- (" + taskPrecond + ").\n";
			} else {
				taskPrecond = "poss(" + taskID + ",S).\n";
			}
			preconditionAxiomsTasks += taskPrecond; 
			
			restoreSitArg += "restoreSitArg(" + formatter.toSat(taskID) + ",S," + formatter.toSat(taskID) + "(S)).\n";
			//restoreSitArg += "restoreSitArg(" + formatter.toPreFluent(taskID) + ",S," + formatter.toPreFluent(taskID) + "(S)).\n";
			restoreSitArg += "restoreSitArg(" + formatter.toAtt(taskID) + ",S," + formatter.toAtt(taskID) + "(S)).\n";
			
			nonDetActions = nonDetActions.substring(0, nonDetActions.length() - 1) + "]).\n";
			
			satisfactionFormulae += localSatFormula;
			//satisfactionFormulaePre += localPreFormula;
			attemptFormulae += localAttFormula;

			if (t.getPreFormula() != null) {
				//System.out.println(parseBooleanFormula(t.getPreFormula()));
			}

		} // Task

		// Task post-processing
		agentActionList = agentActionList.substring(0, agentActionList.length() - 1) + "]).\n";
		stochasticActionList = stochasticActionList.substring(0, stochasticActionList.length() - 1) + "]).\n";
		
		
		/* 
		 *  Process Goals
		 */
		for (Goal g:a.getGoals()) {
			String procOp = "";
			String formOp = "";
			String goalID =  formatter.toCamelCase(g.getName());
			
		
			if (g.getDecompType() == DecompType.AND) { // AND Decomposed
				procOp = " : ";
				formOp = ",";
			} else if (g.getDecompType() == DecompType.OR) { //OR Decomposed
				procOp = " # ";
				formOp = ";";
			}
			
			String procLine = "proc(" + goalID + ", ";
			String localSatFormula = goalID + "_Sat(S) :- ";
			String localAttFormula = goalID + "_Att(S) :- ";
			String localPreFormula = goalID + "_Pre(S) :- ";
			for (DecompositionElement l: g.getChildren()) {
				String childID = formatter.toCamelCase(l.getName());
				procLine += childID + procOp;
				localSatFormula += childID + "_Sat(S)" + formOp;
				localPreFormula += childID + "_Pre(S)" + formOp;
				localAttFormula += childID + "_Att(S)" + ";";
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
			//satisfactionFormulaePre += localPreFormula;
			procedures += procLine;
		}

		rootSat = "goalAchieved(S) :- " + a.getGoalRoot().getName() + "_Sat(S).\n";
		procedures += "dtgRun :- write('Policy: '), bp(" + a.getGoalRoot().getName() + ",10,_,U,P,x),nl,"
				+ "\n        write('Utility: '),writeln(U), "
				+ "\n        write('Probability: '),writeln(P).";
		
		/* 
		 *  Process Qualities
		 */
		String rewardTotal = "";
		for (Quality o:a.getQualities()) {

			String header =
					o.getName() + "(V_init,s0) :- getInitValue(" + o.getName() + ",V_init),!.\n";
			String indent = " ".repeat((o.getName() + "(0,s0) :- ").length());

			
			//addInit if PREVIOUS (o)
			//isOperantOfPrevious(Element e, Formula f)
			//isInCrossRun(Element e)
			
			boolean addInit = !parser.isOperantOfPrevious(o, o.getFormula()) && isInCrossRun(o,a.getCrossRunSet());

			if (addInit) {
				String part1 = o.getName() + "(V,S) :-" + o.getName() + "(R_" + o.getName() + "_init,s0),\n" +
						parser.parseSimpleQualityExpressionPart1(o.getFormula(),indent);
				String part2 = indent + "V is R_" + o.getName() + "_init +\n" + parser.parseSimpleQualityExpressionPart2(o.getFormula(),indent) + ".\n";
				rewardFormulae +=  header + part1 + part2 + "\n\n";
			} else {
				String part1 = o.getName() + "(V,S) :- \n" + parser.parseSimpleQualityExpressionPart1(o.getFormula(),indent);
				String part2 = indent + "V is \n" + parser.parseSimpleQualityExpressionPart2(o.getFormula(),indent) + ".\n";
				rewardFormulae +=  header + part1 + part2 + "\n\n";				
			}
			
			restoreSitArg += "restoreSitArg(" + o.getName() + "(X),S," + o.getName() + "(X,S)).\n";
			
			if (o.isRoot()) {
				rewardTotal = "rewardInst(R,S) :- " + o.getName() + "(R,S).\n";
			}
		}
		rewardFormulae += "\n" + rewardTotal + "\n";
		
		
		satisfactionFormulae += "\n\n% Condition Box Related\n";
		
		for (Condition cond:a.getConditions()) {
			//fluentList += cond.getName() + "_fl,";
			satisfactionFormulae += cond.getName() + "(s0) :- !,initiallyTrue(" + cond.getName() + "_fl).\n";
			satisfactionFormulae += cond.getName() + "(S) :- " + parser.parseConditionExpression(cond.getFormula()) + ".\n";
			restoreSitArg += "restoreSitArg(" + cond.getName() + ",S," + cond.getName() + "(S)).\n";
		}
		
		satisfactionFormulae += "\n\n% Effect Related\n" + satisfactionFormulae_Effects + "\n";  
		
		fluentList = fluentList.substring(0, fluentList.length() - 1) + "]).\n";
		
		
		if (outputFile.equals("")) {
			printToStdOut(getSpecFromVars());
		} else {
			printToFile(getSpecFromVars());
		}
	}

	
	
	private boolean isInCrossRun(Element e,CrossRunSet crs) {
		boolean found = false;
		for (Element cr: crs.getElements()) {
			found = found || cr.getName().equals(e.getName());
		}
		return found;
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
		numRuns + "\n" +
		problemType + "\n" +
		
		"\n\n%\n% CROSS-RUN ELEMENTS \n% \n\n" +
		crossRunSet + "\n\n" +
		
		"\n\n%\n% EXPORTED STATE ELEMENTS \n% \n\n" +
		discreteExportedSet + "\n\n" +
		continuousExportedSet + "\n\n" +
		
		"\n\n%\n% INITIALIZATIONS \n% \n\n" +
		initList + "\n\n" +
		
		
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
		preconditionAxioms + "\n\n" +
		preconditionAxiomsTasks + "\n" +
		
		"%\n% SATISFACTION FORMULAE \n% \n\n" +
		satisfactionFormulae + "\n" +
		
		"%\n% ATTEMPT FORMULAE \n% \n\n" +
		attemptFormulae + "\n" +
		
		"%\n% ROOT SATISFACTION \n% \n\n" +
		rootSat + "\n" +

		
		"%\n% REWARD FORMULAE \n% \n\n" +
		rewardFormulae + "\n" +
		
		"%\n% SENSE CONDITIONS \n% \n\n" +
		senseConditions + "\n" +
		
		"%\n% RESTORE SITUATION ARGUMENT \n% \n\n" +
		restoreSitArg + "\n");
	}
	

	
	private String getHeader() {
		return (":-style_check(-discontiguous).\n"
				+ ":-style_check(-singleton).\n"
				+ ":- multifile getRewardMode/1.\n"
				+ ":- multifile getRewardModeDTG/1.\n"
				+ ":- multifile penalizeDeadlock/1.\n"
				+ ":- multifile deadlockPenalty/1.\n"
				+ ":- multifile getInfeasiblePenalty/1.\n"
				+ ":- multifile val/2.\n"
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

	private void printToStdOut(String s) {
		System.out.println(s);
	}

}
