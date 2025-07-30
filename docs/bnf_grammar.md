# Primitives
```
[symbol] ::= Any valid XML character except quotes and angle brackets 
[letter] ::="A"|"B"|... |"Z"|"a"|"b"|... |"z" 
[letter-small] ::= "a" | "b" | ... | "z"
[digit] ::= "0" | "1" | ... | "9"
[character] ::= [letter] | [digit] | [symbol] 
[alphanum] ::= [letter] | [digit] | "-" 
[string] ::= [character]+
[identifier] ::= [letter-small][alphanum]*
[boolean] ::= "true" | "false"
[decimal] ::= [digit]+ [ "." [digit]+ ]
[integer] ::= [digit]+
```

# Boolean Expressions
```
[boolean-expression] ::= [bool-const] | [atom] | [previous-bool] | [boolean-operation] | [numeric-comparison]
[bool-const] ::= "<boolConst>" [boolean] "</boolConst>"
[atom] ::= [predicate-id] |[condition-id] | [goal-id] | [task-id] |[effect-id]
[predicate-id] ::= "<predicateID>" [identifier] "</predicateID>"
[condition-id]::="<conditionID>" [identifier] "</conditionID>"
[effect-id]::="<effectID>" [identifier] "</effectID>"
[goal-id] ::= "<goalID>" [identifier] "</goalID>"
[task-id] ::= "<taskID>" [identifier] "</taskID>"
[previous-bool] ::= "<previous>" [atom] "</previous>"
[boolean-operation] ::= [and] | [or] | [not]
[and] ::= "<and>" [boolean-expression] [boolean-expression]+ "</and>"
[or] ::= "<or>" [boolean-expression] [boolean-expression]+ "</or>"
[not] ::= "<not>" [boolean-expression] "</not>"
[comparison-op] 23= "gt " | "gte" | "lt" | "lte" | "eq" | "neq"
[numeric-comparison] ::= "<" [comparison-op] ">" "<left>" [numeric-expression] "</left>" "<right>" [numeric-expression] "</right>" "</" [comparison-op] ">"
```


# Numeric Expressions
```
[numeric-expression] ::= [num-const] | [num-atom] | [num-previous] | [arithmetic-operation]
[num-const] ::= "<numConst>" [decimal] "</numConst>"
[num-atom] ::= [atom] | [variable-id] | [qual-id]
[variable-id] ::= "<variableID>" [identifier] "</variableID>"
[qual-id] ::= "<qualID>" [identifier] "</qualID>"
[num-previous] ::= "<previous>" [num-atom] "</previous>"
[arithmetic-operator] ::= [add] | [subtract] | [multiply] | [divide]
[add] ::= "<add>" [numeric-expression] [numeric-expression]+ "</add>"
[multiply] ::= "<multiply>" [numeric-expression] [numeric-expression]+ "</multiply>"
[subtract] ::= "<subtract>" "<left>" [numeric-expression] "</left>" "<right>" [numeric-expression] "</right>" "</subtract>"
[divide] ::= "<divide>" "<left>" [numeric-expression] "</left>" "<right>" [numeric-expression] "</right>" "</divide>"
```

# Root
```
[istardt-model] ::= "<iStarDT " [mamespaces] ">" [model-header] [options] [actors] "</iStarDT>"
[namespaces] ::= " xmlns=‘" [string] "’" [" xmlns:xsi=‘" [string] "’] [" xsi:schemaLocation= ‘" [string] "’"]
[model-header ] ::= "<header title = ‘" [string] "’ author = ‘" [string] "'source = '" [string] "’ lastUpdated=‘" [string] "’>" [string] "</header>"
[options] ::= "<options continuous = ‘" [boolean] "’ infeasibleActionPenalty = ‘" [decimal] "’></options>"
[actors] ::= "<actors>" [actor]+ "</actors>"
[actor] ::= "<actor " [actor-attributes] ">" [actor-content] "</actor>"
[actor-attributes] ::= "name=‘" [string] "’" [description=‘" [string] "’]
[actor-content] ::= [ [goals] ] [ [tasks] ] [ [qualities] ] [ [predicates] ] [ [variables] ] [ [condBoxes] ] [ [cross-runs] ] [ [exported-set] ] [ [initializations] ]
```

# Goals
```
[goals] ::= "<goals>" [goal]+ "</goals>"
[goal] ::= "<goal name=‘" [identifier] "’" [" root=‘" [boolean] "’"] [" description=‘" [string] "’"] [" terminal=‘" [boolean] "’"] [" episodeLength=‘" [integer] "’"] ">" [goal-content] "</goal>"
[goal-content] ::= [ [pre] ] [ [npr] ] [ [refinement] ]
[pre] ::= "<pre>" [boolean-expression] "</pre>"
[npr] ::= "<npr>" [boolean-expression] "</npr>"
[refinement] ::= "<refinement type=‘" [ref-type] "’>" ([childGoal] | [childTask])+ "</refinement>"
[ref-type] ::= "AND" | "OR"
[childGoal] ::= "<childGoal ref=‘" [identifier] "’/>" /* reference to goal name */
[childTask] ::= "<childTask ref=‘" [identifier] "’/>" /* reference to task name */
```

# Tasks
```
[tasks] ::= "<tasks>" [task]+ "</tasks>"
[task] ::= "<task name=‘" [identifier] "’" [" description=‘" [string] "’"] ">" [task-content] "</task>"
[task-content] ::= [effectGroup] [ [pre] ] [ [npr] ]
[effectGroup] ::= "<effectGroup>" [effect]+ "</effectGroup>"
[effect] ::= "<effect name=‘" [identifier] "’" [ " satisfying=‘" [boolean] "’" ] " probability=‘" [decimal] "’" [ " description=‘" [string] "’" ] ">" [effect-content] "</effect>"
[effect-content] ::= [ [turnsTrue]* ] [ [turnsFalse]* ] [ [sets]*] [ [pre] ] [ [npr] ]
[turnsTrue] ::= "<turnsTrue>" [identifier] "</turnsTrue>"
[turnsFalse] ::= "<turnsFalse>" [identifier] "</turnsFalse>"
[sets] ::= "<set>" [variable-id] [num-const] "</set>"
```


# Qualities
```
[qualities] ::= "<qualities>" [quality]+ "</qualities>"
[quality] ::= "<quality name=‘" [identifier] "’" [" description=‘" [string] "’"] [" root=‘" [boolean] "’"] ">" [numeric-expression] "</quality>"
```

# Predicates and Variables
```
[predicates] ::= "<predicates>" [predicate]+ "</predicates>"
[predicate] ::= "<predicate description =‘" [string] "’>" [identifier] "</predicate>"
[variables] ::= "<variables>" [variable]+ "</variables>"
[variable] ::= "<variable description =‘" [string] "’>" [identifier] "</variable>"
```

# Condition Boxes
```
[condBoxes] ::= "<condBoxes>" [condBox]+ "</condBoxes>"
[condBox]   ::= "<condBox name=‘" [identifier] "’" [" description=‘" <string> "’"] ">" [boolean-expression]"</condBox>"
```

# Cross-runs, Exported Sets, Initializations
```
[cross-runs] ::= "<crossRuns>" [cross-run]+ "</crossRuns>"
[cross-run] ::= "<crossRun>" [predicate-id] | [variable-id] | [qual-id] | [condition-id] "</crossRun>"
[exported-set] ::= "<exportedSet>" [export]+ "</exportedSet>"
[export] ::= ::= "<export continuous=" [boolean] [ "minVal=" [decimal]] [" maxVal=" [decimal] ] ">" [exp-id] "</export>"
[exp-id] ::= [goal-id] | [task-id] | [predicate-id] | [variable-id] | [qual-id] | [condition-id]
[initializations] ::= "<initializations> " [initialization] "</initialization>"
[initialization] ::= [b-init] | [d-init]
[b-init] ::= "<initialization element=" [predicate-id] "> " [boolean] "</initialization>"
[d-init] ::= "<initialization element=" [cont-init-id] "> " [decimal] "</initialization>"
[cont-init-id] ::= [variable-id] | [qual-id]
```