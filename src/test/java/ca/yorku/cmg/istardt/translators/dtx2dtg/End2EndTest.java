package ca.yorku.cmg.istardt.translators.dtx2dtg;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ca.yorku.cmg.istardt.xmlparser.objects.Model;
import ca.yorku.cmg.istardt.xmlparser.xml.IStarUnmarshaller;

class End2EndTest {

	private final String generalPath = "src/test/resources/dtx2dtg/";
	
	private void compareFiles(String file1,String file2) throws IOException {
		Path actual = Path.of(file1);
		Path expected = Path.of(file2);

		List<String> actualLines = Files.readAllLines(actual);
		List<String> expectedLines = Files.readAllLines(expected);

		assertEquals(expectedLines, actualLines, "Files do not match!");
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
		
	@Test
	void test_Order() throws IOException { 
		transIt("1.Order");
	}
	
	@Test
	void test_Build_1R_Discrete()  throws IOException { 
		transIt("2.1.Build_1R_Discrete");
     }
	
	@Test
	void test_Build_3R_Discrete()  throws IOException { 
		transIt("2.2.Build_3R_Discrete");     }
	
	@Test
	void test_Build_3R_Discrete_2()  throws IOException { 
		transIt("2.3.Build_3R_Discrete_2");
	}
	
	@Test
	void test_Build_1R_Mixed()  throws IOException { 
		transIt("2.4.Build_1R_Mixed");
	}

	@Test
	void test_Build_5R_Mixed()  throws IOException { 
		transIt("2.5.Build_5R_Mixed");     
	}
	
	@Test
	void test_Heating_1R_Mixed()  throws IOException { 
		transIt("3.1.Heating_1R_Mixed");     
	}
	
	@Test
	void test_Heating_10R_Mixed()  throws IOException { 
		transIt("3.2.Heating_10R_Mixed");     
    }

	@Test
	void test_OrganizeTravel() throws IOException {     
		transIt("4.1.OrganizeTravel");     }


	
	
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
