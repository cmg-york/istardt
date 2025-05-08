package ca.yorku.cmg.istardt.translators.dttranslator;

import ca.yorku.cmg.istardt.xmlparser.objects.*;

public class DTTranslator {
	private Model model;
	private Formatter formatter;
	private String agentActionList = "agentActionList([";
	private String agentActions = "";
	private String stochasticActionList = "stochasticActionList([";
	private String stochasticActions = "";
	private String nonDetActions = "";
	private String fluentList = "fluentList([";
	private String procedures = "";
	private String probabilities = "";
	private String successorStateAxioms = "";
	private String successorStateAxiomsPre = "";
	private String senseConditions = "";
	private String restoreSitArg = "";
	private String satisfactionFormulae = "";
	private String satisfactionFormulaePre = "";
	private String attemptFormulae = "";

	public DTTranslator(Model m) {
		this.model = m;
		this.formatter = new Formatter();
	}

	public Actor getActor() {
		return model.getActors().get(0);
	}

	public void translate() {


		Actor a = getActor();
		System.out.println("There is an actor called: " + a.getAtom().getTitleText());
		
		
		/* 
		 *  Formula Boxes
		 */
		// Set up formulae  
		//for (Condition c:a.) {
		//}		
		
		
		/* 
		 *  Process Tasks
		 */
		for (Task t:a.getTasks()) {
			String taskID = formatter.toCamelCase(t.getName());

			agentActionList += taskID + ",";
			agentActions += "agentAction(" + taskID + ").\n";
			nonDetActions += "nondetActions(" + taskID + ",_,[";

			String localSatFormula = "";
			String localPreFormula = "";
			String localAttFormula = "";

			for (Effect e: t.getEffects()) {
				String effectID = formatter.toCamelCase(e.getName());
				stochasticActionList += effectID + ",";
				stochasticActions += "stochasticAction(" + effectID + ").\n";
				nonDetActions += effectID + ",";
				fluentList += formatter.toFluent(effectID) + ",";
				
				fluentList += formatter.toPreFluent(effectID) + ","; // PRE
				
				probabilities += "prob(" + effectID + "," + e.getProbability() + ",_).\n";
				successorStateAxioms += formatter.toFluent(effectID) + "(do(A,S)) :- " + 
						formatter.toFluent(effectID) + "(S); A=" + effectID + ".\n";
				
				successorStateAxiomsPre += formatter.toPreFluent(effectID) + "(do(A,S)) :- " + 
						formatter.toFluent(effectID) + "(S).\n"; //PRE
				
				senseConditions += "senseCondition(" + effectID + "," + formatter.toFluent(effectID) + ").\n";
				//senseConditions += "senseCondition(" + effectID + "," + formatter.toPreFluent(effectID) + ").\n";
				restoreSitArg += "restoreSitArg(" + formatter.toFluent(effectID) + ",S," + formatter.toFluent(effectID) + "(S)).\n";
				restoreSitArg += "restoreSitArg(" + formatter.toPreFluent(effectID) + ",S," + formatter.toPreFluent(effectID) + "(S)).\n";
				
				localAttFormula += formatter.toFluent(effectID) + "(S);";

				if (e.isSatisfying()) {
					localSatFormula += formatter.toFluent(effectID) + "(S);";
					localPreFormula += formatter.toPreFluent(effectID) + "(S);";
				}
				
				if (e.getFormula() != null) {
					System.out.println("HELLLO:" + t.getPreFormula().toString());
				}

			}
			localSatFormula = formatter.toSat(taskID) + "(S) :- " +  localSatFormula.substring(0, localSatFormula.length() - 1) + ".\n";
			localPreFormula = formatter.toPreFluent(taskID) + "(S) :- " +  localPreFormula.substring(0, localPreFormula.length() - 1) + ".\n";
			localAttFormula = formatter.toAtt(taskID) + "(S) :- " +  localAttFormula.substring(0, localAttFormula.length() - 1) + ".\n";

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
			
			if (g.getDecompType() == DecompType.AND) { // AND Decomposed
				procOp = " : ";
				formOp = ",";
			} else if (g.getDecompType() == DecompType.OR) { //OR Decomposed
				procOp = " # ";
				formOp = ";";
			}
			
			String procLine = "proc(";
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

			satisfactionFormulae += localSatFormula;
			attemptFormulae += localAttFormula;
			satisfactionFormulaePre += localPreFormula;
			procedures += procLine;
		}
		
		/* 
		System.out.println(agentActionList);
		System.out.println(agentActions);
		System.out.println(stochasticActionList);
		System.out.println(stochasticActions);
		System.out.println(fluentList);
		System.out.println(nonDetActions);
		System.out.println(probabilities);
		System.out.println(successorStateAxioms);
		System.out.println(successorStateAxiomsPre);
		System.out.println(senseConditions);
		System.out.println(restoreSitArg);
		System.out.println(satisfactionFormulae);
		System.out.println(attemptFormulae);
		System.out.println(satisfactionFormulaePre);
		System.out.println(procedures);
		 */


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
