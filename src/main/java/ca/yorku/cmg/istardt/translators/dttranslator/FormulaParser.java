package ca.yorku.cmg.istardt.translators.dttranslator;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import ca.yorku.cmg.istardt.xmlparser.objects.ANDOperator;
import ca.yorku.cmg.istardt.xmlparser.objects.Atom;
import ca.yorku.cmg.istardt.xmlparser.objects.Formula;
import ca.yorku.cmg.istardt.xmlparser.objects.GTOperator;
import ca.yorku.cmg.istardt.xmlparser.objects.LTOperator;
import ca.yorku.cmg.istardt.xmlparser.objects.MultiplyOperator;
import ca.yorku.cmg.istardt.xmlparser.objects.OROperator;
import ca.yorku.cmg.istardt.xmlparser.objects.PlusOperator;
import ca.yorku.cmg.istardt.xmlparser.objects.PreviousOperator;

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
		} else if (f instanceof Atom) {
			//TODO: Check its type and add Sat() or fl() 
			return ((Atom) f).getTitleText() + "_fl(S)";
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
			Atom l = (Atom) ((MultiplyOperator) f).getLeft();
			Atom r = (Atom) ((MultiplyOperator) f).getRight();
			if (isNumeric(l.getTitleText())) { //coefficient is left
				structure.put(r.getTitleText(), l.getTitleText());
			} else if (isNumeric(r.getTitleText())) { //coefficient is right
				structure.put(l.getTitleText(), r.getTitleText());
			}
		} else if (f instanceof PlusOperator) {
			parseSimpleQualityExpressionToMap(((PlusOperator) f).getLeft(),structure);
			parseSimpleQualityExpressionToMap(((PlusOperator) f).getRight(),structure);
		} else {
			System.err.println("Parser warning: type is: " + f.getClass().toGenericString());
		}
		return structure;
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
