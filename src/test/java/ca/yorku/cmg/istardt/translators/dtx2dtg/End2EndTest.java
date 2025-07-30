package ca.yorku.cmg.istardt.translators.dtx2dtg;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ca.yorku.cmg.istardt.xmlparser.objects.Model;
import ca.yorku.cmg.istardt.xmlparser.xml.IStarUnmarshaller;
import ca.yorku.cmg.istardt.xmlparser.xml.utils.CustomLogger;
import io.github.nina2dv.xmlvalidation.XmlValidation;

class End2EndTest {

	private final String generalPath = "src/test/resources/dtx2dtg/";
	private static final String XSD_SCHEMA_PATH = "src/main/resources/xsd/istar_dt_x_schema.xsd";
	private static final String SCHEMATRON_SCHEMA_PATH = "src/main/resources/schematron/istar_dt_x_schematron.sch";
	private static final CustomLogger LOGGER = CustomLogger.getInstance();

	private void compareFiles(String file1,String file2) throws IOException {
		Path actual = Path.of(file1);
		Path expected = Path.of(file2);

		List<String> actualLines = Files.readAllLines(actual);
		List<String> expectedLines = Files.readAllLines(expected);

		assertTrue(expectedLines.equals(actualLines), " Translation outcome does not match authoritative: \n.... Produced     : " + file1 + "\n.... Authoritative: " + file2 + "\n.... Files equal?");
	}

	private void transIt(String file) throws IOException {
		System.out.println("\n\n == dtx2dtg New Test ==\nTesting tranlsation for domain: " + file + ".istardtx");
		String inputFile = generalPath + file + ".istardtx";
		String outputFile = generalPath + file + ".pl";
		File xmlFile = new File(inputFile);

		IStarUnmarshaller unmarshaller = new IStarUnmarshaller();
		Model model = unmarshaller.unmarshalToModel(xmlFile);
		com2dtg trans = new com2dtg(model,outputFile);
		trans.translate(false);
		compareFiles(outputFile,outputFile.substring(0, outputFile.length()-3) + "-Auth.pl");
	}

	private void validateIt(String file) {

		String inputFile = generalPath + file + ".istardtx";
		LOGGER.setDebugEnabled(false);

		try {
			File xmlFile = new File(inputFile);

			File xsdFile = new File(XSD_SCHEMA_PATH);
			File schematronFile = new File(SCHEMATRON_SCHEMA_PATH);

			if (!xsdFile.exists()) {
				System.err.println("Error: XSD schema file not found: " + XSD_SCHEMA_PATH);
				System.exit(1);
			}

			if (!schematronFile.exists()) {
				System.err.println("Error: Schematron schema file not found: " + SCHEMATRON_SCHEMA_PATH);
				System.exit(1);
			}

			// Validate XML against XSD schema
			System.out.println("Validating XML against XSD schema...");
			try {
				XmlValidation.validate("xsd", xsdFile.getAbsolutePath(), xmlFile.getAbsolutePath());
			} catch (Exception e) {
				System.err.println("XSD validation failed:");
				System.err.println(e.getMessage());
				System.exit(1);
			}

			// Validate XML against Schematron schema
			System.out.println("Validating XML against Schematron schema...");
			try {
				XmlValidation.validate("schematron", schematronFile.getAbsolutePath(), xmlFile.getAbsolutePath());
			} catch (Exception e) {
				System.err.println("Schematron validation failed:");
				System.err.println(e.getMessage());
				System.exit(1);
			}
		} catch (Exception e) {
			System.err.println("Unexpected error: " + e.getMessage());
			e.printStackTrace();
		} 
	}


	@Test
	void test_Order() throws IOException {
		String file = "1.Order";
		transIt(file);
		validateIt(file);
	}

	@Test
	void test_Build_1R_Discrete()  throws IOException { 
		String file = "2.1.Build_1R_Discrete";
		transIt(file);
		validateIt(file);
	}

	@Test
	void test_Build_3R_Discrete()  throws IOException { 
		String file = "2.2.Build_3R_Discrete";
		transIt(file);
		validateIt(file);
	}

	@Test
	void test_Build_3R_Discrete_2()  throws IOException { 
		String file = "2.3.Build_3R_Discrete_2";
		transIt(file);
		validateIt(file);
	}

	@Test
	void test_Build_1R_Mixed()  throws IOException { 
		String file = "2.4.Build_1R_Mixed";
		transIt(file);
		validateIt(file);
	}

	@Test
	void test_Build_5R_Mixed()  throws IOException { 
		String file = "2.5.Build_5R_Mixed";
		transIt(file);
		validateIt(file);
	}

	@Test
	void test_Heating_1R_Mixed()  throws IOException { 
		String file = "3.1.Heating_1R_Mixed";
		transIt(file);
		validateIt(file);
	}

	@Test
	void test_Heating_10R_Mixed()  throws IOException { 
		String file = "3.2.Heating_10R_Mixed";
		transIt(file);
		validateIt(file);
	}

	@Test
	void test_OrganizeTravel() throws IOException {     
		String file = "4.1.OrganizeTravel";
		transIt(file);
		validateIt(file);	
	}




	/* 
	 * 
	 * Future tests	
	 * 
	 */

	@Disabled
	@Test
	void test_OrderState() throws IOException { 
		String inputFile = generalPath + "OrderState.istardtx";
		String outputFile = generalPath + "OrderState.pl";
		File xmlFile = new File(inputFile);

		IStarUnmarshaller unmarshaller = new IStarUnmarshaller();
		Model model = unmarshaller.unmarshalToModel(xmlFile);
		com2dtg trans = new com2dtg(model,outputFile);
		trans.translate(true);
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
		trans.translate(true);
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
		trans.translate(true);
	} 


}
