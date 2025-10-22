package com.sitra.sitra.service.seguridad;

import com.sitra.sitra.entity.seguridad.PersonaEntity;
import com.sitra.sitra.exceptions.BadRequestException;
import com.sitra.sitra.exceptions.DuplicateKeyError;
import com.sitra.sitra.exceptions.NotFoundException;
import com.sitra.sitra.expose.request.seguridad.PersonaRequest;
import com.sitra.sitra.expose.response.seguridad.PersonaResponse;
import com.sitra.sitra.expose.util.SecurityUtil;
import com.sitra.sitra.repository.seguridad.PersonaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
public class PersonaServiceImpl implements PersonaService{

    private final PersonaRepository personaRepository;

    private String context;

    @Override
    @Transactional
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
    @Transactional
    public PersonaResponse update(PersonaRequest request) {
        context = "updatePerson";
        log.info("Modificando una persona. [ PERSONA : {} | CONTEXTO : {} ]", request.getNumeroDocumentoIdentidad(), context);

        PersonaEntity entity = getPersonByNumberDocument(request.getNumeroDocumentoIdentidad());

        PersonaRequest.toUpdate(request, entity);

        PersonaEntity save = personaRepository.save(entity);

        return PersonaResponse.toResponse.apply(save);
    }

    @Override
    @Transactional
    public PersonaResponse deleteByNumberDocument(String numberDocument) {
        context = "deletePerson";
        log.info("Eliminando una persona. [ PERSONA : {} | CONTEXTO : {} ]", numberDocument, context);

        PersonaEntity entity = getPersonByNumberDocument(numberDocument);
        entity.setEliminado(true);
        entity.setActualizadoPor(SecurityUtil.getCurrentUserId());
        entity.setFechaActualizacion(LocalDateTime.now());

        PersonaEntity save = personaRepository.save(entity);

        return PersonaResponse.toResponse.apply(save);
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
