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
		String inputFile = generalPath + "1.Order.istardtx";
		String outputFile = generalPath + "1.Order.pl";
    	File xmlFile = new File(inputFile);

    	IStarUnmarshaller unmarshaller = new IStarUnmarshaller();
        Model model = unmarshaller.unmarshalToModel(xmlFile);
        com2dtg trans = new com2dtg(model,outputFile);
        trans.translate();
     }

	@Disabled
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
	
	@Test
	void test_Build_1R_Discrete()  throws IOException { 
		String inputFile = generalPath + "2.1.Build_1R_Discrete.istardtx";
		String outputFile = generalPath + "2.1.Build_1R_Discrete.pl";
    	File xmlFile = new File(inputFile);

    	IStarUnmarshaller unmarshaller = new IStarUnmarshaller();
        Model model = unmarshaller.unmarshalToModel(xmlFile);
        com2dtg trans = new com2dtg(model,outputFile);
        trans.translate();
     }
	
	@Test
	void test_Build_3R_Discrete()  throws IOException { 
		String inputFile = generalPath + "2.2.Build_3R_Discrete.istardtx";
		String outputFile = generalPath + "2.2.Build_3R_Discrete.pl";
    	File xmlFile = new File(inputFile);

    	IStarUnmarshaller unmarshaller = new IStarUnmarshaller();
        Model model = unmarshaller.unmarshalToModel(xmlFile);
        com2dtg trans = new com2dtg(model,outputFile);
        trans.translate();
     }
	
	@Test
	void test_Build_3R_Discrete_2()  throws IOException { 
		String inputFile = generalPath + "2.3.Build_3R_Discrete_2.istardtx";
		String outputFile = generalPath + "2.3.Build_3R_Discrete_2.pl";
    	File xmlFile = new File(inputFile);

    	IStarUnmarshaller unmarshaller = new IStarUnmarshaller();
        Model model = unmarshaller.unmarshalToModel(xmlFile);
        com2dtg trans = new com2dtg(model,outputFile);
        trans.translate();
     }
	
	@Test
	void test_Build_1R_Mixed()  throws IOException { 
		String inputFile = generalPath + "2.4.Build_1R_Mixed.istardtx";
		String outputFile = generalPath + "2.4.Build_1R_Mixed.pl";
    	File xmlFile = new File(inputFile);

    	IStarUnmarshaller unmarshaller = new IStarUnmarshaller();
        Model model = unmarshaller.unmarshalToModel(xmlFile);
        com2dtg trans = new com2dtg(model,outputFile);
        trans.translate();
     }

	@Test
	void test_Build_5R_Mixed()  throws IOException { 
		String inputFile = generalPath + "2.5.Build_5R_Mixed.istardtx";
		String outputFile = generalPath + "2.5.Build_5R_Mixed.pl";
    	File xmlFile = new File(inputFile);

    	IStarUnmarshaller unmarshaller = new IStarUnmarshaller();
        Model model = unmarshaller.unmarshalToModel(xmlFile);
        com2dtg trans = new com2dtg(model,outputFile);
        trans.translate();
     }
	
	
	@Test
	void test_Heating_1R_Mixed()  throws IOException { 
		String inputFile = generalPath + "3.1.Heating_1R_Mixed.istardtx";
		String outputFile = generalPath + "3.1.Heating_1R_Mixed.pl";
    	File xmlFile = new File(inputFile);

    	IStarUnmarshaller unmarshaller = new IStarUnmarshaller();
        Model model = unmarshaller.unmarshalToModel(xmlFile);
        com2dtg trans = new com2dtg(model,outputFile);
        trans.translate();
     }
	

	@Test
	void test_OrganizeTravel() throws IOException {     
		String inputFile = generalPath + "4.1.OrganizeTravel.istardtx";
		String outputFile = generalPath + "4.1.OrganizeTravel.pl";
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


}
