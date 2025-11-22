package com.sitra.sitra.service.turnos;

import com.sitra.sitra.entity.turnos.OrdenAtencionEntity;
import com.sitra.sitra.expose.request.turnos.OrdenAtencionRequest;
import com.sitra.sitra.expose.response.turnos.OrdenAtencionResponse;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface OrdenAtencionService {
    OrdenAtencionResponse save(OrdenAtencionRequest request);
    OrdenAtencionResponse update(OrdenAtencionRequest request);
    OrdenAtencionResponse callNext(String date, String codePriority, String codeVentanilla, Long asesorId);
    Page<OrdenAtencionResponse> getPagedAttentionOrdersNormalsInPendingStatus(int page, int size, String date);
    Page<OrdenAtencionResponse> getPagedAttentionOrdersPreferentialInPendingStatus(int page, int size, String date);
    List<OrdenAtencionResponse> getAttentionOrderInCallsStatus(String date);
    List<OrdenAtencionResponse> getListByDate(String date);
    OrdenAtencionResponse getById(Long id);

    OrdenAtencionEntity getOrdenById(Long id);
    List<OrdenAtencionEntity> getListByStatus(String codEstadoAtencion);
    OrdenAtencionEntity getOrderInCallStatus(String codePriority, LocalDate date);
    OrdenAtencionEntity getNextOrderInPendingStatus(String codePriority, LocalDate date);
}
