<?xml version="1.0" encoding="UTF-8"?>
<sch:schema
        xmlns:sch="http://purl.oclc.org/dsdl/schematron"
        xmlns:istar-t="https://example.org/istar-t"
        queryBinding="xslt2">

  <!-- Declare the 'istar-t' prefix here -->
  <sch:ns prefix="istar-t" uri="https://example.org/istar-t"/>

  <!-- Pattern to Check Descriptions -->
  <sch:pattern id="CheckDescriptions">
    <sch:rule context="istar-t:quality | istar-t:goal | istar-t:task | istar-t:effect | istar-t:indirectEffect | istar-t:prebox">
      <!-- Missing description => report (warning) -->
      <sch:report test="not(@description)" role="WARN">
        <sch:text>
          Element &lt;<sch:value-of select="name()"/>&gt; with @name="<sch:value-of select="@name"/>" has no @description.
        </sch:text>
      </sch:report>
    </sch:rule>

    <sch:rule context="istar-t:predicate">
      <sch:report test="not(@description)" role="WARN">
        <sch:text>
          Element &lt;<sch:value-of select="name()"/>&gt; with content="<sch:value-of select="normalize-space(.)"/>" has no @description.
        </sch:text>
      </sch:report>
    </sch:rule>
  </sch:pattern>


  <!-- Pattern to Check Root Goals Episode Length -->
  <sch:pattern id="CheckRootGoalsEpisodeLength">
    <sch:rule context="istar-t:goal[@root='true']">
      <!-- Missing @episodeLength => warning -->
      <sch:report test="not(@episodeLength)" role="WARN">
        <sch:text>
          Root goal "<sch:value-of select="@name"/>" has no @episodeLength.
        </sch:text>
      </sch:report>

      <!-- Invalid @episodeLength => error -->
      <sch:assert test="@episodeLength castable as xs:integer and @episodeLength &gt; 0" role="ERROR">
        <sch:text>
          Root goal "<sch:value-of select="@name"/>" has an invalid @episodeLength. It must be a positive integer.
        </sch:text>
      </sch:assert>
    </sch:rule>
  </sch:pattern>

  <!-- Pattern to Check Leaf Goals Refinement -->
  <sch:pattern id="CheckLeafGoalsRefinement">
    <sch:rule context="istar-t:goal">
      <!-- No <refinement> => warning -->
      <sch:report test="not(istar-t:refinement)" role="WARN">
        <sch:text>
          Goal "<sch:value-of select="@name"/>" has no &lt;refinement&gt; (leaf-level goal).
        </sch:text>
      </sch:report>
    </sch:rule>
  </sch:pattern>

  <!-- Pattern to Check Refinement Children Count -->
  <sch:pattern id="CheckRefinementChildrenCount">
    <sch:rule context="istar-t:refinement[@type='AND']">
      <sch:assert test="count(istar-t:childGoal | istar-t:childTask) &gt;= 2" role="ERROR">
        <sch:text>
          AND-refinement must have at least 2 children.
        </sch:text>
      </sch:assert>
    </sch:rule>
    <sch:rule context="istar-t:refinement[@type='OR']">
      <sch:assert test="count(istar-t:childGoal | istar-t:childTask) &gt;= 1" role="ERROR">
        <sch:text>
          OR-refinement must have at least 1 child.
        </sch:text>
      </sch:assert>
    </sch:rule>
  </sch:pattern>

  <!-- Pattern to Check Task Reference Count -->
  <sch:pattern id="CheckTaskReferenceCount">
    <sch:rule context="istar-t:task">
      <sch:let name="thisName" value="@name"/>
      <!-- Count how many istar-t:childTask elements refer to this task, anywhere under the same actor -->
      <sch:let name="refCount" value="count(ancestor::istar-t:actor//istar-t:childTask[@ref = $thisName])"/>
      <!-- Not exactly once => warning -->
      <sch:report test="$refCount != 1" role="WARN">
        <sch:text>
          Task "<sch:value-of select="@name"/>" is referenced <sch:value-of select="$refCount"/> time(s); must be exactly once.
        </sch:text>
      </sch:report>
    </sch:rule>
  </sch:pattern>

  <!-- Pattern to Check Child References Exist -->
  <sch:pattern id="CheckChildRefsExist">
    <sch:rule context="istar-t:childGoal">
      <sch:assert test="@ref = ancestor::istar-t:actor//istar-t:goal/@name" role="ERROR">
        <sch:text>
          childGoal ref="<sch:value-of select="@ref"/>" does not match any &lt;goal name="..."&gt;.
        </sch:text>
      </sch:assert>
    </sch:rule>
    <sch:rule context="istar-t:childTask">
      <sch:assert test="@ref = ancestor::istar-t:actor//istar-t:task/@name" role="ERROR">
        <sch:text>
          childTask ref="<sch:value-of select="@ref"/>" does not match any &lt;task name="..."&gt;.
        </sch:text>
      </sch:assert>
    </sch:rule>
  </sch:pattern>

  <!-- Pattern to Check Effects Probability -->
  <sch:pattern id="CheckEffectsProbability">
    <sch:rule context="istar-t:effect">
      <!-- Probability must be 0..1 => error -->
      <sch:assert test="@probability castable as xs:decimal and @probability &gt;= 0 and @probability &lt;= 1" role="ERROR">
        <sch:text>
          Effect "<sch:value-of select="@name"/>" has an invalid @probability attribute. It must be a decimal between 0 and 1.
        </sch:text>
      </sch:assert>

      <!-- Missing @satisfying => warning -->
      <sch:report test="not(@satisfying)" role="WARN">
        <sch:text>
          Effect "<sch:value-of select="@name"/>" has no @satisfying. Default is "true."
        </sch:text>
      </sch:report>

      <!-- @satisfying must be 'true' or 'false' => error -->
      <sch:assert test="not(@satisfying) or @satisfying = 'true' or @satisfying = 'false'" role="ERROR">
        <sch:text>
          Effect "<sch:value-of select="@name"/>" has an invalid @satisfying attribute. It must be "true" or "false".
        </sch:text>
      </sch:assert>
    </sch:rule>
  </sch:pattern>

  <!-- Pattern to Check Sum of Probabilities in Each EffectGroup -->
  <sch:pattern id="CheckEffectGroupProbabilitySum">
    <sch:rule context="istar-t:effectGroup">
      <!-- Assert that the sum of @probability equals 1.0 within a small tolerance -->
      <sch:assert test="abs(xs:decimal(sum(istar-t:effect/@probability)) - 1.0) le 0.001" role="ERROR">
        <sch:text>
          The sum of @probability in &lt;effectGroup&gt; should be 1.0, but it is <sch:value-of select="format-number(sum(istar-t:effect/@probability), '0.000')"/>.
        </sch:text>
      </sch:assert>
    </sch:rule>
  </sch:pattern>


  <!-- Pattern to Check TurnsTrue and TurnsFalse in Predicates -->
  <sch:pattern id="CheckTurnsTrueInPredicates">
    <sch:rule context="istar-t:turnsTrue | istar-t:turnsFalse">
      <sch:assert test="normalize-space(.) = ancestor::istar-t:actor//istar-t:predicates/istar-t:predicate/normalize-space()"
                  role="ERROR">
        <sch:text>
          <sch:value-of select="name()"/>="
          <sch:value-of select="."/>" not found among &lt;predicate&gt; definitions.
        </sch:text>
      </sch:assert>
    </sch:rule>
  </sch:pattern>


  <!-- Pattern to Check Pre/NPR References -->
  <sch:pattern id="CheckPreNprReferences">
    <sch:rule context="istar-t:pre | istar-t:npr">
      <sch:let name="ref" value="normalize-space(.)"/>
      <sch:assert
              test="
          $ref = ancestor::istar-t:actor//istar-t:preBox/@name
          or $ref = ancestor::istar-t:actor//istar-t:goal/@name
          or $ref = ancestor::istar-t:actor//istar-t:task/@name
          or $ref = ancestor::istar-t:actor//istar-t:effectGroup/istar-t:effect/@name
        "
              role="ERROR">
        <sch:text>
          reference "<sch:value-of select="$ref"/>" not found as preBox, goal, task, or effect name.
        </sch:text>
      </sch:assert>
    </sch:rule>
  </sch:pattern>

  <!-- Pattern to Check Atoms in Boolean Expressions -->
  <sch:pattern id="CheckBooleanExpressionAtoms">
    <!-- Check atoms in all boolean expressions -->
    <sch:rule context="istar-t:boolAtom[ancestor::istar-t:preBox or ancestor::istar-t:and or ancestor::istar-t:or or ancestor::istar-t:not]">
      <sch:let name="actor" value="ancestor::istar-t:actor"/>
      <sch:let name="boolAtom" value="normalize-space(.)"/>
      <sch:let name="predicateNames" value="$actor//istar-t:predicates/istar-t:predicate/normalize-space(.)"/>
      <sch:let name="isValidPredicate" value="$boolAtom = $predicateNames"/>

      <!-- Atom must be a valid predicate name -->
      <sch:assert test="$isValidPredicate" role="ERROR">
        <sch:text>
          Boolean expression atom "<sch:value-of select="$boolAtom"/>" is not declared as a predicate.
        </sch:text>
      </sch:assert>
    </sch:rule>

    <!-- Check atoms referenced inside a previous element in boolean expressions -->
    <sch:rule context="istar-t:previous/istar-t:boolAtom[ancestor::istar-t:preBox or ancestor::istar-t:and or ancestor::istar-t:or or ancestor::istar-t:not]">
      <sch:let name="actor" value="ancestor::istar-t:actor"/>
      <sch:let name="boolAtom" value="normalize-space(.)"/>
      <sch:let name="predicateNames" value="$actor//istar-t:predicates/istar-t:predicate/normalize-space(.)"/>
      <sch:let name="isValidPredicate" value="$boolAtom = $predicateNames"/>

      <!-- Atom inside previous must be a valid predicate name -->
      <sch:assert test="$isValidPredicate" role="ERROR">
        <sch:text>
          Boolean expression previous atom "<sch:value-of select="$boolAtom"/>" is not declared as a predicate.
        </sch:text>
      </sch:assert>
    </sch:rule>
  </sch:pattern>

  <!-- Pattern to Check Atoms in Numeric Expressions -->
  <sch:pattern id="CheckNumericExpressionAtoms">
    <!-- Check atoms in all numeric expressions -->
    <sch:rule context="istar-t:numAtom[ancestor::istar-t:quality or ancestor::istar-t:add or ancestor::istar-t:subtract or
                                    ancestor::istar-t:multiply or ancestor::istar-t:divide or ancestor::istar-t:negate]">
      <sch:let name="actor" value="ancestor::istar-t:actor"/>
      <sch:let name="numAtom" value="normalize-space(.)"/>
      <sch:let name="predicateNames" value="$actor//istar-t:predicates/istar-t:predicate/normalize-space(.)"/>
      <sch:let name="qualityNames" value="$actor//istar-t:qualities/istar-t:quality/@name"/>
      <sch:let name="isValidReference" value="$numAtom = $predicateNames or $numAtom = $qualityNames"/>

      <!-- Atom must be a valid predicate or quality name -->
      <sch:assert test="$isValidReference" role="ERROR">
        <sch:text>
          Numeric expression atom "<sch:value-of select="$numAtom"/>" is not declared as a predicate or quality.
        </sch:text>
      </sch:assert>
    </sch:rule>

    <!-- Check atoms referenced inside a previous element in numeric expressions -->
    <sch:rule context="istar-t:previous/istar-t:numAtom[ancestor::istar-t:quality or ancestor::istar-t:add or ancestor::istar-t:subtract or
                                                    ancestor::istar-t:multiply or ancestor::istar-t:divide or ancestor::istar-t:negate]">
      <sch:let name="actor" value="ancestor::istar-t:actor"/>
      <sch:let name="numAtom" value="normalize-space(.)"/>
      <sch:let name="predicateNames" value="$actor//istar-t:predicates/istar-t:predicate/normalize-space(.)"/>
      <sch:let name="qualityNames" value="$actor//istar-t:qualities/istar-t:quality/@name"/>
      <sch:let name="isValidReference" value="$numAtom = $predicateNames or $numAtom = $qualityNames"/>

      <!-- Atom inside previous must be a valid predicate or quality name -->
      <sch:assert test="$isValidReference" role="ERROR">
        <sch:text>
          Numeric expression previous atom "<sch:value-of select="$numAtom"/>" is not declared as a predicate or quality.
        </sch:text>
      </sch:assert>
    </sch:rule>
  </sch:pattern>

  <!-- Pattern to Check Root Quality -->
  <sch:pattern id="CheckRootQuality">
    <sch:rule context="istar-t:quality[@root='true']">
      <sch:assert test="count(ancestor::istar-t:actor/istar-t:qualities/istar-t:quality[@root='true']) = 1" role="ERROR">
        <sch:text>
          There must be exactly one root quality per actor.
        </sch:text>
      </sch:assert>
    </sch:rule>
  </sch:pattern>

</sch:schema>
