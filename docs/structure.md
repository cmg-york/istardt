## Architecture Overview for the iStar-T XML Unmarshalling System

The system follows these design patterns:

1. DTO Pattern: XML-specific data transfer objects to decouple XML structure from domain model
- Located in com.example.xml.dto package
- Direct mapping to XML schema elements

2. Builder Pattern: Fluent builders for domain objects
- Located in com.example.objects.builders package
- Simplifies construction of complex domain objects

3. Mapper Pattern: Convert between DTOs and domain objects
- Located in com.example.xml.mappers package
- Each mapper handles a specific domain object type

4. Factory Pattern: Create complex objects (like Formula objects)
- Located in com.example.xml.util package
- Encapsulates creation logic for complex structures

5. Facade Pattern: Simplified API for the client
- IStarUnmarshaller as the main entry point
- Hides complexity of mapping process

Component Structure:

1. Domain Model (com.example.objects)
- Core business objects (Model, Actor, Goal, Task, etc.)
- Abstract classes and interfaces (Element, Formula, etc.)

2. XML DTOs (com.example.xml.dto)
- XML-specific data structures
- Annotated with JAXB annotations

3. Mappers (com.example.xml.mappers)
- Convert DTOs to domain objects
- Manage relationships between objects

4. Builders (com.example.objects.builders)
- Fluent interfaces for constructing domain objects
- Handle complex initialization logic

5. Utilities (com.example.xml.util)
- Factories for creating complex objects
- Helper methods for XML processing

6. Client API (com.example.xml)
- IStarUnmarshaller as the main entry point
- Configuration options for unmarshalling