# Schematron Documentation

## What is Schematron?

Schematron is a rule-based validation language for making assertions about patterns found in XML documents. Unlike grammar-based schema languages (like XSD), Schematron uses XPath expressions to define rules and can validate context-specific constraints that are difficult 
to express with other XML schema languages.

## Key Features

- **Rule-based validation**: Validates XML documents based on rules rather than structure
- **XPath-based**: Uses XPath expressions to select document nodes and check conditions
- **Flexible reporting**: Provides customizable error and warning messages
- **Context-sensitive**: Can validate based on document context or business rules

## Basic Concepts

### Patterns

Patterns group related validation rules together. Each pattern can have a name and contains one or more rules.

```xml
<sch:pattern id="CheckDescriptions">
  <!-- Rules go here -->
</sch:pattern>
```

### Rules

Rules define the context nodes to which assertions and reports apply. The context is defined using an XPath expression:

```xml
<sch:rule context="istar-t:actor | istar-t:goal | istar-t:task">
  <!-- Assertions and reports for actors, goals, and tasks -->
</sch:rule>
```

### Assertions and Reports

- **Assertions (`<sch:assert>`)**: Define conditions that should be true; failures generate messages
- **Reports (`<sch:report>`)**: Define conditions that should trigger a report when true

Both use test attributes containing XPath expressions:

```xml
<!-- Error if an AND-refinement doesn't have at least 2 children -->
<sch:assert test="count(istar-t:childGoal | istar-t:childTask) >= 2" role="ERROR">
  AND-refinement must have at least 2 children.
</sch:assert>

<!-- Warning if a goal doesn't have a description -->
<sch:report test="not(@description)" role="WARN">
  Element &lt;<sch:value-of select="name()"/>&gt; with @name="<sch:value-of select="@name"/>" has no @description.
</sch:report>
```

### Severity Levels

In our implementation, validation messages have two severity levels:

- **Errors (`role="ERROR"`)**: Indicate invalid documents that must be fixed
- **Warnings (`role="WARN"`)**: Suggest potential issues but don't invalidate the document

## XPath Essentials for Schematron

Schematron relies heavily on XPath expressions. Here are some common XPath patterns used in our Schematron rules:

### Node Selection

- `//element`: Select all elements with the given name
- `element1/element2`: Select all element2 that are direct children of element1
- `element1//element2`: Select all element2 descendants of element1
- `element1 | element2`: Select all element1 and element2 nodes

### Attributes

- `@attribute`: Select the attribute with the given name
- `element[@attribute]`: Select elements that have the specified attribute
- `element[@attribute="value"]`: Select elements with an attribute equal to a value

### Functions

- `count(nodeset)`: Count the number of nodes in a nodeset
- `sum(nodeset)`: Sum the values of nodes in a nodeset
- `not(expression)`: Negate a boolean expression
- `normalize-space(string)`: Remove whitespace from a string
- `contains(string1, string2)`: Check if string1 contains string2

### Variables

Variables store XPath expressions for reuse:

```xml
<sch:let name="name" value="@name"/>
<sch:let name="allNamed" value="//istar-t:actor[@name=$name]"/>
```

## Common Validation Patterns

### Required Attributes

```xml
<!-- Error if probability is missing -->
<sch:assert test="@probability" role="ERROR">
  Effect "<sch:value-of select="@name"/>" is missing the required probability attribute.
</sch:assert>
```

### Value Constraints

```xml
<!-- Error if probability is not between 0 and 1 -->
<sch:assert test="@probability castable as xs:decimal and @probability >= 0 and @probability <= 1" role="ERROR">
  Effect "<sch:value-of select="@name"/>" has an invalid @probability. It must be a decimal between 0 and 1.
</sch:assert>
```

### Uniqueness Constraints

```xml
<!-- Error if element name is not unique -->
<sch:assert test="count($allNamed) = 1" role="ERROR">
  Element &lt;<sch:value-of select="$currentType"/>&gt; with name="<sch:value-of select="$name"/>" is not unique.
</sch:assert>
```

### Reference Validation

```xml
<!-- Error if childGoal references non-existent goal -->
<sch:assert test="@ref = ancestor::istar-t:actor//istar-t:goal/@name" role="ERROR">
  childGoal ref="<sch:value-of select="@ref"/>" does not match any &lt;goal name="..."&gt;.
</sch:assert>
```

### Numerical Constraints

```xml
<!-- Error if effect probabilities don't sum to 1.0 -->
<sch:assert test="abs(xs:decimal(sum(istar-t:effect/@probability)) - 1.0) le 0.001" role="ERROR">
  The sum of @probability in &lt;effectGroup&gt; should be 1.0, but it is <sch:value-of select="format-number(sum(istar-t:effect/@probability), '0.000')"/>.
</sch:assert>
```

## Schematron in XML Validation Library

Schematron complements XSD validation by checking:

1. **Business rules**: Logical constraints that XSD cannot express
2. **Cross-element relationships**: Validating references between elements
3. **Quantitative constraints**: Checking numeric properties like probability sums
4. **Best practices**: Warning about recommended patterns

## Custom Message Formatting

Schematron allows rich message formatting using XPath expressions:

```xml
<sch:report test="not(@description)" role="WARN">
  Element &lt;<sch:value-of select="name()"/>&gt; with @name="<sch:value-of select="@name"/>" has no @description.
</sch:report>
```

## Adding New Rules

To add new Schematron rules:

1. Identify the validation requirement
2. Determine the appropriate pattern and context
3. Write the XPath expression for the test condition
4. Create an `assert` (for must-be-true conditions) or `report` (for must-not-be-true conditions)
5. Set the appropriate role (`ERROR` or `WARN`)
6. Add a clear error message

Example of adding a new rule:

```xml
<sch:pattern id="CheckNewRequirement">
  <sch:rule context="istar-t:element">
    <sch:assert test="condition-that-must-be-true" role="ERROR">
      Clear error message explaining the problem and how to fix it.
    </sch:assert>
  </sch:rule>
</sch:pattern>
```

## References

- [Schematron Official Website](https://schematron.com/)
- [W3C XPath Introduction](https://www.w3schools.com/xml/xpath_intro.asp)
- [XML Validation Library](https://github.com/nina2dv/xml-istar-rl)