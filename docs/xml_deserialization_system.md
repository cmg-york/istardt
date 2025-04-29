# XML Deserialization System

## 1. System Overview

The deserialization system converts XML documents into a structured object model in Java through:

* XML validation against XSD and Schematron schemas
* Jackson-based XML deserialization with custom deserializers
* Two-phase object creation and reference resolution
* Formula expression parsing using the Visitor pattern

## 2. Deserialization Process Flow

The complete process follows these steps:

1. **Validation**: XML is validated against both XSD and Schematron schemas
2. **Unmarshaller Setup**: Jackson's XmlMapper is configured with custom deserializers
3. **Deserialization**: XML is converted to Java objects with ID-based references
4. **Reference Resolution**: Object references are resolved to establish relationships
5. **Model Completion**: The fully processed model is returned

## 3. Core Components

### 3.1 IStarTApplication

The main entry point orchestrating the entire process:
- Validates XML against both XSD and Schematron schemas
- Creates an instance of IStarUnmarshaller
- Unmarshals XML into domain objects
- Prints model information

The application handles the high-level flow, starting with validation and ending with the presentation of the model structure.

### 3.2 XmlValidation

XmlValidation validates XML files against schemas:
- Supports XSD validation for structural correctness
- Supports Schematron validation for complex business rules
- Returns validation results with errors and warnings

This component ensures that the input XML conforms to both structural requirements (via XSD) and business rules (via Schematron) before processing.

### 3.3 IStarUnmarshaller

IStarUnmarshaller coordinates the unmarshalling process:
- Creates and configures a Jackson XmlMapper with custom settings
- Registers the IStarTModule containing all custom deserializers
- Clears the ReferenceResolver before processing a new file
- Converts XML to domain objects using Jackson
- Processes references after deserialization

The key method `unmarshalToModel` demonstrates the two-phase approach: first parsing XML to create objects, then processing references to establish relationships between them.

### 3.4 IStarTModule

IStarTModule is a Jackson module that registers all custom deserializers:
- Extends Jackson's SimpleModule
- Registers deserializers for all domain model classes
- Maps XML elements to their corresponding deserializers

This module tells Jackson which deserializer to use for each domain class, enabling specialized processing for different element types.

## 4. Custom Deserializers (in src/main/java/com/example/xml/deserializers)

### 4.1 BaseDeserializer

BaseDeserializer provides common functionality for all element deserializers:
- Extracts common attributes (ID, name, description)
- Creates and sets Atom objects for elements
- Registers elements with the ReferenceResolver
- Provides helper methods for child nodes and references

Following the Template Method pattern, it defines the overall deserialization process while allowing subclasses to implement element-specific logic. The central method `deserialize` orchestrates:
1. Creating a new element
2. Extracting common attributes
3. Handling element-specific attributes
4. Registering the element with ReferenceResolver

### 4.2 Specific Deserializers

Various deserializers handle specific elements of the iStar-T model:

- **ModelDeserializer**: Handles the root element, creating the Model container and processing Actor elements
- **ActorDeserializer**: Processes actor properties and children (goals, tasks, qualities)
- **GoalDeserializer**: Handles goals and their refinements, including decomposition hierarchies
- **TaskDeserializer**: Processes tasks and their effects, managing probability distributions
- **QualityDeserializer**: Handles quality attributes and their formulas
- **ConditionDeserializer**: Processes conditions and constraints (PreBox elements)
- **IndirectEffectDeserializer**: Handles indirect effects and their formulas
- **EffectDeserializer**: Processes task effects, including probability and state changes
- **FormulaDeserializer**: Uses the visitor pattern to process formula expressions

Each deserializer extends BaseDeserializer and implements the `createNewElement` and `handleSpecificAttributes` methods to provide element-specific processing logic.

### 4.3 DeserializerUtils

The DeserializerUtils class provides common utility methods for deserializers:
- Safe extraction of attributes from JSON nodes
- Standardized error handling for deserialization issues
- Helper methods for processing lists and collections
- Processing formula elements within parent nodes

This utility class centralizes common operations, reducing duplication and ensuring consistent behavior across different deserializers.

## 5. Formula Processing In-Depth

1. XML contains formula elements with operators and operands
2. The FormulaDeserializer identifies the type of formula node
3. It creates a FormulaNodeVisitorImpl to process the node
4. The visitor pattern handles different node types through specialized methods
5. For complex operators, helper methods process operands recursively
6. The resulting Formula object hierarchy represents the formula structure
7. Formula objects are attached to their parent elements (qualities, conditions, etc.)

The Visitor pattern separates formula node traversal from operations performed on nodes, making it easy to add new formula types or operations without modifying existing code.

Key Formula Types:
- **Formula**: Abstract base class for all formulas
- **ANDOperator, OROperator, NOTOperator**: Boolean operators
- **PlusOperator, MinusOperator, MultiplyOperator, DivideOperator**: Arithmetic operators
- **GTOperator, LTOperator, etc.**: Comparison operators
- **OperatorDecorator**: Base class for operators using the Decorator pattern

## 6. Reference Resolution In-Depth

Reference resolution occurs in multiple phases:

**1. Registration Phase**: During deserialization, elements register with ReferenceResolver
- Each element has a unique ID
- Elements are stored in maps by ID and name
- This allows lookup by either ID or name later

**2. Resolution Phase**: After deserialization, ReferenceProcessor resolves references
- Processes goal refinements (AND/OR decompositions)
- Establishes parent-child relationships between decomposition elements
- Sets up task-effect relationships
- Collects all non-decomposition elements for the environment

**Cross-Reference Types**:
- Goal to sub-goals/tasks
- Task to effects
- Parent-child relationships in decomposition hierarchies
- Formula references to other elements

The two-phase approach solves the problem of forward references, where an element might reference another element that hasn't been deserialized yet.

## 7. Jackson XML Annotations

The domain classes use Jackson annotations to control XML mapping:

Key annotations:
* `@JacksonXmlRootElement`: Defines the root element name
* `@JsonDeserialize`: Specifies the custom deserializer
* `@JacksonXmlProperty`: Maps XML attributes/elements to Java fields
* `@JacksonXmlElementWrapper`: Controls collection wrapping
* `@JsonManagedReference` / `@JsonBackReference`: Handles bidirectional relationships

These annotations bridge the gap between XML structure and Java object structure, enabling Jackson to map elements and attributes.

## 8. Deserialization Process Step-by-Step

### Step 1: Initialization and Validation
1. IStarTApplication loads the XML file
2. With XmlValidation (which uses this [validation package](https://github.com/nina2dv/xml-istar-rl)), it validates the XML against the XSD schema. It also validates the XML against the Schematron schema for business rules
4. It creates an instance of IStarUnmarshaller

### Step 2: Unmarshaller Setup
1. IStarUnmarshaller creates and configures a Jackson XmlMapper
2. It registers the IStarTModule with the mapper
3. The module registers all custom deserializers for domain classes

### Step 3: Parse XML to Raw Domain Objects
1. ReferenceResolver is cleared to remove any previous references
2. Jackson's XmlMapper reads the XML file using the registered deserializers
3. The ModelDeserializer handles the root element
4. Child deserializers process their respective XML elements recursively
5. Elements register themselves with the ReferenceResolver during deserialization

### Step 4: Formula Processing
1. Within element deserializers, formula elements are processed
2. The FormulaDeserializer uses FormulaNodeVisitorImpl to process formula nodes
3. The visitor pattern creates appropriate formula objects based on node types
4. Formula objects are attached to their parent elements

### Step 5: Reference Resolution
1. After basic deserialization, ReferenceProcessor processes the model
2. It resolves references between objects (goals, tasks, effects)
3. It establishes parent-child relationships in decomposition hierarchies
4. It collects non-decomposition elements and adds them to the environment
5. It completes the model structure with all relationships established

### Step 6: Return Completed Model
1. The fully processed model is returned to the application
2. The application can now use the model for analysis or visualization
