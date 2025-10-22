package com.sitra.sitra.service.seguridad;

import com.sitra.sitra.entity.seguridad.PersonaEntity;
import com.sitra.sitra.exceptions.BadRequestException;
import com.sitra.sitra.exceptions.DuplicateKeyError;
import com.sitra.sitra.exceptions.NotFoundException;
import com.sitra.sitra.expose.request.seguridad.PersonaRequest;
import com.sitra.sitra.expose.response.seguridad.PersonaResponse;
import com.sitra.sitra.repository.seguridad.PersonaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
public class PersonaServiceImpl implements PersonaService{

    private final PersonaRepository personaRepository;

    private String context;

    @Override
    public PersonaResponse save(PersonaRequest request) {
        context = "savePerson";
        log.info("Registrando una persona. [ CONTEXT : {} ]", context);

        if (existsByNumberDocument(request.getNumeroDocumentoIdentidad())) new DuplicateKeyError("Error! Numero de documento duplicado!");

        PersonaEntity entity = PersonaRequest.toEntity.apply(request);

        PersonaEntity save = personaRepository.save(entity);
        return PersonaResponse.toResponse.apply(save);
    }

    @Override
    public PersonaResponse getByNumberDocument(String numberDocument) {
        context = "getPerson";
        log.info("Obteniendo una persona. [ PERSONA : {} | CONTEXTO : {} ]", numberDocument, context);

        PersonaEntity entity = getPersonByNumberDocument(numberDocument);

        return PersonaResponse.toResponse.apply(entity);
    }

    @Override
    public PersonaResponse update(PersonaRequest request) {
        return null;
    }

    @Override
    public PersonaResponse deleteByNumberDocument(String numberDocument) {
        return null;
    }

    @Override
    public boolean existsByNumberDocument(String numberDocument) {
        if (numberDocument == null) throw new BadRequestException("Numero de documento incorrecto. [ Persona ]");

        return personaRepository.existsPersona(numberDocument);
    }

    @Override
    public PersonaEntity getPersonByNumberDocument(String numberDocument) {
        if (numberDocument == null) throw new BadRequestException("Numero de documento incorrecto. [ Persona ]");

        return personaRepository.getByNumberDocument(numberDocument)
                .orElseThrow(() -> new NotFoundException("Registro no encontrado [ PERSONA ]"));
    }


}
