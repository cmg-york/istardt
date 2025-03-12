package com.example.xml.mappers;

import com.example.objects.Condition;
import com.example.objects.Environment;
import com.example.objects.Formula;
import com.example.objects.builders.ConditionBuilder;
import com.example.xml.dto.PreBoxDto;
import com.example.xml.util.FormulaFactory;
import java.util.List;
import java.util.Map;

/**
 * Mapper for converting from PreBoxDto to Condition.
 */
public class PreBoxMapper {

    /**
     * Maps a list of PreBoxDto objects to a list of Condition objects.
     *
     * @param preBoxDtos The preBox DTOs from XML
     * @param conditionList The list to populate with created conditions
     * @param conditionsMap Map to store conditions by ID for later reference
     * @param environment The environment to add conditions to
     */
    public void fromDtoList(List<PreBoxDto> preBoxDtos, List<Condition> conditionList,
                            Map<String, Condition> conditionsMap, Environment environment) {
        if (preBoxDtos == null) {
            return;
        }

        for (PreBoxDto preBoxDto : preBoxDtos) {
            Condition condition = fromDto(preBoxDto);
            String id = condition.getId();

            conditionsMap.put(id, condition);
            conditionList.add(condition);

            // Add to environment
            environment.addNonDecompElement(condition);
        }
    }

    /**
     * Maps a single PreBoxDto to a Condition.
     *
     * @param preBoxDto The preBox DTO from XML
     * @return The created Condition
     */
    public Condition fromDto(PreBoxDto preBoxDto) {
        // Use the builder pattern to create the condition
        ConditionBuilder builder = new ConditionBuilder()
                .withId(preBoxDto.getName())
                .withRepresentation(preBoxDto.getDescription());

        // Process formula
        Formula formula = FormulaFactory.createFromPreBox(preBoxDto);
        if (formula != null) {
            builder.withFormula(formula);
        }

        return builder.build();
    }
}