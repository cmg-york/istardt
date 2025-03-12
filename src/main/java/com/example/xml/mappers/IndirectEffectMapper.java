package com.example.xml.mappers;

import com.example.objects.Environment;
import com.example.objects.Formula;
import com.example.objects.IndirectEffect;
import com.example.objects.builders.IndirectEffectBuilder;
import com.example.xml.dto.IndirectEffectDto;
import com.example.xml.util.FormulaFactory;
import java.util.List;
import java.util.Map;

/**
 * Mapper for converting from IndirectEffectDto to IndirectEffect.
 */
public class IndirectEffectMapper {

    /**
     * Maps a list of IndirectEffectDto objects to a list of IndirectEffect objects.
     *
     * @param effectDtos The indirect effect DTOs from XML
     * @param indirectEffectList The list to populate with created indirect effects
     * @param indirectEffectsMap Map to store indirect effects by ID for later reference
     * @param environment The environment to add indirect effects to
     */
    public void fromDtoList(List<IndirectEffectDto> effectDtos, List<IndirectEffect> indirectEffectList,
                            Map<String, IndirectEffect> indirectEffectsMap, Environment environment) {
        if (effectDtos == null) {
            return;
        }

        for (IndirectEffectDto effectDto : effectDtos) {
            IndirectEffect indirectEffect = fromDto(effectDto);
            String id = indirectEffect.getId();

            indirectEffectsMap.put(id, indirectEffect);
            indirectEffectList.add(indirectEffect);

            // Add to environment
            environment.addNonDecompElement(indirectEffect);
        }
    }

    /**
     * Maps a single IndirectEffectDto to an IndirectEffect.
     *
     * @param effectDto The indirect effect DTO from XML
     * @return The created IndirectEffect
     */
    public IndirectEffect fromDto(IndirectEffectDto effectDto) {
        // Use the builder pattern to create the indirect effect
        IndirectEffectBuilder builder = new IndirectEffectBuilder()
                .withId(effectDto.getName())
                .withRepresentation(effectDto.getDescription())
                .isExported(effectDto.getExported() != null ? effectDto.getExported() : false);

        // Process formula
        if (effectDto.getFormula() != null) {
            Formula formula = FormulaFactory.createFromFormula(effectDto.getFormula());
            if (formula != null) {
                builder.withFormula(formula);
            }
        }

        return builder.build();
    }
}