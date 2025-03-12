package com.example.xml.mappers;

import com.example.objects.Environment;
import com.example.objects.Formula;
import com.example.objects.Quality;
import com.example.objects.builders.QualityBuilder;
import com.example.xml.dto.QualityDto;
import com.example.xml.util.FormulaFactory;
import java.util.List;
import java.util.Map;

/**
 * Mapper for converting from QualityDto to Quality.
 */
public class QualityMapper {

    /**
     * Maps a list of QualityDto objects to a list of Quality objects.
     *
     * @param qualityDtos The quality DTOs from XML
     * @param qualityList The list to populate with created qualities
     * @param qualitiesMap Map to store qualities by ID for later reference
     * @param environment The environment to add qualities to
     */
    public void fromDtoList(List<QualityDto> qualityDtos, List<Quality> qualityList,
                            Map<String, Quality> qualitiesMap, Environment environment) {
        if (qualityDtos == null) {
            return;
        }

        for (QualityDto qualityDto : qualityDtos) {
            Quality quality = fromDto(qualityDto);
            String id = quality.getId();

            qualitiesMap.put(id, quality);
            qualityList.add(quality);

            // Add to environment
            environment.addNonDecompElement(quality);
        }
    }

    /**
     * Maps a single QualityDto to a Quality.
     *
     * @param qualityDto The quality DTO from XML
     * @return The created Quality
     */
    public Quality fromDto(QualityDto qualityDto) {
        // Use the builder pattern to create the quality
        QualityBuilder builder = new QualityBuilder()
                .withId(qualityDto.getName())
                .withRepresentation(qualityDto.getDescription())
                .isRoot(qualityDto.getRoot() != null ? qualityDto.getRoot() : false);

        // Process formula
        if (qualityDto.getFormula() != null) {
            Formula formula = FormulaFactory.createFromFormula(qualityDto.getFormula());
            if (formula != null) {
                builder.withFormula(formula);
            }
        }

        return builder.build();
    }
}