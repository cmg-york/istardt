# istar-unmarshal

A Java-based system for deserializing iStar-T XML models into a structured object model. This project provides a robust implementation for processing XML goal models with sophisticated handling of formulas, references, and hierarchical structures.

## Table of Contents
- [Features](#features)
- [Requirements](#requirements)
- [Installation](#installation)
- [Usage](#usage)
    - [Running the Application](#running-the-application)
- [Project Structure](#project-structure)
- [Architecture](#architecture)
- [Configuration](#configuration)
- [Testing](#testing)
- [Limitations/Future notes](#limitationsfuture-notes)

## Features

- XML validation against XSD and Schematron schemas
- Two-phase deserialization for resolving complex references
- Formula processing using the Visitor pattern
- Comprehensive domain model for iStar-T elements

## Requirements

- Java 17 or higher
- Maven 3.6 or higher

## Installation

1. Clone the repository
2. Enter the project folder
3. Go to `settingsExample.xml` and add in your GitHub username and personal access token (classic) with `read:packages` scope
```xml
<servers>
    <server>
        <id>github</id>
        <username>GITHUB_USERNAME</username>
        <password>GITHUB_PERSONAL_ACCESS_TOKEN</password>
    </server>
</servers>
```
https://docs.github.com/en/packages/learn-github-packages/about-permissions-for-github-packages#visibility-and-access-permissions-for-packages
> In most registries, to pull a package, you must authenticate with a personal access token or GITHUB_TOKEN, regardless of whether the package is public or private.

> To download and install packages from a repository, your personal access token (classic) must have the read:packages scope, and your user account must have read permission.
4. Rename `settingsExample.xml` to `settings.xml`
5. Run mvn command:

```bash
mvn clean install -s settings.xml
```
6. To run the main application (IStarTApplication), run:

```bash
mvn exec:java
```

## Usage

### Running the Application

The main application reads an XML file, validates it against both XSD and Schematron schemas, and prints model information:

```bash
mvn exec:java
```

This will use the default XML file specified in `IStarTApplication.java` (figure1a_fixed2.xml).

To use different input files, you'll need to modify the file path constants in `IStarTApplication.java`:
```java
public class IStarTApplication {

    private static final String XSD_SCHEMA_PATH = "src/main/resources/xsd/istar-rl-schema_v3.xsd";
    private static final String SCHEMATRON_SCHEMA_PATH = "src/main/resources/schematron/istar-rl-schematron3.sch";
    private static final String XML_FILE_PATH = "src/main/resources/xml/figure1a_fixed2.xml";
    // ...
}
```

## Project Structure

```
src
├── main
│   ├── java
│   │   └── com
│   │       └── example
│   │           ├── IStarTApplication.java       # Main application
│   │           ├── XmlValidation.java           # Validation utility
│   │           ├── objects                      # Domain model
│   │           │   ├── Model.java
│   │           │   ├── Actor.java
│   │           │   ├── Goal.java
│   │           │   ├── Task.java
│   │           │   ├── Element.java
│   │           │   ├── DecompositionElement.java
│   │           │   ├── NonDecompositionElement.java
│   │           │   ├── Formula.java
│   │           │   └── ...
│   │           ├── xml                          
│   │               ├── IStarUnmarshaller.java
│   │               ├── ReferenceResolver.java
│   │               ├── deserializers
│   │               │   ├── IStarTModule.java
│   │               │   ├── BaseDeserializer.java
│   │               │   ├── ModelDeserializer.java
│   │               │   ├── ActorDeserializer.java
│   │               │   ├── GoalDeserializer.java
│   │               │   ├── TaskDeserializer.java
│   │               │   ├── FormulaDeserializer.java
│   │               │   └── ...
│   │               ├── formula
│   │               │   ├── FormulaNodeVisitor.java
│   │               │   └── FormulaNodeVisitorImpl.java
│   │               ├── processing
│   │               │   └── ReferenceProcessor.java
│   │               └── utils
│   │                   └── DeserializerUtils.java
│   └── resources
│       ├── xsd                                  # XSD schemas
│       │   └── istar-rl-schema_v3.xsd
│       ├── schematron                           # Schematron schemas
│       │   └── istar-rl-schematron3.sch
│       └── xml                                  # Sample XML files
│           └── figure1a_fixed3.xml
└── pom.xml                                      # Maven configuration
```

## Architecture

The system follows a pipeline architecture with these main components:

1. **Validation**: XML validation against schemas
2. **Unmarshalling**: Conversion of XML to Java objects
3. **Reference Resolution**: Establishing relationships between objects

## Configuration

Configuration is handled through the Maven POM file. The main configurable elements are:

- Java version (currently set to Java 17)
- Jackson dependencies and versions
- XML validation library

## Testing

Run the tests with:

```bash
mvn test
```

## Limitations/Future notes

- Current XSD defines 1 actor per model. In the future, will consider multiple actors per model. 
- 