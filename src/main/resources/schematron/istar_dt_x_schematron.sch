<?xml version="1.0" encoding="UTF-8"?>
<sch:schema
        xmlns:sch="http://purl.oclc.org/dsdl/schematron"
        xmlns:istar-dt-x="https://example.org/istar-dt-x"
        queryBinding="xslt2">

  <sch:ns prefix="istar-dt-x" uri="https://example.org/istar-dt-x"/>

  <sch:pattern id="CheckDescriptions">
    <sch:rule context="istar-dt-x:actor | istar-dt-x:quality | istar-dt-x:goal | istar-dt-x:task | istar-dt-x:effect | istar-dt-x:condBox">
      <sch:report test="not(@description)" role="WARN">
        <sch:text>
          Element &lt;<sch:value-of select="name()"/>&gt; with @name="<sch:value-of select="@name"/>" has no @description.
        </sch:text>
      </sch:report>
    </sch:rule>

    <sch:rule context="istar-dt-x:predicate | istar-dt-x:variable">
      <sch:report test="not(@description)" role="WARN">
        <sch:text>
          Element &lt;<sch:value-of select="name()"/>&gt; with content="<sch:value-of select="normalize-space(.)"/>" has no @description.
        </sch:text>
      </sch:report>
    </sch:rule>
  </sch:pattern>

  <sch:pattern id="CheckEffectGroupProbabilitySum">
    <sch:rule context="istar-dt-x:effectGroup">
      <sch:assert test="abs(xs:decimal(sum(istar-dt-x:effect/@probability)) - 1.0) le 0.001" role="ERROR">
        <sch:text>
          The sum of @probability in &lt;effectGroup&gt; should be 1.0, but it is <sch:value-of select="format-number(sum(istar-dt-x:effect/@probability), '0.000')"/>.
        </sch:text>
      </sch:assert>
    </sch:rule>
  </sch:pattern>

  <sch:pattern id="CheckEffectsProbability">
    <sch:rule context="istar-dt-x:effect">
    <sch:assert test="@probability castable as xs:decimal and @probability &gt;= 0 and @probability &lt;= 1" role="ERROR">
      <sch:text>
        Effect "<sch:value-of select="@name"/>" has an invalid @probability attribute. It must be a decimal between 0 and 1.
      </sch:text>
    </sch:assert>
    </sch:rule>
  </sch:pattern>

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

  <sch:pattern id="ChildOfMultipleGoals">
    <sch:rule context="istar-dt-x:actor">
      <sch:report test="some $t in distinct-values(.//istar-dt-x:childTask/@ref) satisfies count(.//istar-dt-x:childTask[@ref = $t]) > 1"
              role="ERROR">
        At least one childTask is a child of multiple goals within this actor.
      </sch:report>
      <sch:report test="some $g in distinct-values(.//istar-dt-x:childGoal/@ref)satisfies count(.//istar-dt-x:childGoal[@ref = $g]) > 1"
              role="ERROR">
        At least one childGoal is a child of multiple goals within this actor.
      </sch:report>
    </sch:rule>
  </sch:pattern>

  <sch:pattern id="CheckIdentifiersExist">
    <sch:rule context="istar-dt-x:goalID">
      <sch:assert test="normalize-space(.) = ancestor::istar-dt-x:actor//istar-dt-x:goal/@name" role="ERROR">
        <sch:text>
          goalID "<sch:value-of select="normalize-space(.)"/>" does not match any &lt;goal name="..."&gt;.
        </sch:text>
      </sch:assert>
    </sch:rule>

    <sch:rule context="istar-dt-x:qualID">
      <sch:assert test="normalize-space(.) = ancestor::istar-dt-x:actor//istar-dt-x:quality/@name" role="ERROR">
        <sch:text>
          qualID "<sch:value-of select="normalize-space(.)"/>" does not match any &lt;quality name="..."&gt;.
        </sch:text>
      </sch:assert>
    </sch:rule>

    <sch:rule context="istar-dt-x:conditionID">
      <sch:assert test="normalize-space(.) = ancestor::istar-dt-x:actor//istar-dt-x:condBox/@name" role="ERROR">
        <sch:text>
          conditionID "<sch:value-of select="normalize-space(.)"/>" does not match any &lt;condBox name="..."&gt;.
        </sch:text>
      </sch:assert>
    </sch:rule>

    <sch:rule context="istar-dt-x:effectID">
      <sch:assert test="normalize-space(.) = ancestor::istar-dt-x:actor//istar-dt-x:effect/@name" role="ERROR">
        <sch:text>
          effectID "<sch:value-of select="normalize-space(.)"/>" does not match any &lt;effect name="..."&gt;.
        </sch:text>
      </sch:assert>
    </sch:rule>


    <sch:rule context="istar-dt-x:taskID">
      <sch:assert test="normalize-space(.) = ancestor::istar-dt-x:actor//istar-dt-x:task/@name" role="ERROR">
        <sch:text>
          taskID "<sch:value-of select="normalize-space(.)"/>" does not match any &lt;task name="..."&gt;.
        </sch:text>
      </sch:assert>
    </sch:rule>

    <sch:rule context="istar-dt-x:variableID">
      <sch:assert test="normalize-space(.) = ancestor::istar-dt-x:actor//istar-dt-x:variable/normalize-space(.)" role="ERROR">
        <sch:text>
          variableID “<sch:value-of select='normalize-space(.)'/>” does not match any &lt;variable&gt;.
        </sch:text>
      </sch:assert>
    </sch:rule>

    <sch:rule context="istar-dt-x:predicateID">
      <sch:assert test="normalize-space(.) = ancestor::istar-dt-x:actor//istar-dt-x:predicate/normalize-space(.)" role="ERROR">
        <sch:text>
          predicateID “<sch:value-of select='normalize-space(.)'/>” does not match any &lt;predicate&gt;.
        </sch:text>
      </sch:assert>
    </sch:rule>

    <!-- ===== turnsTrue & turnsFalse check ===== -->
    <sch:rule context="istar-dt-x:turnsTrue">
      <sch:assert test="normalize-space(.) = ancestor::istar-dt-x:actor//istar-dt-x:predicate/normalize-space(.)" role="ERROR">
        <sch:text>
          turnsTrue “<sch:value-of select='normalize-space(.)'/>” does not match any &lt;predicate&gt;.
        </sch:text>
      </sch:assert>
    </sch:rule>

    <sch:rule context="istar-dt-x:turnsFalse">
      <sch:assert test="normalize-space(.) = ancestor::istar-dt-x:actor//istar-dt-x:predicate/normalize-space(.)" role="ERROR">
        <sch:text>
          turnsFalse “<sch:value-of select='normalize-space(.)'/>” does not match any &lt;predicate&gt;.
        </sch:text>
      </sch:assert>
    </sch:rule>
  </sch:pattern>

  <sch:pattern id="CheckUniqueNames">
    <sch:rule context="istar-dt-x:actor | istar-dt-x:quality | istar-dt-x:goal | istar-dt-x:task | istar-dt-x:effect | istar-dt-x:condBox">
      <sch:let name="name" value="@name"/>
      <sch:let name="currentType" value="local-name()"/>
      <sch:let name="allNamed" value="//istar-dt-x:actor[@name=$name] |
                                    //istar-dt-x:quality[@name=$name] |
                                    //istar-dt-x:goal[@name=$name] |
                                    //istar-dt-x:task[@name=$name] |
                                    //istar-dt-x:condBox[@name=$name] |
                                    //istar-dt-x:effect[@name=$name]"/>

      <sch:assert test="count($allNamed) = 1" role="ERROR">
        <sch:text>
          Element &lt;<sch:value-of select="$currentType"/>&gt; with name="<sch:value-of select="$name"/>" is not unique across all elements in the document.
        </sch:text>
      </sch:assert>
    </sch:rule>

    <!-- Check predicate and variable -->
    <sch:rule context="istar-dt-x:predicate | istar-dt-x:variable">
      <sch:let name="content" value="normalize-space(.)"/>
      <sch:let name="currentType" value="local-name()"/>
      <sch:let name="allNamed" value="//istar-dt-x:actor[@name=$content] |
                                    //istar-dt-x:quality[@name=$content] |
                                    //istar-dt-x:goal[@name=$content] |
                                    //istar-dt-x:task[@name=$content] |
                                    //istar-dt-x:condBox[@name=$content] |
                                    //istar-dt-x:effect[@name=$content] |
                                    //istar-dt-x:predicate[normalize-space(.) = $content] |
                                    //istar-dt-x:variable[normalize-space(.) = $content]"/>

      <sch:assert test="count($allNamed) = 1" role="ERROR">
        <sch:text>
          Element &lt;<sch:value-of select="$currentType"/>&gt; with name in its content="<sch:value-of select="$content"/>" is not unique across all elements in the document.
        </sch:text>
      </sch:assert>
    </sch:rule>
  </sch:pattern>

  <sch:pattern id="CheckGoalEpisodeLength">
    <sch:rule context="istar-dt-x:goal">
      <sch:assert test="@episodeLength castable as xs:integer and @episodeLength &gt; 0" role="ERROR">
        <sch:text>
          Goal "<sch:value-of select="@name"/>" has an invalid @episodeLength. It must be a positive integer.
        </sch:text>
      </sch:assert>
    </sch:rule>
  </sch:pattern>

  <sch:pattern id="CheckGoalAndQuality">
    <sch:rule context="istar-dt-x:actor">
      <sch:assert test="count(istar-dt-x:goals/istar-dt-x:goal) &gt; 0"
                  role="WARN">
        Each actor must have at least one goal.
      </sch:assert>
    </sch:rule>

    <sch:rule context="istar-dt-x:actor">
      <sch:assert test="count(istar-dt-x:qualities/istar-dt-x:quality) &gt; 0"
                  role="WARN">
        Each actor must have at least one quality.
      </sch:assert>
    </sch:rule>
  </sch:pattern>

  <sch:pattern id="CheckRoot">
    <sch:rule context="istar-dt-x:qualities">
      <sch:assert test="count(ancestor::istar-dt-x:actor/istar-dt-x:qualities/istar-dt-x:quality[@root='true']) = 1" role="WARN">
        <sch:text>
          There must be exactly one root quality per actor.
        </sch:text>
      </sch:assert>
    </sch:rule>

    <sch:rule context="istar-dt-x:goals">
      <sch:assert test="count(ancestor::istar-dt-x:actor/istar-dt-x:goals/istar-dt-x:goal[@root='true']) = 1" role="WARN">
        <sch:text>
          There must be exactly one root goal per actor.
        </sch:text>
      </sch:assert>
    </sch:rule>
  </sch:pattern>

  <sch:pattern id="CheckInitializationIDs">
    <!-- b-init: boolean content - element must be a predicate ID -->
    <sch:rule context="istar-dt-x:initializations/istar-dt-x:initialization[. = 'true' or . = 'false']">
      <sch:assert test="count(ancestor::istar-dt-x:actor/istar-dt-x:predicates/istar-dt-x:predicate[normalize-space() = current()/@element]) = 1" role="ERROR">
        Initialization references unknown Predicate ID "<sch:value-of select="@element"/>".
      </sch:assert>
    </sch:rule>

    <!-- d-init: decimal content - element must be a quality ID or variable ID -->
    <sch:rule context="istar-dt-x:initializations/istar-dt-x:initialization[. castable as xs:decimal]">
      <sch:assert test="count(ancestor::istar-dt-x:actor/istar-dt-x:qualities/istar-dt-x:quality[@name = current()/@element]) = 1
      or
      count(ancestor::istar-dt-x:actor/istar-dt-x:variables/istar-dt-x:variable[normalize-space() = current()/@element]) = 1
    " role="ERROR">
        Initialization references unknown quality or variable ID "<sch:value-of select="@element"/>".
      </sch:assert>
    </sch:rule>
  </sch:pattern>

  <sch:pattern id="NoInitializations">
    <sch:rule context="istar-dt-x:actor">
      <sch:report test="not(istar-dt-x:initializations) or not(istar-dt-x:initializations/istar-dt-x:initialization)" role="WARN">
        <sch:text>
          Actor "<sch:value-of select="@name"/>" has no &lt;initializations&gt; or it is empty.
          All found variables and qualities will be initialized to 0.
        </sch:text>
      </sch:report>
    </sch:rule>
  </sch:pattern>

  <sch:pattern id="NoCrossRunsWithMultiRunRootGoal">
    <sch:rule context="istar-dt-x:actor">
      <sch:report
              test="(not(istar-dt-x:crossRuns) or not(istar-dt-x:crossRuns/istar-dt-x:crossRun))
             and
            (istar-dt-x:goals/istar-dt-x:goal[@root='true' and @episodeLength > 1])"
              role="WARN">
        <sch:text>
          Actor "<sch:value-of select="@name"/>" has no &lt;crossRuns&gt; or it is empty, but has a root goal with episodeLength &gt; 1.
        </sch:text>
      </sch:report>
    </sch:rule>
  </sch:pattern>

  <sch:pattern id="NoExportedSet">
    <sch:rule context="istar-dt-x:actor">
      <sch:report test="not(istar-dt-x:exportedSet) or not(istar-dt-x:exportedSet/istar-dt-x:export)" role="WARN">
        <sch:text>
          Actor "<sch:value-of select="@name"/>" has no &lt;exportedSet&gt; or it is empty.
          Effect predicates will be used.
        </sch:text>
      </sch:report>
    </sch:rule>
  </sch:pattern>

  <sch:pattern id="TerminalGoal">
    <sch:rule context="istar-dt-x:goal">

      <!-- terminal=true but non-empty refinement -->
      <sch:report
              test="@terminal = 'true' and istar-dt-x:refinement[istar-dt-x:childGoal or istar-dt-x:childTask]"
              role="WARN">
        <sch:text>
          Goal "<sch:value-of select="@name"/>" is marked terminal="true" but has a non-empty &lt;refinement&gt;.
        </sch:text>
      </sch:report>

      <!-- terminal=false but refinement is empty or absent -->
      <sch:report
              test="(@terminal = 'false' or not(@terminal)) and (not(istar-dt-x:refinement) or not(istar-dt-x:refinement/*))"
              role="WARN">
        <sch:text>
          Goal "<sch:value-of select="@name"/>" is terminal="false" but &lt;refinement&gt; is empty or absent.
        </sch:text>
      </sch:report>

      <!-- terminal=true and refinement is empty or absent -->
      <sch:report
              test="@terminal = 'true' and (not(istar-dt-x:refinement) or not(istar-dt-x:refinement/*))"
              role="WARN">
        <sch:text>
          Goal "<sch:value-of select="@name"/>" is terminal="true" and &lt;refinement&gt; is empty or absent.
        </sch:text>
      </sch:report>
    </sch:rule>
  </sch:pattern>

</sch:schema>
