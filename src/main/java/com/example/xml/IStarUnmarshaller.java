package com.example.xml;

import com.example.objects.*;
import com.example.xml.EnhancedXmlClasses.*;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.io.File;
import java.util.*;


public class IStarUnmarshaller {

    public Model unmarshalToModel(InputStream xmlStream) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(ActorXml.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (Model) unmarshaller.unmarshal(xmlStream);
    }

    /**
     * Unmarshals an XML file to the domain model
     */
    public Model unmarshalToModel(File xmlFile) throws JAXBException {
        // Create JAXB context and unmarshaller
        JAXBContext context = JAXBContext.newInstance(ActorXml.class);
        Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();

        // Unmarshal the XML file
        ActorXml actorXml = (ActorXml) jaxbUnmarshaller.unmarshal(xmlFile);

        // Create and populate the model
        Model model = new Model();

        // Create environment to store non-decomposition elements
        Environment environment = new Environment();

        // Convert actor XML to domain model
        Actor actor = convertActorToDomainModel(actorXml, environment);

        // Add actor to model
        List<Actor> actors = new ArrayList<>();
        actors.add(actor);
        model.setActors(actors);
        model.setEnvironment(environment);

        // Process cross-references and link objects
        processReferences(model);

        return model;
    }

    /**
     * Converts an ActorXml to an Actor domain object
     */
    private Actor convertActorToDomainModel(ActorXml actorXml, Environment environment) {
        Actor actor = new Actor();
        actor.setName(actorXml.getName());

        // Maps to store objects by their IDs for later reference resolution
        Map<String, Atom> atoms = new HashMap<>();
        Map<String, Goal> goals = new HashMap<>();
        Map<String, Task> tasks = new HashMap<>();
        Map<String, Effect> effects = new HashMap<>();
        Map<String, Quality> qualities = new HashMap<>();
        Map<String, Condition> conditions = new HashMap<>();
        Map<String, IndirectEffect> indirectEffects = new HashMap<>();

        // Process predicates and create atoms
        List<Atom> predicateAtoms = new ArrayList<>();
        if (actorXml.getPredicates() != null) {
            for (PredicateXml predXml : actorXml.getPredicates().getPredicate()) {
                Atom atom = new Atom();
                String id = predXml.getValue().trim();
                atom.setId(id);
                atom.setTitleText(predXml.getDescription());
                atoms.put(id, atom);
                predicateAtoms.add(atom);
            }
        }

        // Process PreBoxes (Conditions)
        List<Condition> conditionList = new ArrayList<>();
        if (actorXml.getPreBoxes() != null) {
            for (PreBoxXml preBoxXml : actorXml.getPreBoxes().getPreBox()) {
                Condition condition = new Condition();
                String id = preBoxXml.getName();

                // Set basic properties
                condition.setId(id);

                Atom representation = new Atom();
                representation.setId(id);
                representation.setTitleText(preBoxXml.getDescription());
                condition.setRepresentation(representation);

                // Process formula - convert preBox expression to Formula
                Formula formula = convertPreBoxToFormula(preBoxXml);
                condition.setValueFormula(formula);

                // Store in maps and lists
                conditions.put(id, condition);
                conditionList.add(condition);

                // Add to environment's nonDecompElements
                environment.addNonDecompElement(condition);
            }
        }

        // Process IndirectEffects
        List<IndirectEffect> indirectEffectList = new ArrayList<>();
        if (actorXml.getIndirectEffects() != null) {
            for (IndirectEffectXml effectXml : actorXml.getIndirectEffects().getIndirectEffect()) {
                IndirectEffect indirectEffect = new IndirectEffect();
                String id = effectXml.getName();

                // Set basic properties
                indirectEffect.setId(id);

                Atom representation = new Atom();
                representation.setId(id);
                representation.setTitleText(effectXml.getDescription());
                indirectEffect.setRepresentation(representation);

                // Set exported flag
                if (effectXml.getExported() != null) {
                    indirectEffect.setExported(effectXml.getExported());
                }

                // Process formula
                if (effectXml.getFormula() != null) {
                    Formula formula = convertFormulaXmlToFormula(effectXml.getFormula());
                    indirectEffect.setValueFormula(formula);
                }

                // Store in maps and lists
                indirectEffects.put(id, indirectEffect);
                indirectEffectList.add(indirectEffect);

                // Add to environment's nonDecompElements
                environment.addNonDecompElement(indirectEffect);
            }
        }

        // Process Qualities
        List<Quality> qualityList = new ArrayList<>();
        if (actorXml.getQualities() != null) {
            for (QualityXml qualityXml : actorXml.getQualities().getQuality()) {
                Quality quality = new Quality();
                String id = qualityXml.getName();

                // Set basic properties
                quality.setId(id);

                Atom representation = new Atom();
                representation.setId(id);
                representation.setTitleText(qualityXml.getDescription());
                quality.setRepresentation(representation);

                // Set root flag
                if (qualityXml.getRoot() != null) {
                    quality.setRoot(qualityXml.getRoot());
                }

                // Process formula
                if (qualityXml.getFormula() != null) {
                    Formula formula = convertFormulaXmlToFormula(qualityXml.getFormula());
                    quality.setValueFormula(formula);
                }

                // Store in maps and lists
                qualities.put(id, quality);
                qualityList.add(quality);

                // NEW: Add qualities to environment's nonDecompElements
                environment.addNonDecompElement(quality);
            }
        }

        // Process goals
        List<Goal> goalList = new ArrayList<>();
        if (actorXml.getGoals() != null) {
            for (GoalXml goalXml : actorXml.getGoals().getGoal()) {
                Goal goal = new Goal();
                String id = goalXml.getName();

                // Set basic properties
                goal.setId(id);

                Atom representation = new Atom();
                representation.setId(id);
                representation.setTitleText(goalXml.getDescription());
                goal.setRepresentation(representation);

                // Process refinement if exists
                if (goalXml.getRefinement() != null) {
                    RefinementXml refinementXml = goalXml.getRefinement();
                    goal.setDecompType(getDecompType(refinementXml.getType()));
                } else {
                    // Default to terminal
                    goal.setDecompType(DecompType.TERM);
                }

                // Set episode runs if specified
                if (goalXml.getEpisodeLength() != null) {
                    try {
                        goal.setRuns(Integer.parseInt(goalXml.getEpisodeLength()));
                    } catch (NumberFormatException e) {
                        // Default to 1 if not a valid number
                        goal.setRuns(1);
                    }
                }

                // Store in maps for later reference
                goals.put(id, goal);
                goalList.add(goal);
            }
        }

        // Process tasks
        List<Task> taskList = new ArrayList<>();
        if (actorXml.getTasks() != null) {
            for (TaskXml taskXml : actorXml.getTasks().getTask()) {
                Task task = new Task();
                String id = taskXml.getName();

                // Set basic properties
                task.setId(id);

                Atom representation = new Atom();
                representation.setId(id);
                representation.setTitleText(taskXml.getDescription());
                task.setRepresentation(representation);

                // Process effects if they exist
                List<Effect> taskEffects = new ArrayList<>();
                if (taskXml.getEffectGroup() != null) {
                    for (EffectXml effectXml : taskXml.getEffectGroup().getEffect()) {
                        Effect effect = new Effect();
                        String effectId = effectXml.getName();

                        // Basic properties
                        effect.setId(effectId);

                        Atom effectAtom = new Atom();
                        effectAtom.setId(effectId);
                        effectAtom.setTitleText(effectXml.getDescription());
                        effect.setRepresentation(effectAtom);

                        // Set probability
                        effect.setProbability(effectXml.getProbability());

                        // Set satisfying flag
                        effect.setSatisfying(effectXml.getSatisfying() == null || effectXml.getSatisfying());

                        // Process turnsTrue predicate(s)
                        if (effectXml.getTurnsTrue() != null && !effectXml.getTurnsTrue().isEmpty()) {
                            String predicate = effectXml.getTurnsTrue().get(0);
                            Atom atom = atoms.get(predicate);
                            if (atom != null) {
                                effect.setAtom(atom);
                            }
                        }

                        // Store effect in maps and lists
                        effects.put(effectId, effect);
                        taskEffects.add(effect);

                        // We're still adding effects to environment since they're NonDecompositionElements
                        environment.addNonDecompElement(effect);
                    }
                }

                task.setEffects(taskEffects);

                // Store in maps for later reference
                tasks.put(id, task);
                taskList.add(task);
            }
        }

        // Set collections in the actor
        actor.setGoals(goalList);
        actor.setTasks(taskList);
        actor.setQualities(qualityList);

        // Second pass - link goal refinements
        if (actorXml.getGoals() != null) {
            for (GoalXml goalXml : actorXml.getGoals().getGoal()) {
                if (goalXml.getRefinement() != null) {
                    Goal goal = goals.get(goalXml.getName());
                    RefinementXml refinementXml = goalXml.getRefinement();

                    // Process child goals
                    if (refinementXml.getChildGoal() != null) {
                        for (ChildRefXml childRef : refinementXml.getChildGoal()) {
                            Goal childGoal = goals.get(childRef.getRef());
                            if (childGoal != null) {
                                if (goal.getDecompType() == DecompType.AND) {
                                    goal.addANDChild(childGoal);
                                } else if (goal.getDecompType() == DecompType.OR) {
                                    goal.addORChild(childGoal);
                                }
                            }
                        }
                    }

                    // Process child tasks
                    if (refinementXml.getChildTask() != null) {
                        for (ChildRefXml childRef : refinementXml.getChildTask()) {
                            Task childTask = tasks.get(childRef.getRef());
                            if (childTask != null) {
                                if (goal.getDecompType() == DecompType.AND) {
                                    goal.addANDChild(childTask);
                                } else if (goal.getDecompType() == DecompType.OR) {
                                    goal.addORChild(childTask);
                                }
                            }
                        }
                    }
                }
            }
        }

        return actor;
    }

    /**
     * Convert a PreBoxXml to a Formula
     */
    private Formula convertPreBoxToFormula(PreBoxXml preBoxXml) {
        // Handle boolean constant
        if (preBoxXml.getBoolConst() != null) {
            return Formula.createBooleanFormula(Boolean.parseBoolean(preBoxXml.getBoolConst()));
        }

        // Handle boolean atom reference
        if (preBoxXml.getBoolAtom() != null) {
            return Formula.createConstantFormula(preBoxXml.getBoolAtom());
        }

        // Handle greater than comparison
        if (preBoxXml.getGt() != null) {
            GtXml gtXml = preBoxXml.getGt();

            // Get left operand
            String leftValue = null;
            if (gtXml.getLeft().getConst() != null) {
                leftValue = gtXml.getLeft().getConst();
            } else if (gtXml.getLeft().getNumAtom() != null) {
                leftValue = gtXml.getLeft().getNumAtom();
            }

            // Get right operand
            String rightValue = null;
            if (gtXml.getRight().getConst() != null) {
                rightValue = gtXml.getRight().getConst();
            } else if (gtXml.getRight().getNumAtom() != null) {
                rightValue = gtXml.getRight().getNumAtom();
            }

            if (leftValue != null && rightValue != null) {
                return new GTOperator(
                        Formula.createConstantFormula(leftValue),
                        Formula.createConstantFormula(rightValue)
                );
            }
        }

        // Default to a simple constant formula with the preBox name
        return Formula.createConstantFormula(preBoxXml.getName());
    }

    /**
     * Convert a FormulaXml to a Formula
     */
    private Formula convertFormulaXmlToFormula(FormulaXml formulaXml) {
        // Handle constant
        if (formulaXml.getConst() != null) {
            return Formula.createConstantFormula(formulaXml.getConst());
        }

        // Handle numAtom reference
        if (formulaXml.getNumAtom() != null) {
            return Formula.createConstantFormula(formulaXml.getNumAtom());
        }

        // Handle addition
        if (formulaXml.getAdd() != null) {
            AddXml addXml = formulaXml.getAdd();

            // If there are constants
            if (addXml.getConst() != null && !addXml.getConst().isEmpty()) {
                if (addXml.getConst().size() >= 2) {
                    // Create a sum of the first two constants
                    Formula left = Formula.createConstantFormula(addXml.getConst().get(0));
                    Formula right = Formula.createConstantFormula(addXml.getConst().get(1));
                    Formula result = new PlusOperator(left, right);

                    // Add any remaining constants
                    for (int i = 2; i < addXml.getConst().size(); i++) {
                        result = new PlusOperator(
                                result,
                                Formula.createConstantFormula(addXml.getConst().get(i))
                        );
                    }

                    return result;
                } else if (addXml.getConst().size() == 1) {
                    return Formula.createConstantFormula(addXml.getConst().get(0));
                }
            }

            // If there are atom references
            if (addXml.getNumAtom() != null && !addXml.getNumAtom().isEmpty()) {
                // Create formulas for atoms
                List<Formula> atomFormulas = new ArrayList<>();
                for (String atomName : addXml.getNumAtom()) {
                    atomFormulas.add(Formula.createConstantFormula(atomName));
                }

                // Add constants to the atom formulas if present
                if (addXml.getConst() != null && !addXml.getConst().isEmpty()) {
                    for (String constVal : addXml.getConst()) {
                        atomFormulas.add(Formula.createConstantFormula(constVal));
                    }
                }

                // Combine all formulas with plus operators
                if (atomFormulas.size() >= 2) {
                    Formula result = new PlusOperator(atomFormulas.get(0), atomFormulas.get(1));
                    for (int i = 2; i < atomFormulas.size(); i++) {
                        result = new PlusOperator(result, atomFormulas.get(i));
                    }
                    return result;
                } else if (atomFormulas.size() == 1) {
                    return atomFormulas.get(0);
                }
            }

            // Handle subtraction within addition
            if (addXml.getSubtract() != null) {
                SubtractXml subtractXml = addXml.getSubtract();

                // Get left operand
                String leftValue = null;
                if (subtractXml.getLeft().getConst() != null) {
                    leftValue = subtractXml.getLeft().getConst();
                } else if (subtractXml.getLeft().getNumAtom() != null) {
                    leftValue = subtractXml.getLeft().getNumAtom();
                }

                // Get right operand
                String rightValue = null;
                if (subtractXml.getRight().getConst() != null) {
                    rightValue = subtractXml.getRight().getConst();
                } else if (subtractXml.getRight().getNumAtom() != null) {
                    rightValue = subtractXml.getRight().getNumAtom();
                }

                if (leftValue != null && rightValue != null) {
                    return new MinusOperator(
                            Formula.createConstantFormula(leftValue),
                            Formula.createConstantFormula(rightValue)
                    );
                }
            }
        }

        // Handle direct subtraction
        if (formulaXml.getSubtract() != null) {
            SubtractXml subtractXml = formulaXml.getSubtract();

            // Get left operand
            String leftValue = null;
            if (subtractXml.getLeft().getConst() != null) {
                leftValue = subtractXml.getLeft().getConst();
            } else if (subtractXml.getLeft().getNumAtom() != null) {
                leftValue = subtractXml.getLeft().getNumAtom();
            }

            // Get right operand
            String rightValue = null;
            if (subtractXml.getRight().getConst() != null) {
                rightValue = subtractXml.getRight().getConst();
            } else if (subtractXml.getRight().getNumAtom() != null) {
                rightValue = subtractXml.getRight().getNumAtom();
            }

            if (leftValue != null && rightValue != null) {
                return new MinusOperator(
                        Formula.createConstantFormula(leftValue),
                        Formula.createConstantFormula(rightValue)
                );
            }
        }

        // Default to a simple constant formula
        return Formula.createConstantFormula("Default Formula");
    }

    /**
     * Process references between objects after all objects have been created
     */
    private void processReferences(Model model) {
        // Process parent-child relationships in decomposition elements
        for (Actor actor : model.getActors()) {
            // Process goal hierarchies
            for (Goal goal : actor.getGoals()) {
                if (goal instanceof DecompositionElement) {
                    DecompositionElement decomp = goal;
                    if (decomp.getChildren() != null) {
                        for (DecompositionElement child : decomp.getChildren()) {
                            // Set parent-child relationships
                            child.setParent(decomp);
                        }
                    }
                }
            }

            // Process task-effect relationships
            for (Task task : actor.getTasks()) {
                if (task.getEffects() != null) {
                    for (Effect effect : task.getEffects()) {
                        effect.setTask(task);
                    }
                }
            }
        }
    }

    /**
     * Maps refinement type string to DecompType enum
     */
    private DecompType getDecompType(String type) {
        if ("AND".equalsIgnoreCase(type)) {
            return DecompType.AND;
        } else if ("OR".equalsIgnoreCase(type)) {
            return DecompType.OR;
        } else {
            return DecompType.TERM;
        }
    }
}