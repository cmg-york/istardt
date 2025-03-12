package com.example.xml.mappers;

import com.example.objects.*;
import com.example.objects.builders.*;
import com.example.xml.dto.*;
import com.example.xml.util.FormulaFactory;

import java.util.*;

/**
 * Mapper for converting from ActorDto to Actor domain object.
 * Coordinates other mappers for elements inside the actor.
 */
public class ActorMapper {

    // Individual element mappers
    private final PredicateMapper predicateMapper;
    private final PreBoxMapper preBoxMapper;
    private final QualityMapper qualityMapper;
    private final IndirectEffectMapper indirectEffectMapper;
    private final GoalMapper goalMapper;
    private final TaskMapper taskMapper;

    // Store objects temporarily during mapping
    private final Map<String, Atom> atomsMap = new HashMap<>();
    private final Map<String, Goal> goalsMap = new HashMap<>();
    private final Map<String, Task> tasksMap = new HashMap<>();
    private final Map<String, Effect> effectsMap = new HashMap<>();
    private final Map<String, Quality> qualitiesMap = new HashMap<>();
    private final Map<String, Condition> conditionsMap = new HashMap<>();
    private final Map<String, IndirectEffect> indirectEffectsMap = new HashMap<>();

    /**
     * Default constructor.
     */
    public ActorMapper() {
        this.predicateMapper = new PredicateMapper();
        this.preBoxMapper = new PreBoxMapper();
        this.qualityMapper = new QualityMapper();
        this.indirectEffectMapper = new IndirectEffectMapper();
        this.goalMapper = new GoalMapper();
        this.taskMapper = new TaskMapper();
    }

    /**
     * Maps an ActorDto to an Actor domain object.
     *
     * @param actorDto The actor DTO from XML
     * @param environment The environment to populate
     * @return The Actor domain object
     */
    public Actor fromDto(ActorDto actorDto, Environment environment) {
        // Create the actor
        Actor actor = new Actor();
        actor.setName(actorDto.getName());

        // Clear any existing maps from previous mappings
        clearMaps();

        // Map predicates to atoms
        List<Atom> predicateAtoms = new ArrayList<>();
        if (actorDto.getPredicates() != null) {
            predicateMapper.fromDtoList(actorDto.getPredicates().getPredicate(), predicateAtoms, atomsMap);
        }

        // Map preBoxes to conditions
        List<Condition> conditionList = new ArrayList<>();
        if (actorDto.getPreBoxes() != null && actorDto.getPreBoxes().getPreBox() != null) {
            preBoxMapper.fromDtoList(actorDto.getPreBoxes().getPreBox(), conditionList, conditionsMap, environment);
        }

        // Map indirect effects
        List<IndirectEffect> indirectEffectList = new ArrayList<>();
        if (actorDto.getIndirectEffects() != null && actorDto.getIndirectEffects().getIndirectEffect() != null) {
            indirectEffectMapper.fromDtoList(actorDto.getIndirectEffects().getIndirectEffect(),
                    indirectEffectList, indirectEffectsMap, environment);
        }

        // Map qualities
        List<Quality> qualityList = new ArrayList<>();
        if (actorDto.getQualities() != null && actorDto.getQualities().getQuality() != null) {
            qualityMapper.fromDtoList(actorDto.getQualities().getQuality(), qualityList, qualitiesMap, environment);
        }

        // Map goals
        List<Goal> goalList = new ArrayList<>();
        if (actorDto.getGoals() != null && actorDto.getGoals().getGoal() != null) {
            goalMapper.fromDtoList(actorDto.getGoals().getGoal(), goalList, goalsMap);
        }

        // Map tasks
        List<Task> taskList = new ArrayList<>();
        if (actorDto.getTasks() != null && actorDto.getTasks().getTask() != null) {
            taskMapper.fromDtoList(actorDto.getTasks().getTask(), taskList, tasksMap, effectsMap,
                    atomsMap, environment);
        }

        // Set collections in the actor
        actor.setGoals(goalList);
        actor.setTasks(taskList);
        actor.setQualities(qualityList);

        // Link goal refinements
        if (actorDto.getGoals() != null && actorDto.getGoals().getGoal() != null) {
            linkGoalRefinements(actorDto.getGoals().getGoal());
        }

        return actor;
    }

    /**
     * Links goal refinements after all goals and tasks have been created.
     */
    private void linkGoalRefinements(List<GoalDto> goalDtos) {
        for (GoalDto goalDto : goalDtos) {
            if (goalDto.getRefinement() != null) {
                Goal goal = goalsMap.get(goalDto.getName());
                if (goal == null) {
                    continue;
                }

                RefinementDto refinementDto = goalDto.getRefinement();

                // Process child goals
                if (refinementDto.getChildGoal() != null) {
                    for (ChildRefDto childRefDto : refinementDto.getChildGoal()) {
                        Goal childGoal = goalsMap.get(childRefDto.getRef());
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
                if (refinementDto.getChildTask() != null) {
                    for (ChildRefDto childRefDto : refinementDto.getChildTask()) {
                        Task childTask = tasksMap.get(childRefDto.getRef());
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

    /**
     * Process references between objects after all objects have been created.
     */
    public void processReferences(Model model) {
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
     * Clears all maps for a new mapping operation.
     */
    private void clearMaps() {
        atomsMap.clear();
        goalsMap.clear();
        tasksMap.clear();
        effectsMap.clear();
        qualitiesMap.clear();
        conditionsMap.clear();
        indirectEffectsMap.clear();
    }
}