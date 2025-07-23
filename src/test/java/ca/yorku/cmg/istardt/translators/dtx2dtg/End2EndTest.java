package ca.yorku.cmg.istardt.translators.dtx2dtg;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ca.yorku.cmg.istardt.xmlparser.objects.Model;
import ca.yorku.cmg.istardt.xmlparser.xml.IStarUnmarshaller;

class End2EndTest {

	private final String generalPath = "src/test/resources/dtx2dtg/";
	

	@Test
	void test_Order() throws IOException { 
		String inputFile = generalPath + "Order.istardtx";
		String outputFile = generalPath + "Order.pl";
    	File xmlFile = new File(inputFile);

    	IStarUnmarshaller unmarshaller = new IStarUnmarshaller();
        Model model = unmarshaller.unmarshalToModel(xmlFile);
        com2dtg trans = new com2dtg(model,outputFile);
        trans.translate();
     }


	@Test
	void test_OrderState() throws IOException { 
		String inputFile = generalPath + "OrderState.istardtx";
		String outputFile = generalPath + "OrderState.pl";
    	File xmlFile = new File(inputFile);

    	IStarUnmarshaller unmarshaller = new IStarUnmarshaller();
        Model model = unmarshaller.unmarshalToModel(xmlFile);
        com2dtg trans = new com2dtg(model,outputFile);
        trans.translate();
     }
	
	@Disabled
	@Test
	void test_OrderVer2() throws IOException {     
		String inputFile = generalPath + "OrderVer2.istardtx";
		String outputFile = generalPath + "OrderVer2.pl";
    	File xmlFile = new File(inputFile);

    	IStarUnmarshaller unmarshaller = new IStarUnmarshaller();
        Model model = unmarshaller.unmarshalToModel(xmlFile);
        com2dtg trans = new com2dtg(model,outputFile);
        trans.translate();
     } 
	

	@Test
	void test_Build() throws IOException {     
		String inputFile = generalPath + "Build.istardtx";
		String outputFile = generalPath + "Build.pl";
    	File xmlFile = new File(inputFile);

    	IStarUnmarshaller unmarshaller = new IStarUnmarshaller();
        Model model = unmarshaller.unmarshalToModel(xmlFile);
        com2dtg trans = new com2dtg(model,outputFile);
        trans.translate();
     } 
	
	@Disabled
	@Test
	void test_OrganizeTravel() throws IOException {     
		String inputFile = generalPath + "OrganizeTravel.istardtx";
		String outputFile = generalPath + "OrganizeTravel.pl";
    	File xmlFile = new File(inputFile);

    	IStarUnmarshaller unmarshaller = new IStarUnmarshaller();
        Model model = unmarshaller.unmarshalToModel(xmlFile);
        com2dtg trans = new com2dtg(model,outputFile);
        trans.translate();
     }
	
	@Disabled
	@Test
	void test_SpecPreparation() throws IOException {     
		String inputFile = generalPath + "SpecPreparation.istardtx";
		String outputFile = generalPath + "SpecPreparation.pl";
    	File xmlFile = new File(inputFile);

    	IStarUnmarshaller unmarshaller = new IStarUnmarshaller();
        Model model = unmarshaller.unmarshalToModel(xmlFile);
        com2dtg trans = new com2dtg(model,outputFile);
        trans.translate();
     } 
	


}
