## Documentation Files

### bnf_grammar.md
Provides a Backus-Naur Form (BNF) grammar specification for the iStar-DT-X XML format. This formal notation describes the syntax rules of the XML language used in the project, detailing the structure of elements such as actors, goals, tasks, and formulas.

### domain_model_uml (.png, .uml)
Contains a UML class diagram of the iStar-DT Java domain model.

### ocl.md (Work in progress)
Lists Object Constraint Language (OCL) constraints applied in the system. These constraints align with Schematron.

### sequence_diagram_deserialization_system.md
Contains a sequence diagram illustrating the XML deserialization process. This diagram shows the interaction between different components of the system during the deserialization of an XML file, including validation, unmarshalling, and reference resolution phases.

### xml_deserialization_system.md (Work in progress)
Provides a detailed explanation of the XML deserialization system architecture. This document describes the components involved in converting XML to Java objects, including the unmarshaller, deserializers, formula processing, and reference resolution mechanisms.

### xml_uml.md
Contains a UML class diagram representing the XML schema structure of the iStar-DT-X format. This diagram shows the relationships between XML elements as defined in the XSD schema.

## Using the Documentation

- For understanding the overall architecture, refer to `xml_deserialization_system.md`.
- To follow the XML format, refer to `bnf_grammar.md` and `xml_uml.md`.
- For details on the domain model implementation, see `domain_model_uml.md`.
- To understand the validation/Schematron rules, read `ocl.md`.
- For a visual representation of the deserialization process, see `sequence_diagram_deserialization_system.md`.