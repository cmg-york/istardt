<?xml version="1.0" encoding="UTF-8"?>
<sch:schema
        xmlns:sch="http://purl.oclc.org/dsdl/schematron"
        xmlns:istar-dt-x="https://example.org/istar-dt-x"
        queryBinding="xslt2">

  <sch:ns prefix="istar-dt-x" uri="https://example.org/istar-dt-x"/>

  <sch:pattern id="CheckEffectGroupProbabilitySum">
    <sch:rule context="istar-dt-x:effectGroup">
      <sch:assert test="abs(xs:decimal(sum(istar-dt-x:effect/@probability)) - 1.0) le 0.001" role="ERROR">
        <sch:text>
          The sum of @probability in &lt;effectGroup&gt; should be 1.0, but it is <sch:value-of select="format-number(sum(istar-dt-x:effect/@probability), '0.000')"/>.
        </sch:text>
      </sch:assert>
    </sch:rule>
  </sch:pattern>

</sch:schema>
