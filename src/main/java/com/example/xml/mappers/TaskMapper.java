package com.example.xml.mappers;

import com.example.objects.*;
import com.example.objects.builders.EffectBuilder;
import com.example.objects.builders.TaskBuilder;
import com.example.xml.dto.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Mapper for converting from TaskDto to Task and EffectDto to Effect.
 */
public class TaskMapper {

    /**
     * Maps a list of TaskDto objects to a list of Task objects.
     *
     * @param taskDtos The task DTOs from XML
     * @param taskList The list to populate with created tasks
     * @param tasksMap Map to store tasks by ID for later reference
     * @param effectsMap Map to store effects by ID for later reference
     * @param atomsMap Map of atoms by ID for reference
     * @param environment The environment to add effects to
     */
    public void fromDtoList(List<TaskDto> taskDtos, List<Task> taskList, Map<String, Task> tasksMap,
                            Map<String, Effect> effectsMap, Map<String, Atom> atomsMap, Environment environment) {
        if (taskDtos == null) {
            return;
        }

        for (TaskDto taskDto : taskDtos) {
            Task task = fromDto(taskDto, effectsMap, atomsMap, environment);
            String id = task.getId();

            tasksMap.put(id, task);
            taskList.add(task);
        }
    }

    /**
     * Maps a single TaskDto to a Task.
     *
     * @param taskDto The task DTO from XML
     * @param effectsMap Map to store effects by ID for later reference
     * @param atomsMap Map of atoms by ID for reference
     * @param environment The environment to add effects to
     * @return The created Task
     */
    public Task fromDto(TaskDto taskDto, Map<String, Effect> effectsMap,
                        Map<String, Atom> atomsMap, Environment environment) {
        // Use the builder pattern to create the task
        TaskBuilder builder = new TaskBuilder()
                .withId(taskDto.getName())
                .withRepresentation(taskDto.getDescription());

        // Add preconditions
        if (taskDto.getPre() != null) {
            for (String pre : taskDto.getPre()) {
                builder.withPrecondition(pre);
            }
        }

        // Add negative preconditions
        if (taskDto.getNpr() != null) {
            for (String npr : taskDto.getNpr()) {
                builder.withNegPrecondition(npr);
            }
        }

        Task task = builder.build();

        // Process effects if they exist
        if (taskDto.getEffectGroup() != null && taskDto.getEffectGroup().getEffect() != null) {
            for (EffectDto effectDto : taskDto.getEffectGroup().getEffect()) {
                Effect effect = fromEffectDto(effectDto, atomsMap);

                // Add the effect to the task
                task.addEffect(effect);

                // Store the effect
                effectsMap.put(effect.getId(), effect);

                // Add to environment
                environment.addNonDecompElement(effect);
            }
        }

        return task;
    }

    /**
     * Maps a single EffectDto to an Effect.
     *
     * @param effectDto The effect DTO from XML
     * @param atomsMap Map of atoms by ID for reference
     * @return The created Effect
     */
    private Effect fromEffectDto(EffectDto effectDto, Map<String, Atom> atomsMap) {
        // Use the builder pattern to create the effect
        EffectBuilder builder = new EffectBuilder()
                .withId(effectDto.getName())
                .withRepresentation(effectDto.getDescription())
                .withProbability(effectDto.getProbability())
                .isSatisfying(effectDto.getSatisfying() == null || effectDto.getSatisfying());

        // Process turnsTrue predicate(s)
        if (effectDto.getTurnsTrue() != null) {
            for (String predicate : effectDto.getTurnsTrue()) {
                builder.withTurnsTrue(predicate);
                Atom atom = atomsMap.get(predicate);
                if (atom != null) {
                    builder.withAtom(atom);
                }
            }
        }

        // Process turnsFalse predicate(s)
        if (effectDto.getTurnsFalse() != null) {
            for (String predicate : effectDto.getTurnsFalse()) {
                builder.withTurnsFalse(predicate);
            }
        }

        // Process preconditions
        if (effectDto.getPre() != null) {
            for (String pre : effectDto.getPre()) {
                builder.withPrecondition(pre);
            }
        }

        // Process negative preconditions
        if (effectDto.getNpr() != null) {
            for (String npr : effectDto.getNpr()) {
                builder.withNegPrecondition(npr);
            }
        }

        return builder.build();
    }
}