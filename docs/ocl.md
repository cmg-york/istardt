# Object Constraint Language (OCL) Constraints

## 1. Element Descriptions
```ocl
-- Warning: All elements should have descriptions
context Predicate
inv hasDescription: 
    if self.description.oclIsUndefined() then
        self.oclAsType(OclAny).oclWarning('Element Predicate with content "' + self.content + '" has no description')
    else
        true
    endif

context PreBox
inv hasDescription: 
    if self.description.oclIsUndefined() then
        self.oclAsType(OclAny).oclWarning('Element PreBox with name "' + self.name + '" has no description')
    else
        true
    endif

context Quality
inv hasDescription: 
    if self.description.oclIsUndefined() then
        self.oclAsType(OclAny).oclWarning('Element Quality with name "' + self.name + '" has no description')
    else
        true
    endif

context Goal
inv hasDescription: 
    if self.description.oclIsUndefined() then
        self.oclAsType(OclAny).oclWarning('Element Goal with name "' + self.name + '" has no description')
    else
        true
    endif

context Task
inv hasDescription: 
    if self.description.oclIsUndefined() then
        self.oclAsType(OclAny).oclWarning('Element Task with name "' + self.name + '" has no description')
    else
        true
    endif

context Effect
inv hasDescription: 
    if self.description.oclIsUndefined() then
        self.oclAsType(OclAny).oclWarning('Element Effect with name "' + self.name + '" has no description')
    else
        true
    endif

context IndirectEffect
inv hasDescription: 
    if self.description.oclIsUndefined() then
        self.oclAsType(OclAny).oclWarning('Element IndirectEffect with name "' + self.name + '" has no description')
    else
        true
    endif
```

## 2. Root Goal Episode Length
```ocl
-- Warning: Root goals should have episodeLength
context Goal
inv rootGoalHasEpisodeLength:
    if self.root = true and self.episodeLength.oclIsUndefined() then
        self.oclAsType(OclAny).oclWarning('Root goal "' + self.name + '" has no episodeLength')
    else
        true
    endif

-- Error: EpisodeLength must be a positive integer
context Goal
inv validEpisodeLength:
    if self.root = true and not self.episodeLength.oclIsUndefined() then
        let episodeInt : Integer = self.episodeLength.toInteger() in
        episodeInt > 0
    else
        true
    endif
```

## 3. Goal Refinement Structure
```ocl
-- Warning: Goals should have refinements (leaf goal warning)
context Goal
inv hasRefinement:
    if self.refinement.oclIsUndefined() then
        self.oclAsType(OclAny).oclWarning('Goal "' + self.name + '" has no refinement (leaf-level goal)')
    else
        true
    endif

-- Error: AND-refinements must have at least 2 children
context Refinement
inv andRefinementMinimumChildren:
    if self.type = 'AND' then
        self.childRef->size() >= 2
    else
        true
    endif

-- Error: OR-refinements must have at least 1 child
context Refinement
inv orRefinementMinimumChildren:
    if self.type = 'OR' then
        self.childRef->size() >= 1
    else
        true
    endif
```

## 4. Task References
```ocl
-- Warning: Tasks should be referenced exactly once
context Task
inv referencedExactlyOnce:
    let refCount : Integer = self.actor.goals.goal.refinement.childTask->select(ct | ct.ref = self.name)->size() in
    if refCount <> 1 then
        self.oclAsType(OclAny).oclWarning('Task "' + self.name + '" is referenced ' + refCount.toString() + ' time(s); must be exactly once')
    else
        true
    endif
```

## 5. Child References Validation
```ocl
-- Error: Child references must exist
context ChildGoal
inv referencesExistingGoal:
    self.actor.goals.goal->exists(g | g.name = self.ref)

context ChildTask
inv referencesExistingTask:
    self.actor.tasks.task->exists(t | t.name = self.ref)
```

## 6. Effect Validation
```ocl
-- Error: Effect probability must be between 0 and 1
context Effect
inv validProbability:
    self.probability >= 0.0 and self.probability <= 1.0

-- Warning: Effect satisfying attribute should be defined
context Effect
inv hasSatisfyingAttribute:
    if self.satisfying.oclIsUndefined() then
        self.oclAsType(OclAny).oclWarning('Effect "' + self.name + '" has no satisfying attribute. Default is "true"')
    else
        true
    endif

-- Error: Sum of probabilities in an EffectGroup must be 1.0
context EffectGroup
inv probabilitySumToOne:
    let sum : Real = self.effect->collect(e | e.probability)->sum() in
    sum.round(3) = 1.000
```

## 7. Predicate Reference Validation
```ocl
-- Error: TurnsTrue and TurnsFalse must reference defined predicates
context TurnsTrue
inv referencesExistingPredicate:
    self.actor.predicates.predicate->exists(p | p.content = self.reference)

context TurnsFalse
inv referencesExistingPredicate:
    self.actor.predicates.predicate->exists(p | p.content = self.reference)

-- Error: Pre/NPR must reference defined preBox, goal, task, or effect
context Pre
inv referencesValidElement:
    self.actor.preBoxes.preBox->exists(pb | pb.name = self.reference) or
    self.actor.goals.goal->exists(g | g.name = self.reference) or
    self.actor.tasks.task->exists(t | t.name = self.reference) or
    self.actor.tasks.task.effectGroup.effect->exists(e | e.name = self.reference)

context NPr
inv referencesValidElement:
    self.actor.preBoxes.preBox->exists(pb | pb.name = self.reference) or
    self.actor.goals.goal->exists(g | g.name = self.reference) or
    self.actor.tasks.task->exists(t | t.name = self.reference) or
    self.actor.tasks.task.effectGroup.effect->exists(e | e.name = self.reference)
```

## 8. Expression Reference Validation
```ocl
-- Error: Atoms in boolean expressions must be declared predicates
context BoolAtom
inv referencesExistingPredicate:
    self.actor.predicates.predicate->exists(p | p.content = self.value)

-- Error: Atoms in numeric expressions must be declared predicates or qualities
context NumAtom
inv referencesExistingElement:
    self.actor.predicates.predicate->exists(p | p.content = self.value) or
    self.actor.qualities.quality->exists(q | q.name = self.value)
```

## 9. Root Quality Validation
```ocl
-- Error: There must be exactly one root quality per actor
context Actor
inv exactlyOneRootQuality:
    self.qualities.quality->select(q | q.root = true)->size() = 1
```

## 10. Custom Logical and Mathematical Constraints
```ocl
-- All childGoal references must point to distinct goals (no cycles)
context Refinement
inv noCyclicGoalReferences:
    self.childGoal->forAll(cg1, cg2 | 
        cg1 <> cg2 implies cg1.ref <> cg2.ref
    )

-- Pre and NPr cannot reference the same element in the same context
context Goal
inv noContradictoryPreconditions:
    self.pre->forAll(p1 |
        not self.npr->exists(p2 | p1.reference = p2.reference)
    )

context Task
inv noContradictoryPreconditions:
    self.pre->forAll(p1 |
        not self.npr->exists(p2 | p1.reference = p2.reference)
    )

-- Each refinement type must be either 'AND' or 'OR'
context Refinement
inv validRefinementType:
    self.type = 'AND' or self.type = 'OR'

-- Boolean constants must be either 'true' or 'false'
context BoolConst
inv validBooleanConstant:
    self.value = 'true' or self.value = 'false'
```