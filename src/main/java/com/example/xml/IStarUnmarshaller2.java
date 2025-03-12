package com.example.xml;

import com.example.objects.Model;
import com.example.xml.dto.ActorDto;
import com.example.xml.mappers.ModelMapper;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.io.InputStream;

/**
 * Main entry point for unmarshalling iStar-T XML to the domain model.
 * Uses the Facade pattern to provide a simple interface for clients.
 */
public class IStarUnmarshaller2 {

    private final ModelMapper modelMapper;

    /**
     * Constructor with default mapper.
     */
    public IStarUnmarshaller2() {
        this.modelMapper = new ModelMapper();
    }

    /**
     * Constructor with custom mapper for testing or extension.
     */
    public IStarUnmarshaller2(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     * Unmarshals an XML file to the domain model.
     *
     * @param xmlFile The XML file to unmarshall
     * @return The populated domain model
     * @throws JAXBException If there's an error during unmarshalling
     */
    public Model unmarshalToModel(File xmlFile) throws JAXBException {
        // Create JAXB context for ActorDto
        JAXBContext context = JAXBContext.newInstance(ActorDto.class);
        Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();

        // Unmarshal the XML file to ActorDto
        ActorDto actorDto = (ActorDto) jaxbUnmarshaller.unmarshal(xmlFile);

        // Map the DTO to the domain model
        return modelMapper.fromDto(actorDto);
    }

    /**
     * Unmarshals an XML input stream to the domain model.
     *
     * @param xmlStream The XML input stream to unmarshall
     * @return The populated domain model
     * @throws JAXBException If there's an error during unmarshalling
     */
    public Model unmarshalToModel(InputStream xmlStream) throws JAXBException {
        // Create JAXB context for ActorDto
        JAXBContext context = JAXBContext.newInstance(ActorDto.class);
        Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();

        // Unmarshal the XML stream to ActorDto
        ActorDto actorDto = (ActorDto) jaxbUnmarshaller.unmarshal(xmlStream);

        // Map the DTO to the domain model
        return modelMapper.fromDto(actorDto);
    }
}