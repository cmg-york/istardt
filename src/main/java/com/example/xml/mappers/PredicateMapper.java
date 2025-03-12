package com.example.xml.mappers;

import com.example.objects.Atom;
import com.example.xml.dto.PredicateDto;
import java.util.List;
import java.util.Map;

/**
 * Mapper for converting from PredicateDto to Atom.
 */
public class PredicateMapper {

    /**
     * Maps a list of PredicateDto objects to a list of Atom objects.
     *
     * @param predicateDtos The predicate DTOs from XML
     * @param predicateAtoms The list to populate with created atoms
     * @param atomsMap Map to store atoms by ID for later reference
     */
    public void fromDtoList(List<PredicateDto> predicateDtos, List<Atom> predicateAtoms,
                            Map<String, Atom> atomsMap) {
        if (predicateDtos == null) {
            return;
        }

        for (PredicateDto predDto : predicateDtos) {
            Atom atom = fromDto(predDto);
            String id = atom.getId();

            atomsMap.put(id, atom);
            predicateAtoms.add(atom);
        }
    }

    /**
     * Maps a single PredicateDto to an Atom.
     *
     * @param predicateDto The predicate DTO from XML
     * @return The created Atom
     */
    public Atom fromDto(PredicateDto predicateDto) {
        Atom atom = new Atom();
        String id = predicateDto.getValue().trim();
        atom.setId(id);
        atom.setTitleText(predicateDto.getDescription());

        return atom;
    }
}