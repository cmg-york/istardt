package ca.yorku.cmg.istardt.translators.dtx2dtg;

import org.junit.jupiter.api.Disabled;

import ca.yorku.cmg.istardt.xmlparser.objects.Atom;
import ca.yorku.cmg.istardt.xmlparser.objects.MultiplyOperator;
import ca.yorku.cmg.istardt.xmlparser.objects.PlusOperator;

@Disabled("Temporarily excluded from test runs")
class FormulaParserTest {

	@Disabled
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
				//parseSimpleQualityExpression(o);
		);		
		
	}

}
