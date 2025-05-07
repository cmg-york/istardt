```mermaid
classDiagram
direction BT
class ANDOperator {
  + getFormula() String
}
class Actor {
  - List~Effect~ directEffects
  - List~Goal~ goals
  - List~Task~ tasks
  - List~Quality~ qualities
  + getDirectEffects() List~Effect~
  + getGoals() List~Goal~
  + getRoot() Element
  + setTasks(List~Task~) void
  + toString() String
  + setGoals(List~Goal~) void
  + getTasks() List~Task~
  + setDirectEffects(List~Effect~) void
  + setQualities(List~Quality~) void
  + getQualities() List~Quality~
}
class Atom {
  - String id
  - String titleHTMLText
  - String titleText
  - String description
  + getFormula() String
  + setTitleHTMLText(String) void
  + setTitleText(String) void
  + getId() String
  + getAtomRepresentation() String
  + getDescription() String
  + setDescription(String) void
  + toString() String
  + setId(String) void
  + getTitleText() String
  + getTitleHTMLText() String
}
class Condition {
  + getFormula() Formula
}
class DecompType {
<<enumeration>>
  +  TERM
  +  OR
  +  AND
  + valueOf(String) DecompType
  + values() DecompType[]
}
class DecompositionElement {
  - List~DecompositionElement~ children
  - DecompType decompType
  - DecompositionElement parent
  - Formula preFormula
  - Formula nprFormula
  + getPreFormula() Formula
  + setPreFormula(Formula) void
  + addANDChild(DecompositionElement) void
  + getSiblings() List~DecompositionElement~
  + setDecompType(DecompType) void
  + getChildren() List~DecompositionElement~
  + getParent() DecompositionElement
  + getNprFormula() Formula
  + isRoot() boolean
  + setChildren(List~DecompositionElement~) void
  + setNprFormula(Formula) void
  + setParent(DecompositionElement) void
  + addORChild(DecompositionElement) void
  + getDecompType() DecompType
  + isSiblings() boolean
  + toString() String
}
class DivideOperator {
  + getFormula() String
}
class EQOperator {
  + getFormula() String
}
class Effect {
  - Formula nprFormula
  - List~String~ turnsFalse
  - Formula preFormula
  - Task task
  - List~String~ turnsTrue
  - boolean satisfying
  - float probability
  + setTask(Task) void
  + getSiblings() List~Effect~
  + setNprFormula(Formula) void
  + getTurnsTrue() List~String~
  + getNprFormula() Formula
  + isSiblingOf(Effect) boolean
  + setTurnsTrue(List~String~) void
  + addTurnsTrue(String) void
  + setSatisfying(boolean) void
  + setProbability(float) void
  + addTurnsFalse(String) void
  + toString() String
  + getTask() Task
  + getTurnsFalse() List~String~
  + setPreFormula(Formula) void
  + setTurnsFalse(List~String~) void
  + getPreFormula() Formula
  + getProbability() float
  + isSatisfying() boolean
}
class Element {
  - String id
  - Atom representation
  + setRepresentation(Atom) void
  + getAtom() Atom
  + toString() String
  + getName() String
  + setId(String) void
  + getId() String
}
class Environment {
  - List~NonDecompositionElement~ nonDecompElements
  + getNonDecompElements() List~NonDecompositionElement~
  + addNonDecompElement(NonDecompositionElement) void
  + getElementById(String) NonDecompositionElement
  + toString() String
  + setNonDecompElements(List~NonDecompositionElement~) void
}
class Formula {
  + getFormula() String
  + createConstantFormula(String) Formula
  + createBooleanFormula(boolean) Formula
}
class GTEOperator {
  + getFormula() String
}
class GTOperator {
  + getFormula() String
}
class Goal {
  - List~String~ childTaskRefs
  - int runs
  - Actor actor
  - boolean root
  - List~String~ childGoalRefs
  + isRoot() boolean
  + getActor() Actor
  + setRoot(boolean) void
  + setActor(Actor) void
  + getChildGoalRefs() List~String~
  + getRuns() int
  + addChildGoalRef(String) void
  + setRuns(int) void
  + toString() String
  + getChildTaskRefs() List~String~
  + setChildTaskRefs(List~String~) void
  + addChildTaskRef(String) void
  + setChildGoalRefs(List~String~) void
}
class IndirectEffect {
  - boolean exported
  + setExported(boolean) void
  + isExported() boolean
  + getFormula() Formula
}
class LTEOperator {
  + getFormula() String
}
class LTOperator {
  + getFormula() String
}
class MinusOperator {
  + getFormula() String
}
class Model {
  - Environment environment
  - List~Actor~ actors
  + toString() String
  + getEnvironment() Environment
  + getActors() List~Actor~
  + setActors(List~Actor~) void
  + setEnvironment(Environment) void
}
class MultiplyOperator {
  + getFormula() String
}
class NEQOperator {
  + getFormula() String
}
class NOTOperator {
  + getFormula() String
}
class NegateOperator {
  + getFormula() String
}
class NonDecompositionElement {
  - Formula valueFormula
  - Boolean previous
  + getFormula() Formula
  + getPrevious() Boolean
  + setValueFormula(Formula) void
  + setPrevious(Boolean) void
}
class OROperator {
  + getFormula() String
}
class OperatorDecorator {
  # Formula left
  # Formula right
}
class PlusOperator {
  + getFormula() String
}
class PreviousOperator {
  + getFormula() String
}
class Quality {
  - boolean exported
  - Actor actor
  - boolean root
  + setExported(boolean) void
  + isRoot() boolean
  + getFormula() Formula
  + setRoot(boolean) void
  + isExported() boolean
}
class Task {
  - Actor actor
  - List~Effect~ effects
  + setEffects(List~Effect~) void
  + addEffect(Effect) void
  + getEffects() List~Effect~
  + getActor() Actor
  + toString() String
  + setActor(Actor) void
  + isDeterministic() boolean
}

ANDOperator  -->  OperatorDecorator 
Actor "1" *--> "directEffects *" Effect 
Actor  -->  Element 
Actor "1" *--> "goals *" Goal 
Actor "1" *--> "qualities *" Quality 
Actor "1" *--> "tasks *" Task 
Atom  -->  Formula 
Condition  ..>  Atom : «create»
Condition  -->  NonDecompositionElement 
DecompositionElement "1" *--> "decompType 1" DecompType 
DecompositionElement  -->  Element 
DecompositionElement "1" *--> "preFormula 1" Formula 
DivideOperator  -->  OperatorDecorator 
EQOperator  -->  OperatorDecorator 
Effect "1" *--> "preFormula 1" Formula 
Effect  -->  NonDecompositionElement 
Effect "1" *--> "task 1" Task 
Element "1" *--> "representation 1" Atom 
Environment "1" *--> "nonDecompElements *" NonDecompositionElement 
GTEOperator  -->  OperatorDecorator 
GTOperator  -->  OperatorDecorator 
Goal "1" *--> "actor 1" Actor 
Goal  -->  DecompositionElement 
IndirectEffect  ..>  Atom : «create»
IndirectEffect  -->  NonDecompositionElement 
LTEOperator  -->  OperatorDecorator 
LTOperator  -->  OperatorDecorator 
MinusOperator  -->  OperatorDecorator 
Model "1" *--> "actors *" Actor 
Model  ..>  Environment : «create»
Model "1" *--> "environment 1" Environment 
MultiplyOperator  -->  OperatorDecorator 
NEQOperator  -->  OperatorDecorator 
NOTOperator  -->  OperatorDecorator 
NegateOperator  -->  OperatorDecorator 
NonDecompositionElement  -->  Element 
NonDecompositionElement "1" *--> "valueFormula 1" Formula 
OROperator  -->  OperatorDecorator 
OperatorDecorator  -->  Formula 
OperatorDecorator "1" *--> "left 1" Formula 
PlusOperator  -->  OperatorDecorator 
PreviousOperator  -->  OperatorDecorator 
Quality "1" *--> "actor 1" Actor 
Quality  -->  NonDecompositionElement 
Task "1" *--> "actor 1" Actor 
Task  -->  DecompositionElement 
Task "1" *--> "effects *" Effect 
```