package com.example.xml.mappers;

import com.example.objects.DecompType;
import com.example.objects.Goal;
import com.example.objects.builders.GoalBuilder;
import com.example.xml.dto.GoalDto;
import com.example.xml.dto.RefinementDto;
import java.util.List;
import java.util.Map;

/**
 * Mapper for converting from GoalDto to Goal.
 */
public class GoalMapper {

    /**
     * Maps a list of GoalDto objects to a list of Goal objects.
     *
     * @param goalDtos The goal DTOs from XML
     * @param goalList The list to populate with created goals
     * @param goalsMap Map to store goals by ID for later reference
     */
    public void fromDtoList(List<GoalDto> goalDtos, List<Goal> goalList, Map<String, Goal> goalsMap) {
        if (goalDtos == null) {
            return;
        }

        for (GoalDto goalDto : goalDtos) {
            Goal goal = fromDto(goalDto);
            String id = goal.getId();

            goalsMap.put(id, goal);
            goalList.add(goal);
        }
    }

    /**
     * Maps a single GoalDto to a Goal.
     *
     * @param goalDto The goal DTO from XML
     * @return The created Goal
     */
    public Goal fromDto(GoalDto goalDto) {
        // Use the builder pattern to create the goal
        GoalBuilder builder = new GoalBuilder()
                .withId(goalDto.getName())
                .withRepresentation(goalDto.getDescription())
                .isRoot(goalDto.getRoot() != null ? goalDto.getRoot() : false);

        // Set episode runs if specified
        if (goalDto.getEpisodeLength() != null) {
            try {
                builder.withRuns(Integer.parseInt(goalDto.getEpisodeLength()));
            } catch (NumberFormatException e) {
                // Default to 1 if not a valid number
                builder.withRuns(1);
            }
        }

        // Set decomposition type based on refinement if it exists
        if (goalDto.getRefinement() != null) {
            RefinementDto refinementDto = goalDto.getRefinement();
            builder.withDecompType(getDecompType(refinementDto.getType()));
        } else {
            // Default to terminal if no refinement
            builder.withDecompType(DecompType.TERM);
        }

        // Add preconditions
        if (goalDto.getPre() != null) {
            for (String pre : goalDto.getPre()) {
                builder.withPrecondition(pre);
            }
        }

        // Add negative preconditions
        if (goalDto.getNpr() != null) {
            for (String npr : goalDto.getNpr()) {
                builder.withNegPrecondition(npr);
            }
        }

        return builder.build();
    }

    /**
     * Maps refinement type string to DecompType enum.
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