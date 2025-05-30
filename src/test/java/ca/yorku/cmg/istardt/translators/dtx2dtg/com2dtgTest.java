package ca.yorku.cmg.istardt.translators.dtx2dtg;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ca.yorku.cmg.istardt.translators.dtx2dtg.Formatter;
import ca.yorku.cmg.istardt.translators.dtx2dtg.FormulaParser;
import ca.yorku.cmg.istardt.xmlparser.objects.ANDOperator;
import ca.yorku.cmg.istardt.xmlparser.objects.Atom;
import ca.yorku.cmg.istardt.xmlparser.objects.CrossRunSet;
import ca.yorku.cmg.istardt.xmlparser.objects.Element;
import ca.yorku.cmg.istardt.xmlparser.objects.Export;
import ca.yorku.cmg.istardt.xmlparser.objects.ExportedSet;
import ca.yorku.cmg.istardt.xmlparser.objects.Formula;
import ca.yorku.cmg.istardt.xmlparser.objects.GTOperator;
import ca.yorku.cmg.istardt.xmlparser.objects.Goal;
import ca.yorku.cmg.istardt.xmlparser.objects.Initialization;
import ca.yorku.cmg.istardt.xmlparser.objects.InitializationSet;
import ca.yorku.cmg.istardt.xmlparser.objects.LTOperator;
import ca.yorku.cmg.istardt.xmlparser.objects.OROperator;
import ca.yorku.cmg.istardt.xmlparser.objects.Predicate;
import ca.yorku.cmg.istardt.xmlparser.objects.PreviousOperator;
import ca.yorku.cmg.istardt.xmlparser.objects.Quality;
import ca.yorku.cmg.istardt.xmlparser.objects.Task;
import ca.yorku.cmg.istardt.xmlparser.objects.Variable;

class com2dtgTest {
	
	private Formatter formatter;
	
	
	@Test
	void test() {
		assert(true);
	}

	
	

	/** 
	 *  T E S T S - TODO: automate
	 */
	
	@Disabled
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
		//pars.parseSimpleQualityExpressionTest();
		
		/** 
		 * TRANSLATION - END 
		 */
		
		System.out.println(out);
		
	}

	@Disabled
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
	
	@Disabled
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
	
	@Disabled
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

	@Disabled
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
