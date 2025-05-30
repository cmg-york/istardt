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

public class DTTranslator {
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
	private String rootSat = "";
//	private String satisfactionFormulaePre = "";
	private String attemptFormulae = "";
	private String senseConditions = "";
	private String restoreSitArg = "";	

	public DTTranslator(Model m, String path) {
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
			//discreteExport += "]).\n";
			discreteExport = "discreteExportedSet(X) :- fluentList(X),!.\n";
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
		String initializations = "";
		String initLine = "init([";
		
		for (Initialization it: init.getInitializations()) {
			String descr = it.getRef(); //CAUTION: maybe this is getRef

			if ((it.getElement() instanceof Predicate))  {
				initializations += descr + "_fl(s0).\n";
				initLine += descr + "_fl,";
			} else if (it.getElement() instanceof Variable) {
				initializations += descr + "_fl(" + it.getValue() + ",s0).\n";
				initLine += descr + "_fl(" + it.getValue() +  "),";
			} else if (it.getElement() instanceof Quality) {
				initializations += descr + "(" + it.getValue() + ",s0).\n";
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
					
					localAttFormula += formatter.toFluent(pred) + "(S);";
					
					if (e.isSatisfying()) {
						localSatFormula += formatter.toFluent(pred) + "(S);";
						localPreFormula += formatter.toPreFluent(pred) + "(S);";
					}
					
					
				} //Next predicate
				

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
				System.out.println(parseBooleanFormula(t.getPreFormula()));
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
		procedures += "dtgRun :- write('Policy: '), bp(" + a.getGoalRoot().getName() + ",10,_,U,P,x),"
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
		
		
		
		satisfactionFormulae += "\n\n % Condition Box Related\n";
		
		for (Condition cond:a.getConditions()) {
			//fluentList += cond.getName() + "_fl,";
			satisfactionFormulae += cond.getName() + "_fl(s0) :- !,initiallyTrue(" + cond.getName() + "_fl).\n";
			satisfactionFormulae += cond.getName() + "_fl(S) :- " + parser.parseConditionExpression(cond.getFormula()) + ".\n";
			restoreSitArg += "restoreSitArg(" + formatter.toFluent(cond.getName()) + ",S," + formatter.toFluent(cond.getName()) + "(S)).\n";
		}
		
		
		fluentList = fluentList.substring(0, fluentList.length() - 1) + "]).\n";
		
		
		if (outputFile.equals("")) {
			printToStdOut(getSpecFromVars());
		} else {
			printToFile(getSpecFromVars());
		}
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
	
	
	
	private boolean isInCrossRun(Element e,CrossRunSet crs) {
		boolean found = false;
		for (Element cr: crs.getElements()) {
			found = found || cr.getName().equals(e.getName());
		}
		return found;
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
	
	
	/** 
	 *  T E S T S - ERASE IN THE END!
	 */
	

	public void conditionExpressionTest() {
		/** 
		 * CREATE FIXTURE 
		 */
		
		/**
		 *  
		bookRefundableTickets 
		OR 
		PREV(nonRefTixFailed) AND (cost GT 50) AND (privacy LT 0.5) AND headAvailable
		OR 
		flightsExist
		
		*/
		
		//Create Atoms
		Atom bookRefundableTickets = new Atom();
		bookRefundableTickets.setTitleText("bookRefundableTickets");
		Atom flightsExist = new Atom();
		flightsExist.setTitleText("flightsExist");
		Atom nonRefTixFailed = new Atom();
		nonRefTixFailed.setTitleText("nonRefTixFailed");
		Atom cost = new Atom();
		cost.setTitleText("cost");
		Atom privacy = new Atom();
		privacy.setTitleText("privacy");
		Atom headAvailable = new Atom();
		headAvailable.setTitleText("headAvailable");
		Atom five = new Atom();
		five.setTitleText("5");
		Atom ten = new Atom();
		ten.setTitleText("10");
		
		
		
		Formula f0 = new PreviousOperator(nonRefTixFailed);
		Formula f1 = new GTOperator(cost, five);
		Formula f2 = new LTOperator(ten, privacy);
		
		Formula conj = new ANDOperator(f0, new ANDOperator(f1, new ANDOperator(f2,headAvailable)));
		
		Formula f = new OROperator(bookRefundableTickets,new OROperator(conj, flightsExist));
		
		/** 
		 * TRANSLATION - START 
		 */
		
		FormulaParser pars = new FormulaParser();
		
		String out = pars.parseConditionExpression(f);
		pars.parseSimpleQualityExpressionTest();
		
		/** 
		 * TRANSLATION - END 
		 */
		
		System.out.println(out);
		
	}


	public void crossRunTest() {
		/** 
		 * CREATE FIXTURE 
		 */
		// Create fixture
		CrossRunSet crs = new CrossRunSet();
		Quality o = new Quality();
		Variable v = new Variable();
		Predicate p = new Predicate();
		
		
		// 
		// QUALITIES
		//
		Atom a = new Atom();
		a.setTitleText("avoidMoneyLoss");
		o.setRepresentation(a);
		crs.addElement(o);

		a = new Atom();
		o = new Quality();
		a.setTitleText("privacy");
		o.setRepresentation(a);
		crs.addElement(o);

		
		// 
		// VARIABLE
		//
		a = new Atom();
		v = new Variable();
		a.setTitleText("cost");
		v.setRepresentation(a);
		crs.addElement(v);
		
		
		// 
		// PREDICATE
		//
		a = new Atom();
		p = new Predicate();
		a.setTitleText("headGranted");
		p.setRepresentation(a);
		crs.addElement(p);
		
		
		/** 
		 * TRANSLATION - START 
		 */
		
		
		String crossState = "%\n% C R O S S   S T A T E \n%\n";
		crossState += "transStateStructure([";
		String debugme = "";
		for (Element cr: crs.getElements()) {
			String descr = cr.getName(); //CAUTION: maybe this is getRef
			debugme += cr.getClass() +",";
			if ((cr instanceof Predicate))  {
				crossState += descr + "_fl,";
			} else if ((cr instanceof Variable) || (cr instanceof Quality)) {
				crossState += descr + "(_),";
			} else {
				System.err.println("ERROR: initialization should be either a predicate, variable, or quality");
			}
		}
		
		System.err.println("HELLO: " + debugme);
		
		crossState  = crossState .substring(0, crossState .length() - 1) + "]).\n";
		
		/** 
		 * TRANSLATION - END 
		 */
		
		
		System.out.println(crossState);
		
	}
	
	
	public void exportedSetTest() {
		/** 
		 * CREATE FIXTURE 
		 */
		// Create fixture
		ExportedSet exp = new ExportedSet();
		Export x = new Export();
		Element g = new Goal();
		Element o = new Quality();
		Element p = new Predicate();
		Atom a = new Atom();
		
		// 
		// GOAL
		//
		a.setId("ticketsBooked-Atom");
		a.setTitleText("ticketsBooked");;
		g.setId("ticketsBooked-Element");
		g.setRepresentation(a);
		x.setElement(g);
		x.setContinuous(false);
		exp.addExport(x);
		
		
		// 
		// QUALITY
		//
		x = new Export();
		a = new Atom();
		
		a.setId("overalQuality-Atom");
		a.setTitleText("overalQuality");;
		o.setId("overallQuality-Element");
		o.setRepresentation(a);
		x.setElement(o);
		x.setContinuous(true);
		exp.addExport(x);
		

		// 
		// QUALITY
		//
		x = new Export();
		a = new Atom();
		
		a.setId("cmtGranted-Atom");
		a.setTitleText("cmtGranted");;
		p.setId("cmtGranted-Element");
		p.setRepresentation(a);
		x.setElement(p);
		x.setContinuous(false);
		exp.addExport(x);
		
		
		// 
		// QUALITY
		//
		x = new Export();
		a = new Atom();
		o = new Quality();
		
		a.setId("privacy-Atom");
		a.setTitleText("privacy");;
		o.setId("privacy-Element");
		o.setRepresentation(a);
		x.setElement(o);
		x.setContinuous(true);
		x.setMinVal(0);
		x.setMaxVal(1);	;
		exp.addExport(x);
		

		/** 
		 * TRANSLATION - START 
		 */
		
		Boolean continuous = false;
		String discreteExport = "%\n% D I S C R E T E   E X P O R T S\n%\n";
		discreteExport += "discreteExportedSet([";
		String continuousExport = "%\n% C O N T I N U O U S   E X P O R T S\n%\n";
		continuousExport += "continuousExportedSet([";
		
		for (Export rt: exp.getExports()) {
			String descr = rt.getElement().getName(); //CAUTION: maybe this is getRef
			if ((rt.getElement() instanceof Goal) || (rt.getElement() instanceof Task))  {
				descr += "_Sat";
				discreteExport += descr + ",";
			} else if (rt.getElement() instanceof Predicate) {
				descr += "_fl";
				discreteExport += descr + ",";
			} else if (rt.getElement() instanceof Quality)  {
				descr += "_fl()," + rt.getMinVal() + "," + rt.getMaxVal();
				continuous = true;
				continuousExport += "[" + descr + "], ";
			}
		}

		discreteExport = discreteExport.substring(0, discreteExport.length() - 1) + "]).\n";
		continuousExport = continuousExport.substring(0, continuousExport.length() - 1) + "]).\n";
		
		/** 
		 * TRANSLATION - END 
		 */
		System.out.println(discreteExport);
		System.out.println(continuousExport);

		
	}

	
	public void initializationTest() {
		/** 
		 * CREATE FIXTURE 
		 */
		// Create fixture
		InitializationSet init = new InitializationSet();
		Initialization in = new Initialization();
		Element p = new Predicate();
		Element v = new Variable();
		Element o = new Quality();
		Atom a = new Atom();
		
		// 
		// PREDICATES
		//
		a.setTitleText("flightsExist");
		p.setRepresentation(a);
		in.setElement(p);
		in.setValue("true");
		init.addInitialization(in);
		
		a = new Atom();
		p = new Predicate();
		in = new Initialization();
		a.setTitleText("headaAvailable");
		p.setRepresentation(a);
		in.setElement(p);
		in.setValue("true");
		init.addInitialization(in);

		
		// 
		// VARIABLE
		//
		a = new Atom();
		v = new Variable();
		in = new Initialization();
		a.setTitleText("cost");
		v.setRepresentation(a);
		in.setElement(v);
		in.setValue("70");
		init.addInitialization(in);
		
		
		/** 
		 * TRANSLATION - START 
		 */
		
		
		String initializations = "%\n% I N I T I A L I Z A T I O N S\n%\n";
		
		for (Initialization it: init.getInitializations()) {
			String descr = it.getElement().getName(); //CAUTION: maybe this is getRef
			if ((it.getElement() instanceof Predicate))  {
				initializations += descr + "_fl(s0).\n";
			} else if (it.getElement() instanceof Variable) {
				initializations += descr + "_fl(" + it.getValue() + ",s0).\n";
			} else {
				System.err.println("ERROR: initialization should be either a predicate or a variable");
			}
		}
		
		/** 
		 * TRANSLATION - END 
		 */
		
		System.out.println(initializations);
		
		
	}

	



	
}
