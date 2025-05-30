package ca.yorku.cmg.istardt.translators.dtx2dtg;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import ca.yorku.cmg.istardt.xmlparser.objects.ANDOperator;
import ca.yorku.cmg.istardt.xmlparser.objects.Atom;
import ca.yorku.cmg.istardt.xmlparser.objects.Element;
import ca.yorku.cmg.istardt.xmlparser.objects.Formula;
import ca.yorku.cmg.istardt.xmlparser.objects.GTOperator;
import ca.yorku.cmg.istardt.xmlparser.objects.Goal;
import ca.yorku.cmg.istardt.xmlparser.objects.LTOperator;
import ca.yorku.cmg.istardt.xmlparser.objects.MinusOperator;
import ca.yorku.cmg.istardt.xmlparser.objects.MultiplyOperator;
import ca.yorku.cmg.istardt.xmlparser.objects.NOTOperator;
import ca.yorku.cmg.istardt.xmlparser.objects.NumericConstant;
import ca.yorku.cmg.istardt.xmlparser.objects.OROperator;
import ca.yorku.cmg.istardt.xmlparser.objects.PlusOperator;
import ca.yorku.cmg.istardt.xmlparser.objects.Predicate;
import ca.yorku.cmg.istardt.xmlparser.objects.PreviousOperator;
import ca.yorku.cmg.istardt.xmlparser.objects.Quality;
import ca.yorku.cmg.istardt.xmlparser.objects.Task;
import ca.yorku.cmg.istardt.xmlparser.objects.Variable;

public class FormulaParser {
	
	public boolean isNumeric(String str) {
	    if (str == null || str.isEmpty()) return false;
	    try {
	        Double.parseDouble(str);
	        return true;
	    } catch (NumberFormatException e) {
	        return false;
	    }
	}
	
	
	public String parseConditionExpression(Formula f) {
		if (f instanceof OROperator) {
			return "(" + parseConditionExpression(((OROperator) f).getLeft()) + ";" + parseConditionExpression(((OROperator) f).getRight()) + ")";
		} else if (f instanceof ANDOperator) {
			return "(" + parseConditionExpression(((ANDOperator) f).getLeft()) + "," + parseConditionExpression(((ANDOperator) f).getRight()) + ")";
		} else if (f instanceof GTOperator) {
			return  generateComparisonExpression(f,GTOperator.class,"<");
		} else if (f instanceof LTOperator) {
			return  generateComparisonExpression(f,LTOperator.class,"<");
		} else if (f instanceof PreviousOperator) {
			Atom a = (Atom) ((PreviousOperator) f).getLeft();
			return a.getTitleText() + "_fl(s0)";
		} else if (f instanceof NOTOperator) {
			return ("\\+ (" + parseConditionExpression(((NOTOperator) f).getLeft()) + ")");
		} else if (f instanceof Atom) {
			if ((((Atom) f).getElement() instanceof Goal) || (((Atom) f).getElement() instanceof Task)) {
				return ((Atom) f).getTitleText() + "_Sat(S)";
			} else if (((Atom) f).getElement() instanceof Predicate) {
				return ((Atom) f).getTitleText() + "_fl(S)";
			} else {//quality or variable
				return ((Atom) f).getTitleText() + "(S)";
			}
		} else {
			System.err.println("parseConditionExpression: unimplemented operator [" + f.getClass().toGenericString() + "].");
		}
		return "";
	}
	
	
	public String parseSimpleQualityExpression(Formula f) {
		Map<String,String> m = parseSimpleQualityExpressionToMap(f,new HashMap<String,String>());
		System.out.println("Reward constitutents: " + m.toString());
		return prepareSimpleQualityExpression(m);
	}
	
	
	private <T extends Formula> String generateComparisonExpression(
			Formula f, 
			Class<T> operatorType,
			String operator
			) {
		
	    if (!operatorType.isInstance(f)) {
	        throw new IllegalArgumentException("Expected type " + operatorType.getSimpleName() +
	                                           " but got " + f.getClass().getSimpleName());
	    }

	    T op = operatorType.cast(f); // Cast to the given type

	    // Assuming getLeft() and getRight() are defined in the operatorType
	    Formula left, right;
	    try {
	        Method getLeft = operatorType.getMethod("getLeft");
	        Method getRight = operatorType.getMethod("getRight");

	        left = (Formula) getLeft.invoke(op);
	        right = (Formula) getRight.invoke(op);
	    } catch (Exception e) {
	        throw new RuntimeException("Failed to access getLeft/getRight methods", e);
	    }

	    if (!(left instanceof Atom) || !(right instanceof Atom)) {
	        throw new IllegalArgumentException("Expected both operands to be Atoms");
	    }

	    Atom l = (Atom) left;
	    Atom r = (Atom) right;

	    String prep, body;

	    if (isNumeric(l.getTitleText())) {
	        prep = r.getTitleText() + "(V_" + r.getTitleText() + ",S),";
	        body = l.getTitleText() + " " + operator + " V_" + r.getTitleText();

	    } else if (isNumeric(r.getTitleText())) {
	        prep = l.getTitleText() + "(V_" + l.getTitleText() + ",S),";
	        body = "V_" + l.getTitleText() + " " + operator + " " + r.getTitleText();

	    } else {
	        prep = l.getTitleText() + "(V_" + l.getTitleText() + ",S)," +
	               r.getTitleText() + "(V_" + r.getTitleText() + ",S),";
	        body = "V_" + l.getTitleText() + " " + operator + "  V_" + r.getTitleText();
	    }

	    return "(" + prep + body + ")";
	}
	
	
	private String prepareSimpleQualityExpression(Map<String,String> structure) {
		String part1 = "";
		String part2 = "R is ";
        for (Map.Entry<String, String> entry : structure.entrySet()) {
            String identifier = entry.getKey();
            String coefficient = entry.getValue();
            part1 += identifier + "(R_" + identifier + ",S), "; 
            part2 += coefficient + "*R_" + identifier + " + ";
        }
        part2  = part2.substring(0, part2.length() - 3) + ".\n";
        return part1 + part2;
	}
	
	private Map<String,String> parseSimpleQualityExpressionToMap(Formula f, Map<String,String> structure) {
		if (f instanceof MultiplyOperator) {
			Formula l = ((MultiplyOperator) f).getLeft();
			Formula r = ((MultiplyOperator) f).getRight();
			if (l instanceof NumericConstant) { //coefficient is left
				structure.put(((Atom) r).getTitleText(), String.valueOf(((NumericConstant) l).getContent()));
			} else if (l instanceof NumericConstant) { //coefficient is right
				structure.put(((Atom) l).getTitleText(), String.valueOf(((NumericConstant) r).getContent()));
			}
		} else if (f instanceof PlusOperator) {
			parseSimpleQualityExpressionToMap(((PlusOperator) f).getLeft(),structure);
			parseSimpleQualityExpressionToMap(((PlusOperator) f).getRight(),structure);
		} else {
			System.err.println("Parser warning: type is: " + f.getClass().toGenericString());
		}
		return structure;
	}
	
	
	
	
	
	
	public String getAtomExpressionForRewardFormulaPart1(Formula f) {
		Atom m;
		boolean hasPrev = false;
		if ((f instanceof PreviousOperator)) {
			m = (Atom) ((PreviousOperator) f).getLeft();
			hasPrev = true;
		} else {
			m = (Atom) f;
		}
		if ((m.getElement() instanceof Predicate)) {
			return(
					"val(R_" + m.getTitleText() + "_fl," +  m.getTitleText() + "_fl(" + (hasPrev ? "s0" : "S") + ")),\n"  
				);
		} else if ((m.getElement() instanceof Goal) || (m.getElement() instanceof Task)) {
			return(
					"val(R_" + m.getTitleText() + "_Sat," +  m.getTitleText() + "_Sat(" + (hasPrev ? "s0" : "S") + ")),\n"  
				);
		} else if ((m.getElement() instanceof Quality) || (m.getElement() instanceof Variable)) {
			return(
					m.getTitleText() + "(R_" +  m.getTitleText() + "," + (hasPrev ? "s0" : "S") + "),\n"  
				);
		} else {
			System.err.println("Uknown type of :" + m.getTitleText() + " is " + m.getClass());
		}
		return("");
	}
	
	
	public String parseSimpleQualityExpressionPart1(Formula f,String indent) {
		if (f instanceof MultiplyOperator) {
			return parseSimpleQualityExpressionPart1(((MultiplyOperator) f).getLeft(),indent) +  
					parseSimpleQualityExpressionPart1(((MultiplyOperator) f).getRight(),indent);
		} else if (f instanceof PlusOperator) {
			return parseSimpleQualityExpressionPart1(((PlusOperator) f).getLeft(),indent) +  
					parseSimpleQualityExpressionPart1(((PlusOperator) f).getRight(),indent);
		} else if (f instanceof MinusOperator) {
			return parseSimpleQualityExpressionPart1(((MinusOperator) f).getLeft(),indent) +  
					parseSimpleQualityExpressionPart1(((MinusOperator) f).getRight(),indent);
		} else if (f instanceof PreviousOperator) {
			return indent + getAtomExpressionForRewardFormulaPart1(((PreviousOperator) f));
		} else if (f instanceof NumericConstant) {
			return("");
		} else if (f instanceof Atom) {
			return( indent + getAtomExpressionForRewardFormulaPart1((Atom) f));		
		} else {
			String issue = "parseSimpleQualityExpressionPart1 warning: type of |" + f.getFormula() +  "| is: " + f.getClass().toGenericString();
			System.err.println(issue);
			return (issue);
		}
	}
	
	
	public boolean isOperantOfPrevious(Element e, Formula f){
		if (f instanceof MultiplyOperator) {
			return (isOperantOfPrevious(e, ((MultiplyOperator) f).getRight()) ||  
					isOperantOfPrevious(e, ((MultiplyOperator) f).getLeft()));
		} else if (f instanceof PlusOperator) {
			return (isOperantOfPrevious(e, ((PlusOperator) f).getRight()) ||  
					isOperantOfPrevious(e, ((PlusOperator) f).getLeft()));
		} else if (f instanceof MinusOperator) {
			return (isOperantOfPrevious(e, ((MinusOperator) f).getRight()) ||  
					isOperantOfPrevious(e, ((MinusOperator) f).getLeft()));
		} else if (f instanceof PreviousOperator) {
			Atom a = (Atom) ((PreviousOperator) f).getLeft();
			Element q = a.getElement();
			return q.getName().equals(e.getName());
		} else if ((f instanceof NumericConstant) || (f instanceof Atom)) {
			return(false);
		} else {
			String issue = "isOperantOfPrevious warning: type of |" + f.getFormula() +  "| is: " + f.getClass().toGenericString();
			System.err.println(issue);
			return (false);
		}
	}
	
	
	public String getAtomExpressionForRewardFormulaPart2(Formula f) {
		Atom m;
		if ((f instanceof PreviousOperator)) {
			m = (Atom) ((PreviousOperator) f).getLeft();
		} else {
			m = (Atom) f;
		}
		if ((m.getElement() instanceof Predicate) ) {
			return("R_" + m.getTitleText() + "_fl");
		} else if ((m.getElement() instanceof Goal) || (m.getElement() instanceof Task)) {
			return("R_" + m.getTitleText() + "_Sat");
		} else if ((m.getElement() instanceof Quality) || (m.getElement() instanceof Variable)) {
			return("R_" + m.getTitleText());
		} else {
			System.err.println("Uknown type of :" + m.getTitleText() + " is " + m.getClass());
		}
		return("");
	}
	
	public String parseSimpleQualityExpressionPart2(Formula f,String indent) {

		if (f instanceof MultiplyOperator) {
			return "(" + parseSimpleQualityExpressionPart2(((MultiplyOperator) f).getLeft(),indent) + ") * (" + 
			parseSimpleQualityExpressionPart2(((MultiplyOperator) f).getRight(),indent) + ")";
		} else if (f instanceof PlusOperator) {
			return "(" + parseSimpleQualityExpressionPart2(((PlusOperator) f).getLeft(),indent) + ") + (" + 
					parseSimpleQualityExpressionPart2(((PlusOperator) f).getRight(),indent) + ")";
		} else if (f instanceof MinusOperator) {
			return "(" + parseSimpleQualityExpressionPart2(((MinusOperator) f).getLeft(),indent) + ") - (" + 
					parseSimpleQualityExpressionPart2(((MinusOperator) f).getRight(),indent) + ")";
		} else if (f instanceof PreviousOperator) {
			return(getAtomExpressionForRewardFormulaPart2((PreviousOperator) f));
		} else if (f instanceof Atom) {
			return( getAtomExpressionForRewardFormulaPart2((Atom) f));
		} else if (f instanceof NumericConstant) {
			return(String.valueOf(((NumericConstant) f).getContent()));
		} else {
			//Bakaliki
			String issue = "parseSimpleQualityExpressionPart2 warning: type of |" + f.getFormula() +  "| is: " + f.getClass().toGenericString();
			System.err.println(issue);
			return(issue);
			/* if (f.getFormula().equals("0")) {
				Quality qul = new Quality();
				Atom a = new Atom();
				NumericConstant x = new NumericConstant(23);
				a.setElement(qul);
				a.setTitleText("roomTemperature");
				Formula nf = new MinusOperator(a,x);

				return( parseSimpleQualityExpressionPart2(nf,indent) );	
				//return("0");	
			} else if (f.getFormula().equals("Unknown PredicateID")) {
				Atom a = new Atom();
				Predicate p = new Predicate();
				a.setElement(p);
				a.setTitleText("hvacOn");
				return( getAtomExpressionForRewardFormulaPart2((Atom) a));	
			} else {
			return "ERROR in parseSimpleQUalityExpressionPart2";
			} */
		}
	}
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void parseSimpleQualityExpressionTest() {
		Atom c1 = new Atom();
		c1.setTitleText("0.8");
		Atom c2 = new Atom();
		c2.setTitleText("0.2");
		Atom c3 = new Atom();
		c3.setTitleText("0.7");
		Atom c4 = new Atom();
		c4.setTitleText("0.3");
		Atom p1 = new Atom();
		p1.setTitleText("headAuthorizes");
		Atom p2 = new Atom();
		p2.setTitleText("committeeAuthorizes");
		Atom p3 = new Atom();
		p3.setTitleText("avoidMoneyLoss");
		Atom p4 = new Atom();
		p4.setTitleText("privacy");
		
		MultiplyOperator m1 = new MultiplyOperator(c1, p1);
		MultiplyOperator m2 = new MultiplyOperator(p2, c2);
		PlusOperator o1 = new PlusOperator(m1, m2);
		
		MultiplyOperator m3 = new MultiplyOperator(p3, c3);
		MultiplyOperator m4 = new MultiplyOperator(c4, p4);
		PlusOperator o2 = new PlusOperator(m3, m4);
		
		PlusOperator o = new PlusOperator(o1,o2);
		
		System.out.println(
				parseSimpleQualityExpression(o)
		);		
		
	}
	
}
