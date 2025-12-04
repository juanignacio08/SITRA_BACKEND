package com.sitra.sitra.service.turnos;

import com.sitra.sitra.entity.turnos.OrdenAtencionEntity;
import com.sitra.sitra.expose.request.turnos.OrdenAtencionRequest;
import com.sitra.sitra.expose.response.turnos.OrdenAtencionResponse;
import com.sitra.sitra.persistence.projections.OrdenAtencionDetailProjection;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface OrdenAtencionService {
    OrdenAtencionResponse save(OrdenAtencionRequest request);
    OrdenAtencionResponse update(OrdenAtencionRequest request);
    Page<OrdenAtencionResponse> getPagedAttentionOrdersNormalsInPendingStatus(int page, int size, String date);
    Page<OrdenAtencionResponse> getPagedAttentionOrdersPreferentialInPendingStatus(int page, int size, String date);
    List<OrdenAtencionResponse> getAttentionOrderInCallsStatus(String date);
    OrdenAtencionResponse getById(Long id);
    List<OrdenAtencionDetailProjection> getRecordByDate(String date);

    OrdenAtencionEntity getOrdenById(Long id);
    OrdenAtencionEntity getOrderInCallStatus(String codePriority, LocalDate date);
    OrdenAtencionEntity getNextOrderInPendingStatus(String codePriority, LocalDate date);
    OrdenAtencionEntity getOrderInVentanilla(String codePriority, LocalDate date, String codeVentanilla);
}
