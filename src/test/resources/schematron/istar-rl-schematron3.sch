<?xml version="1.0" encoding="UTF-8"?>
<sch:schema
        xmlns:sch="http://purl.oclc.org/dsdl/schematron"
        xmlns:istar-dt-x="https://example.org/istar-dt-x"
        queryBinding="xslt2">

  <!-- Declare the 'istar-dt-x' prefix here -->
  <sch:ns prefix="istar-dt-x" uri="https://example.org/istar-dt-x"/>

  <!-- Pattern to Check Descriptions -->
  <sch:pattern id="CheckDescriptions">
    <sch:rule context="istar-dt-x:actor | istar-dt-x:quality | istar-dt-x:goal | istar-dt-x:task | istar-dt-x:effect | istar-dt-x:indirectEffect | istar-dt-x:prebox">
      <!-- Missing description => report (warning) -->
      <sch:report test="not(@description)" role="WARN">
        <sch:text>
          Element &lt;<sch:value-of select="name()"/>&gt; with @name="<sch:value-of select="@name"/>" has no @description.
        </sch:text>
      </sch:report>
    </sch:rule>

    <sch:rule context="istar-dt-x:predicate">
      <sch:report test="not(@description)" role="WARN">
        <sch:text>
          Element &lt;<sch:value-of select="name()"/>&gt; with content="<sch:value-of select="normalize-space(.)"/>" has no @description.
        </sch:text>
      </sch:report>
    </sch:rule>
  </sch:pattern>

  <!-- Pattern to Check Unique Names Across All Elements -->
  <sch:pattern id="CheckUniqueNames">
    <!-- Check that element names are unique across all element types in the whole XML -->
    <sch:rule context="istar-dt-x:actor | istar-dt-x:quality | istar-dt-x:goal | istar-dt-x:task | istar-dt-x:effect | istar-dt-x:indirectEffect | istar-dt-x:preBox">
      <sch:let name="name" value="@name"/>
      <sch:let name="currentType" value="local-name()"/>
      <sch:let name="allNamed" value="//istar-dt-x:actor[@name=$name] |
                                    //istar-dt-x:quality[@name=$name] |
                                    //istar-dt-x:goal[@name=$name] |
                                    //istar-dt-x:task[@name=$name] |
                                    //istar-dt-x:effect[@name=$name] |
                                    //istar-dt-x:indirectEffect[@name=$name] |
                                    //istar-dt-x:preBox[@name=$name]"/>

      <sch:assert test="count($allNamed) = 1" role="ERROR">
        <sch:text>
          Element &lt;<sch:value-of select="$currentType"/>&gt; with name="<sch:value-of select="$name"/>" is not unique across all elements in the document.
        </sch:text>
      </sch:assert>
    </sch:rule>

    <!-- Check that predicate content is unique across all predicates in the whole XML -->
    <sch:rule context="istar-dt-x:predicate">
      <sch:let name="content" value="normalize-space(.)"/>
      <sch:let name="allPredicates" value="//istar-dt-x:predicate[normalize-space(.) = $content]"/>

      <sch:assert test="count($allPredicates) = 1" role="ERROR">
        <sch:text>
          Predicate content "<sch:value-of select="$content"/>" is not unique across all predicates in the document.
        </sch:text>
      </sch:assert>
    </sch:rule>
  </sch:pattern>

  <!-- Pattern to Check Root Goals Episode Length -->
  <sch:pattern id="CheckRootGoalsEpisodeLength">
    <sch:rule context="istar-dt-x:goal[@root='true']">
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
    <sch:rule context="istar-dt-x:goal">
      <!-- No <refinement> => warning -->
      <sch:report test="not(istar-dt-x:refinement)" role="WARN">
        <sch:text>
          Goal "<sch:value-of select="@name"/>" has no &lt;refinement&gt; (leaf-level goal).
        </sch:text>
      </sch:report>
    </sch:rule>
  </sch:pattern>

  <!-- Pattern to Check Refinement Children Count -->
  <sch:pattern id="CheckRefinementChildrenCount">
    <sch:rule context="istar-dt-x:refinement[@type='AND']">
      <sch:assert test="count(istar-dt-x:childGoal | istar-dt-x:childTask) &gt;= 2" role="ERROR">
        <sch:text>
          AND-refinement must have at least 2 children.
        </sch:text>
      </sch:assert>
    </sch:rule>
    <sch:rule context="istar-dt-x:refinement[@type='OR']">
      <sch:assert test="count(istar-dt-x:childGoal | istar-dt-x:childTask) &gt;= 1" role="ERROR">
        <sch:text>
          OR-refinement must have at least 1 child.
        </sch:text>
      </sch:assert>
    </sch:rule>
  </sch:pattern>

  <!-- Pattern to Check Task Reference Count -->
  <sch:pattern id="CheckTaskReferenceCount">
    <sch:rule context="istar-dt-x:task">
      <sch:let name="thisName" value="@name"/>
      <!-- Count how many istar-dt-x:childTask elements refer to this task, anywhere under the same actor -->
      <sch:let name="refCount" value="count(ancestor::istar-dt-x:actor//istar-dt-x:childTask[@ref = $thisName])"/>
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
    <sch:rule context="istar-dt-x:childGoal">
      <sch:assert test="@ref = ancestor::istar-dt-x:actor//istar-dt-x:goal/@name" role="ERROR">
        <sch:text>
          childGoal ref="<sch:value-of select="@ref"/>" does not match any &lt;goal name="..."&gt;.
        </sch:text>
      </sch:assert>
    </sch:rule>
    <sch:rule context="istar-dt-x:childTask">
      <sch:assert test="@ref = ancestor::istar-dt-x:actor//istar-dt-x:task/@name" role="ERROR">
        <sch:text>
          childTask ref="<sch:value-of select="@ref"/>" does not match any &lt;task name="..."&gt;.
        </sch:text>
      </sch:assert>
    </sch:rule>
  </sch:pattern>

  <!-- Pattern to Check Effects Probability -->
  <sch:pattern id="CheckEffectsProbability">
    <sch:rule context="istar-dt-x:effect">
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
    <sch:rule context="istar-dt-x:effectGroup">
      <!-- Assert that the sum of @probability equals 1.0 within a small tolerance -->
      <sch:assert test="abs(xs:decimal(sum(istar-dt-x:effect/@probability)) - 1.0) le 0.001" role="ERROR">
        <sch:text>
          The sum of @probability in &lt;effectGroup&gt; should be 1.0, but it is <sch:value-of select="format-number(sum(istar-dt-x:effect/@probability), '0.000')"/>.
        </sch:text>
      </sch:assert>
    </sch:rule>
  </sch:pattern>


  <!-- Pattern to Check TurnsTrue and TurnsFalse in Predicates -->
  <sch:pattern id="CheckTurnsTrueInPredicates">
    <sch:rule context="istar-dt-x:turnsTrue | istar-dt-x:turnsFalse">
      <sch:assert test="normalize-space(.) = ancestor::istar-dt-x:actor//istar-dt-x:predicates/istar-dt-x:predicate/normalize-space()"
                  role="ERROR">
        <sch:text>
          <sch:value-of select="name()"/>="
          <sch:value-of select="."/>" not found among &lt;predicate&gt; definitions.
        </sch:text>
      </sch:assert>
    </sch:rule>
  </sch:pattern>

  <!-- Pattern to Check Atoms in Boolean Expressions (limited to preBox and indirectEffect) -->
  <sch:pattern id="CheckBooleanExpressionAtoms">
    <!-- Check atoms in preBox and indirectEffect formulas only -->
    <sch:rule context="istar-dt-x:boolAtom[
    ancestor::istar-dt-x:preBox/istar-dt-x:formula or
    ancestor::istar-dt-x:indirectEffect/istar-dt-x:formula
  ]">
      <sch:let name="actor" value="ancestor::istar-dt-x:actor"/>
      <sch:let name="boolAtom" value="normalize-space(.)"/>
      <sch:let name="predicateNames" value="$actor//istar-dt-x:predicates/istar-dt-x:predicate/normalize-space(.)"/>
      <sch:let name="isValidPredicate" value="$boolAtom = $predicateNames"/>

      <!-- Atom must be a valid predicate name -->
      <sch:assert test="$isValidPredicate" role="ERROR">
        <sch:text>
          Boolean expression atom "<sch:value-of select="$boolAtom"/>" in preBox/indirectEffect formula is not declared as a predicate.
        </sch:text>
      </sch:assert>
    </sch:rule>
  </sch:pattern>

  <!-- Pattern to Check BoolAtom References in Pre/Npr Formulas for Goals, Tasks and Effects -->
  <sch:pattern id="CheckPreNprFormulaBoolAtoms">
    <sch:rule context="istar-dt-x:boolAtom[
    ancestor::istar-dt-x:goal/istar-dt-x:pre/istar-dt-x:formula or
    ancestor::istar-dt-x:goal/istar-dt-x:npr/istar-dt-x:formula or
    ancestor::istar-dt-x:task/istar-dt-x:pre/istar-dt-x:formula or
    ancestor::istar-dt-x:task/istar-dt-x:npr/istar-dt-x:formula or
    ancestor::istar-dt-x:effect/istar-dt-x:pre/istar-dt-x:formula or
    ancestor::istar-dt-x:effect/istar-dt-x:npr/istar-dt-x:formula
  ]">
      <sch:let name="actor" value="ancestor::istar-dt-x:actor"/>
      <sch:let name="boolAtom" value="normalize-space(.)"/>
      <sch:let name="preBoxNames" value="$actor//istar-dt-x:preBox/@name"/>
      <sch:let name="goalNames" value="$actor//istar-dt-x:goal/@name"/>
      <sch:let name="taskNames" value="$actor//istar-dt-x:task/@name"/>
      <sch:let name="effectNames" value="$actor//istar-dt-x:effectGroup/istar-dt-x:effect/@name"/>
      <sch:let name="predicateNames" value="$actor//istar-dt-x:predicates/istar-dt-x:predicate/normalize-space(.)"/>
      <sch:let name="isValidReference" value="
      $boolAtom = $preBoxNames or
      $boolAtom = $goalNames or
      $boolAtom = $taskNames or
      $boolAtom = $effectNames or
      $boolAtom = $predicateNames
    "/>

      <!-- BoolAtom in pre/npr formulas must reference valid preBox, goal, task, effect, or predicate -->
      <sch:assert test="$isValidReference" role="ERROR">
        <sch:text>
          In pre/npr formula of goal/task/effect, boolAtom "<sch:value-of select="$boolAtom"/>" must reference a valid preBox, goal, task, effect, or predicate name.
        </sch:text>
      </sch:assert>
    </sch:rule>
  </sch:pattern>

  <!-- Pattern to Check Atoms in Numeric Expressions -->
  <sch:pattern id="CheckNumericExpressionAtoms">
    <!-- Check atoms in all numeric expressions -->
    <sch:rule context="istar-dt-x:numAtom[ancestor::istar-dt-x:quality or ancestor::istar-dt-x:add or ancestor::istar-dt-x:subtract or
                                    ancestor::istar-dt-x:multiply or ancestor::istar-dt-x:divide or ancestor::istar-dt-x:negate]">
      <sch:let name="actor" value="ancestor::istar-dt-x:actor"/>
      <sch:let name="numAtom" value="normalize-space(.)"/>
      <sch:let name="predicateNames" value="$actor//istar-dt-x:predicates/istar-dt-x:predicate/normalize-space(.)"/>
      <sch:let name="qualityNames" value="$actor//istar-dt-x:qualities/istar-dt-x:quality/@name"/>
      <sch:let name="isValidReference" value="$numAtom = $predicateNames or $numAtom = $qualityNames"/>

      <!-- Atom must be a valid predicate or quality name -->
      <sch:assert test="$isValidReference" role="ERROR">
        <sch:text>
          Numeric expression atom "<sch:value-of select="$numAtom"/>" is not declared as a predicate or quality.
        </sch:text>
      </sch:assert>
    </sch:rule>

    <!-- Check atoms referenced inside a previous element in numeric expressions -->
    <sch:rule context="istar-dt-x:previous/istar-dt-x:numAtom[ancestor::istar-dt-x:quality or ancestor::istar-dt-x:add or ancestor::istar-dt-x:subtract or
                                                    ancestor::istar-dt-x:multiply or ancestor::istar-dt-x:divide or ancestor::istar-dt-x:negate]">
      <sch:let name="actor" value="ancestor::istar-dt-x:actor"/>
      <sch:let name="numAtom" value="normalize-space(.)"/>
      <sch:let name="predicateNames" value="$actor//istar-dt-x:predicates/istar-dt-x:predicate/normalize-space(.)"/>
      <sch:let name="qualityNames" value="$actor//istar-dt-x:qualities/istar-dt-x:quality/@name"/>
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
    <sch:rule context="istar-dt-x:quality[@root='true']">
      <sch:assert test="count(ancestor::istar-dt-x:actor/istar-dt-x:qualities/istar-dt-x:quality[@root='true']) = 1" role="ERROR">
        <sch:text>
          There must be exactly one root quality per actor.
        </sch:text>
      </sch:assert>
    </sch:rule>
  </sch:pattern>

</sch:schema>
