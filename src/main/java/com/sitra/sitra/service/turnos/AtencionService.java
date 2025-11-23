package com.sitra.sitra.service.turnos;

import com.sitra.sitra.entity.turnos.AtencionEntity;
import com.sitra.sitra.expose.request.turnos.AtencionRequest;
import com.sitra.sitra.expose.response.turnos.AtencionResponse;
import org.springframework.data.domain.Page;

public interface AtencionService {
    AtencionResponse save(AtencionRequest request);
    AtencionResponse finishAtention(AtencionRequest request);
    Page<AtencionResponse> getListPaginatedByDate(int page, int size, String date);
    Page<AtencionResponse> getListPaginatedByDateAndVentanilla(int page, int size, String date, String codeVentanilla);

    AtencionEntity getById(Long id);
}
