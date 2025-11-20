package com.sitra.sitra.service.turnos.impl;

import com.sitra.sitra.entity.seguridad.UsuarioEntity;
import com.sitra.sitra.entity.turnos.AtencionEntity;
import com.sitra.sitra.entity.turnos.OrdenAtencionEntity;
import com.sitra.sitra.exceptions.NotFoundException;
import com.sitra.sitra.expose.request.turnos.AtencionRequest;
import com.sitra.sitra.expose.response.turnos.AtencionResponse;
import com.sitra.sitra.repository.turnos.AtencionRepository;
import com.sitra.sitra.service.maestros.impl.TablaMaestraServiceImpl;
import com.sitra.sitra.service.seguridad.UsuarioService;
import com.sitra.sitra.service.turnos.AtencionService;
import com.sitra.sitra.service.turnos.OrdenAtencionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
public class AtencionServiceImpl implements AtencionService {

    private final AtencionRepository atencionRepository;

    private final OrdenAtencionService ordenAtencionService;
    private final UsuarioService usuarioService;

    @Override
    @Transactional
    public AtencionResponse save(AtencionRequest request) {
        String context = "Registrando una nueva atencion";
        log.info("Registrando una atencion. [ CONTEXTO : {} ]", context);

        if (!TablaMaestraServiceImpl.tableCodeVentanilla.containsValue(request.getCodVentanilla())) throw new NotFoundException("Ventanilla no registrado");

        if (!TablaMaestraServiceImpl.tableOrderAttentionStatus.containsValue(request.getCodEstadoAtencion())) throw new NotFoundException("Estado no soportado.");

        OrdenAtencionEntity ordenAtencion = ordenAtencionService.getOrdenById(request.getOrdenAtencionId());
        UsuarioEntity asesor = usuarioService.getUser(request.getAsesorId());

        AtencionEntity entity = AtencionRequest.toEntity.apply(request);
        entity.setAsesor(asesor);
        entity.setOrdenAtencion(ordenAtencion);

        AtencionEntity saved = atencionRepository.save(entity);

        return AtencionResponse.toResponse.apply(saved);
    }

    @Override
    public AtencionResponse update(AtencionRequest request) {
        return null;
    }

    @Override
    public Page<AtencionResponse> getListPaginatedByDate(int page, int size, String date) {
        return null;
    }

    @Override
    public Page<AtencionResponse> getListPaginatedByDateAndVentanilla(int page, int size, String date, String codeVentanilla) {
        return null;
    }
}
