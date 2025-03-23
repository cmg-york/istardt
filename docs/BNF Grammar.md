# Root Element
```
<istar-model> ::= "<actor" <actor-attributes> ">" <actor-content> "</actor>"

<actor-attributes> ::= "name=\"" <string> "\"" [<namespaces>]

<namespaces> ::= " xmlns=\"" <string> "\"" [" xmlns:xsi=\"" <string> "\""] [" xsi:schemaLocation=\"" <string> "\""]

<actor-content> ::= [<predicates>] [<pre-boxes>] [<indirect-effects>] [<qualities>] [<goals>] [<tasks>]
```
# Predicates Section
```
<predicates> ::= "<predicates>" <predicate>+ "</predicates>"

<predicate> ::= "<predicate" <predicate-attributes> ">" <string> "</predicate>"

<predicate-attributes> ::= [" primitive=\"" <boolean> "\""] [" init=\"" <boolean> "\""] [" exported=\"" <boolean> "\""] [" description=\"" <string> "\""]
```
# PreBoxes Section
```
<pre-boxes> ::= "<preBoxes>" <pre-box>+ "</preBoxes>"

<pre-box> ::= "<preBox name=\"" <string> "\"" [" description=\"" <string> "\""] ">" "<formula>" <boolean-expression> "</formula>" "</preBox>"
```
# Boolean Expressions
```
<boolean-expression> ::= <bool-const> | <bool-atom> | <previous-bool> | <numeric-comparison> | <boolean-operator>

<bool-const> ::= "<boolConst>" ("true" | "false") "</boolConst>"
<bool-atom> ::= "<boolAtom>" <string> "</boolAtom>"
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
<numeric-expression> ::= <const> | <num-atom> | <num-previous> | <arithmetic-operator>

<const> ::= "<const>" <decimal> "</const>"
<num-atom> ::= "<numAtom>" <string> "</numAtom>"
<num-previous> ::= "<previous>" "<numAtom>" <string> "</numAtom>" "</previous>"

<arithmetic-operator> ::= <add> | <binary-arithmetic> | <multiply> | <negate>
<add> ::= "<add>" <numeric-expression> <numeric-expression>+ "</add>"
<multiply> ::= "<multiply>" <numeric-expression> <numeric-expression>+ "</multiply>"
<binary-arithmetic> ::= "<" <binary-op> ">" "<left>" <numeric-expression> "</left>" "<right>" <numeric-expression> "</right>" "</" <binary-op> ">"
<binary-op> ::= "subtract" | "divide"
<negate> ::= "<negate>" <numeric-expression> "</negate>"
```
# IndirectEffects Section
```
<indirect-effects> ::= "<indirectEffects>" <indirect-effect>+ "</indirectEffects>"

<indirect-effect> ::= "<indirectEffect name=\"" <string> "\"" [" exported=\"" <boolean> "\""] [" description=\"" <string> "\""] ">" [<boolean-formula>] "</indirectEffect>"

<boolean-formula> ::= "<formula>" <boolean-expression> "</formula>"
<numeric-formula> ::= "<formula>" <numeric-expression> "</formula>"
```
# Qualities Section
```
<qualities> ::= "<qualities>" <quality>+ "</qualities>"

<quality> ::= "<quality name=\"" <string> "\"" [" description=\"" <string> "\""] [" exported=\"" <boolean> "\""] [" root=\"" <boolean> "\""] ">" <numeric-formula> "</quality>"
```
# Goals Section
```
<goals> ::= "<goals>" <goal>+ "</goals>"

<goal> ::= "<goal name=\"" <string> "\"" [" root=\"" <boolean> "\""] [" description=\"" <string> "\""] [" episodeLength=\"" <string> "\""] ">" <goal-content> "</goal>"

<goal-content> ::= [<pre>*] [<npr>*] [<refinement>]

<pre> ::= "<pre>" <string> "</pre>"
<npr> ::= "<npr>" <string> "</npr>"

<refinement> ::= "<refinement type=\"" <string> "\">" (<child-goal> | <child-task>)+ "</refinement>"

<child-goal> ::= "<childGoal ref=\"" <string> "\"/>"
<child-task> ::= "<childTask ref=\"" <string> "\"/>"
```
# Tasks Section
```
<tasks> ::= "<tasks>" <task>+ "</tasks>"

<task> ::= "<task name=\"" <string> "\"" [" description=\"" <string> "\""] ">" <task-content> "</task>"

<task-content> ::= [<pre>*] [<npr>*] [<effect-group>]

<effect-group> ::= "<effectGroup>" <effect>+ "</effectGroup>"

<effect> ::= "<effect name=\"" <string> "\"" [" satisfying=\"" <boolean> "\""] " probability=\"" <decimal> "\"" [" description=\"" <string> "\""] ">" <effect-content> "</effect>"

<effect-content> ::= [<turns-true>*] [<turns-false>*] [<pre>*] [<npr>*]

<turns-true> ::= "<turnsTrue>" <string> "</turnsTrue>"
<turns-false> ::= "<turnsFalse>" <string> "</turnsFalse>"
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