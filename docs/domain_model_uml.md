```mermaid
classDiagram
direction BT
class ANDOperator {
  + getFormula() String
}
class Actor {
  - List~Goal~ goals
  - List~Quality~ qualities
  - List~NonDecompositionElement~ nonDecompElements
  - List~Task~ tasks
  - List~Effect~ effects
  + setNonDecompElements(List~NonDecompositionElement~) void
  + getRoot() Element
  + getNonDecompElements() List~NonDecompositionElement~
  + addNonDecompElement(NonDecompositionElement) void
  + getEffects() List~Effect~
  + setTasks(List~Task~) void
  + setEffects(List~Effect~) void
  + getGoals() List~Goal~
  + setQualities(List~Quality~) void
  + setGoals(List~Goal~) void
  + getQualities() List~Quality~
  + toString() String
  + getTasks() List~Task~
}
class Atom {
  - String id
  - String titleHTMLText
  - String description
  - boolean exported
  - String titleText
  - boolean crossRun
  + toString() String
  + getTitleHTMLText() String
  + setDescription(String) void
  + isExported() boolean
  + setTitleText(String) void
  + getAtomRepresentation() String
  + isCrossRun() boolean
  + setTitleHTMLText(String) void
  + getFormula() String
  + getId() String
  + setId(String) void
  + setExported(boolean) void
  + getDescription() String
  + getTitleText() String
  + setCrossRun(boolean) void
}
class Condition {
  + getFormula() Formula
}
class DecompType {
<<enumeration>>
  +  OR
  +  AND
  +  TERM
  + values() DecompType[]
  + valueOf(String) DecompType
}
class DecompositionElement {
  - DecompType decompType
  - Formula nprFormula
  - List~DecompositionElement~ children
  - Formula preFormula
  - DecompositionElement parent
  + setPreFormula(Formula) void
  + getChildren() List~DecompositionElement~
  + isSiblings() boolean
  + getSiblings() List~DecompositionElement~
  + addANDChild(DecompositionElement) void
  + toString() String
  + setChildren(List~DecompositionElement~) void
  + addORChild(DecompositionElement) void
  + isRoot() boolean
  + setParent(DecompositionElement) void
  + getDecompType() DecompType
  + getParent() DecompositionElement
  + setDecompType(DecompType) void
  + setNprFormula(Formula) void
  + getNprFormula() Formula
  + getPreFormula() Formula
}
class DivideOperator {
  + getFormula() String
}
class DomainPredicate
class DomainVariable
class EQOperator {
  + getFormula() String
}
class Effect {
  - List~String~ turnsTrue
  - boolean satisfying
  - List~String~ turnsFalse
  - Formula nprFormula
  - Formula preFormula
  - Task task
  - float probability
  + isSatisfying() boolean
  + addTurnsFalse(String) void
  + setProbability(float) void
  + setTurnsTrue(List~String~) void
  + getNprFormula() Formula
  + getTask() Task
  + addTurnsTrue(String) void
  + toString() String
  + getTurnsFalse() List~String~
  + getPreFormula() Formula
  + setTask(Task) void
  + getProbability() float
  + setTurnsFalse(List~String~) void
  + getTurnsTrue() List~String~
  + setSatisfying(boolean) void
  + getSiblings() List~Effect~
  + setPreFormula(Formula) void
  + setNprFormula(Formula) void
  + isSiblingOf(Effect) boolean
}
class Element {
  - String id
  - Atom representation
  + setId(String) void
  + setRepresentation(Atom) void
  + getName() String
  + getAtom() Atom
  + getId() String
  + toString() String
}
class Formula {
  + createConstantFormula(String) Formula
  + createBooleanFormula(boolean) Formula
  + getFormula() String
}
class GTEOperator {
  + getFormula() String
}
class GTOperator {
  + getFormula() String
}
class Goal {
  - int runs
  - Actor actor
  - List~String~ childGoalRefs
  - List~String~ childTaskRefs
  - boolean root
  + addChildTaskRef(String) void
  + setRoot(boolean) void
  + getChildGoalRefs() List~String~
  + getActor() Actor
  + setChildGoalRefs(List~String~) void
  + getChildTaskRefs() List~String~
  + getRuns() int
  + setRuns(int) void
  + addChildGoalRef(String) void
  + isRoot() boolean
  + setActor(Actor) void
  + toString() String
  + setChildTaskRefs(List~String~) void
}
class GoalId
class Invariant
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
  - ModelHeader modelHeader
  - List~Actor~ actors
  + setModelHeader(ModelHeader) void
  + getActors() List~Actor~
  + setActors(List~Actor~) void
  + getModelHeader() ModelHeader
}
class ModelHeader {
  - String version
  - String lastUpdated
  - String author
  - String notes
  + getAuthor() String
  + setVersion(String) void
  + setNotes(String) void
  + getLastUpdated() String
  + setLastUpdated(String) void
  + setAuthor(String) void
  + getNotes() String
  + getVersion() String
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
  + setValueFormula(Formula) void
  + getFormula() Formula
}
class NumInvariant
class OROperator {
  + getFormula() String
}
class OperatorDecorator {
  # Formula left
  # Formula right
  + getLeft() Formula
  + getRight() Formula
}
class PlusOperator {
  + getFormula() String
}
class PreviousOperator {
  + getFormula() String
}
class QualId
class Quality {
  - boolean root
  - boolean exported
  - Actor actor
  + isExported() boolean
  + setRoot(boolean) void
  + setExported(boolean) void
  + isRoot() boolean
  + getFormula() Formula
}
class Task {
  - List~Effect~ effects
  - Actor actor
  + getActor() Actor
  + setActor(Actor) void
  + addEffect(Effect) void
  + setEffects(List~Effect~) void
  + isDeterministic() boolean
  + getEffects() List~Effect~
  + toString() String
}
class TaskId

ANDOperator  -->  OperatorDecorator 
Actor "1" *--> "effects *" Effect 
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
DomainPredicate  -->  Atom 
DomainVariable  -->  Atom 
EQOperator  -->  OperatorDecorator 
Effect "1" *--> "preFormula 1" Formula 
Effect  -->  NonDecompositionElement 
Effect "1" *--> "task 1" Task 
Element "1" *--> "representation 1" Atom 
GTEOperator  -->  OperatorDecorator 
GTOperator  -->  OperatorDecorator 
Goal "1" *--> "actor 1" Actor 
Goal  -->  DecompositionElement 
GoalId  -->  Atom 
Invariant  -->  Atom 
LTEOperator  -->  OperatorDecorator 
LTOperator  -->  OperatorDecorator 
MinusOperator  -->  OperatorDecorator 
Model "1" *--> "actors *" Actor 
Model "1" *--> "modelHeader 1" ModelHeader 
MultiplyOperator  -->  OperatorDecorator 
NEQOperator  -->  OperatorDecorator 
NOTOperator  -->  OperatorDecorator 
NegateOperator  -->  OperatorDecorator 
NonDecompositionElement  -->  Element 
NonDecompositionElement "1" *--> "valueFormula 1" Formula 
NumInvariant  -->  Atom 
OROperator  -->  OperatorDecorator 
OperatorDecorator "1" *--> "left 1" Formula 
OperatorDecorator  -->  Formula 
PlusOperator  -->  OperatorDecorator 
PreviousOperator  -->  OperatorDecorator 
QualId  -->  Atom 
Quality "1" *--> "actor 1" Actor 
Quality  -->  NonDecompositionElement 
Task "1" *--> "actor 1" Actor 
Task  -->  DecompositionElement 
Task "1" *--> "effects *" Effect 
TaskId  -->  Atom 
```