package com.sitra.sitra.service.turnos;

import com.sitra.sitra.entity.turnos.LlamadaEntity;
import com.sitra.sitra.expose.request.turnos.LlamadaRequest;
import com.sitra.sitra.expose.response.turnos.LlamadaResponse;

public interface LlamadaService {
    LlamadaResponse save(LlamadaRequest request);
    LlamadaResponse callNext(String date, String codePriority, String codeVentanilla, Long asesorId);

    LlamadaEntity getByOrderAtention(Long orderAtentionId);
}
