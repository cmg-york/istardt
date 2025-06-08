<?xml version="1.0" encoding="UTF-8"?>
<sch:schema
        xmlns:sch="http://purl.oclc.org/dsdl/schematron"
        xmlns:istar-dt-x="https://example.org/istar-dt-x"
        queryBinding="xslt2">

  <sch:ns prefix="istar-dt-x" uri="https://example.org/istar-dt-x"/>

  <sch:pattern id="CheckDescriptions">
    <sch:rule context="istar-dt-x:actor | istar-dt-x:quality | istar-dt-x:goal | istar-dt-x:task | istar-dt-x:effect | istar-dt-x:indirectEffect | istar-dt-x:condBox">
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
  </sch:pattern>

  <sch:pattern id="CheckUniqueNames">
    <sch:rule context="istar-dt-x:actor | istar-dt-x:quality | istar-dt-x:goal | istar-dt-x:task | istar-dt-x:effect">
      <sch:let name="name" value="@name"/>
      <sch:let name="currentType" value="local-name()"/>
      <sch:let name="allNamed" value="//istar-dt-x:actor[@name=$name] |
                                    //istar-dt-x:quality[@name=$name] |
                                    //istar-dt-x:goal[@name=$name] |
                                    //istar-dt-x:task[@name=$name] |
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

  <sch:pattern id="CheckRootGoalsEpisodeLength">
    <sch:rule context="istar-dt-x:goal">
<!--      <sch:report test="not(@episodeLength)" role="WARN">-->
<!--        <sch:text>-->
<!--          Root goal "<sch:value-of select="@name"/>" has no @episodeLength. Using default-->
<!--        </sch:text>-->
<!--      </sch:report>-->

      <sch:assert test="@episodeLength castable as xs:integer and @episodeLength &gt; 0" role="ERROR">
        <sch:text>
          Root goal "<sch:value-of select="@name"/>" has an invalid @episodeLength. It must be a positive integer.
        </sch:text>
      </sch:assert>
    </sch:rule>
  </sch:pattern>

</sch:schema>
