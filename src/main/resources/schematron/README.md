# Schematron

https://schematron.com/
https://www.w3schools.com/xml/xpath_intro.asp


Based on this [XML validation library](https://github.com/nina2dv/xml-istar-rl), there are either warnings or errors.



For each report or assert element, add ` role="ERROR"` or `role="WARN"` attribute.
For example: 

```xml
<sch:report test="..." role="WARN">
<sch:assert test="..." role="ERROR">
```

