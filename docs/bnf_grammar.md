# Root Element
```
<istar-model> ::= "<actor" <actor-attributes> ">" <actor-content> "</actor>"

<actor-attributes> ::= "name=\"" <string> "\"" [" description=\"" <string> "\""] [<namespaces>]

<namespaces> ::= " xmlns=\"" <string> "\"" [" xmlns:xsi=\"" <string> "\""] [" xsi:schemaLocation=\"" <string> "\""]

<actor-content> ::= [<predicates>] [<preBoxes>] [<indirectEffects>] [<qualities>] [<goals>] [<tasks>]
```

# Predicates Section
```
<predicates> ::= "<predicates>" <predicate>+ "</predicates>"

<predicate> ::= "<predicate" <predicate-attributes> ">" <string> "</predicate>"

<predicate-attributes> ::= [" primitive=\"" <boolean> "\""] [" init=\"" <boolean> "\""] [" exported=\"" <boolean> "\""] [" description=\"" <string> "\""]
```

# PreBoxes Section
```
<preBoxes> ::= "<preBoxes>" <preBox>+ "</preBoxes>"

<preBox> ::= "<preBox name=\"" <string> "\"" [" description=\"" <string> "\""] ">" "<formula>" <boolean-expression> "</formula>" "</preBox>"
```

# Boolean Expressions
```
<boolean-expression> ::= <boolConst> | <boolAtom> | <previous-bool> | <numeric-comparison> | <boolean-operator>

<boolConst> ::= "<boolConst>" ("true" | "false") "</boolConst>"
<boolAtom> ::= "<boolAtom>" <string> "</boolAtom>"
<previous-bool> ::= "<previous>" "<boolAtom>" <string> "</boolAtom>" "</previous>"

<numeric-comparison> ::= "<" <comparison-op> ">" "<left>" <numeric-expression> "</left>" "<right>" <numeric-expression> "</right>" "</" <comparison-op> ">"
<comparison-op> ::= "gt" | "gte" | "lt" | "lte" | "eq" | "neq"

<boolean-operator> ::= <and> | <or> | <not>
<and> ::= "<and>" <boolean-expression> <boolean-expression>+ "</and>"
<or> ::= "<or>" <boolean-expression> <boolean-expression>+ "</or>"
<not> ::= "<not>" <boolean-expression> "</not>"
```

# Numeric Expressions
```
<numeric-expression> ::= <const> | <numAtom> | <num-previous> | <arithmetic-operator>

<const> ::= "<const>" <decimal> "</const>"
<numAtom> ::= "<numAtom>" <string> "</numAtom>"
<num-previous> ::= "<previous>" "<numAtom>" <string> "</numAtom>" "</previous>"

<arithmetic-operator> ::= <add> | <subtract> | <multiply> | <divide> | <negate>
<add> ::= "<add>" <numeric-expression> <numeric-expression>+ "</add>"
<multiply> ::= "<multiply>" <numeric-expression> <numeric-expression>+ "</multiply>"
<subtract> ::= "<subtract>" "<left>" <numeric-expression> "</left>" "<right>" <numeric-expression> "</right>" "</subtract>"
<divide> ::= "<divide>" "<left>" <numeric-expression> "</left>" "<right>" <numeric-expression> "</right>" "</divide>"
<negate> ::= "<negate>" <numeric-expression> "</negate>"
```

# IndirectEffects Section
```
<indirectEffects> ::= "<indirectEffects>" <indirectEffect>+ "</indirectEffects>"

<indirectEffect> ::= "<indirectEffect name=\"" <string> "\"" [" exported=\"" <boolean> "\""] [" description=\"" <string> "\""] ">" [<formula>] "</indirectEffect>"

<formula> ::= "<formula>" <boolean-expression> "</formula>"
```

# Qualities Section
```
<qualities> ::= "<qualities>" <quality>+ "</qualities>"

<quality> ::= "<quality name=\"" <string> "\"" [" description=\"" <string> "\""] [" exported=\"" <boolean> "\""] [" root=\"" <boolean> "\""] ">" <formula-numeric> "</quality>"

<formula-numeric> ::= "<formula>" <numeric-expression> "</formula>"
```

# Goals Section
```
<goals> ::= "<goals>" <goal>+ "</goals>"

<goal> ::= "<goal name=\"" <string> "\"" [" root=\"" <boolean> "\""] [" description=\"" <string> "\""] [" episodeLength=\"" <string> "\""] ">" <goal-content> "</goal>"

<goal-content> ::= [<pre>*] [<npr>*] [<refinement>]

<pre> ::= "<pre>" <formula> "</pre>"
<npr> ::= "<npr>" <formula> "</npr>"

<refinement> ::= "<refinement type=\"" <string> "\">" (<childGoal> | <childTask>)+ "</refinement>"

<childGoal> ::= "<childGoal ref=\"" <string> "\"/>"
<childTask> ::= "<childTask ref=\"" <string> "\"/>"
```

# Tasks Section
```
<tasks> ::= "<tasks>" <task>+ "</tasks>"

<task> ::= "<task name=\"" <string> "\"" [" description=\"" <string> "\""] ">" <task-content> "</task>"

<task-content> ::= [<pre>*] [<npr>*] [<effectGroup>]

<effectGroup> ::= "<effectGroup>" <effect>+ "</effectGroup>"

<effect> ::= "<effect name=\"" <string> "\"" [" satisfying=\"" <boolean> "\""] " probability=\"" <decimal> "\"" [" description=\"" <string> "\""] ">" <effect-content> "</effect>"

<effect-content> ::= [<turnsTrue>*] [<turnsFalse>*] [<pre>*] [<npr>*]

<turnsTrue> ::= "<turnsTrue>" <string> "</turnsTrue>"
<turnsFalse> ::= "<turnsFalse>" <string> "</turnsFalse>"
```

# Basic Types
```
<string> ::= <character>*
<boolean> ::= "true" | "false"
<decimal> ::= <digit>+ ["." <digit>+]

<character> ::= <letter> | <digit> | <symbol>
<letter> ::= "A" | "B" | ... | "Z" | "a" | "b" | ... | "z"
<digit> ::= "0" | "1" | ... | "9"
<symbol> ::= Any valid XML character except quotes and angle brackets
```