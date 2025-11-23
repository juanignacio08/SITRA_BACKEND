package com.sitra.sitra.service.turnos;

import com.sitra.sitra.entity.turnos.LlamadaEntity;
import com.sitra.sitra.expose.request.turnos.LlamadaRequest;
import com.sitra.sitra.expose.response.turnos.LlamadaResponse;
import com.sitra.sitra.expose.response.turnos.PantallaResponse;

public interface LlamadaService {
    LlamadaResponse save(LlamadaRequest request);
    PantallaResponse callNext(String date, String codePriority, String codeVentanilla, Long asesorId);
    PantallaResponse markAsAbsent(Long llamadaId);

    LlamadaEntity getByOrderAtention(Long orderAtentionId);
    LlamadaEntity getWithOrderByOrderAtention(Long orderAtentionId);
    LlamadaEntity getWithOrderById(Long id);
}
