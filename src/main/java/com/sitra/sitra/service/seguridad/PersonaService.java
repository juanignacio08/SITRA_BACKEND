package com.sitra.sitra.service.seguridad;

import com.sitra.sitra.entity.seguridad.PersonaEntity;
import com.sitra.sitra.expose.request.seguridad.PersonaRequest;
import com.sitra.sitra.expose.response.seguridad.PersonaResponse;

public interface PersonaService {
    PersonaResponse save(PersonaRequest request);
    PersonaResponse getByNumberDocument(String numberDocument);
    PersonaResponse update(PersonaRequest request);
    PersonaResponse deleteByNumberDocument(String numberDocument);
    boolean existsByNumberDocument(String numberDocument);
    PersonaEntity getPersonByNumberDocument(String numberDocument);
}
