package ca.yorku.cmg.istardt.xmlparser;

import com.example.validation.XmlValidationService;
import com.example.validation.ValidationResult;

/**
 * Utility class to run XML validators based on the specified type.
 * <p>
 * Usage: <code>XmlValidation &lt;validator-type&gt; &lt;schema-file&gt; &lt;xml-file&gt;</code>
 * where validator-type can be "xsd" or "schematron".
 * </p>
 */
public class XmlValidation {

    public static void validate(String validatorType, String schemaPath, String xmlPath) throws Exception {
        XmlValidationService service = new XmlValidationService();
        ValidationResult result = service.validate(validatorType, schemaPath, xmlPath);

        if (!result.isValid()) {
            StringBuilder errorMsg = new StringBuilder();
            errorMsg.append("Validation failed for type: ").append(validatorType).append("\nErrors:");
            result.getErrors().forEach(error -> errorMsg.append("\n  ").append(error));
            if (!result.getWarnings().isEmpty()) {
                errorMsg.append("\nWarnings:");
                result.getWarnings().forEach(warning -> errorMsg.append("\n  ").append(warning));
            }
            throw new RuntimeException(errorMsg.toString());
        } else {
            System.out.println("Validation passed for type: " + validatorType);
            if (!result.getWarnings().isEmpty()) {
                System.out.println("Warnings:");
                result.getWarnings().forEach(warning -> System.out.println("  " + warning));
            }
        }
    }
}
