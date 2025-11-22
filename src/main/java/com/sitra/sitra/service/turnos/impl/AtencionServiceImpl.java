package com.sitra.sitra.service.turnos.impl;

import com.sitra.sitra.entity.seguridad.UsuarioEntity;
import com.sitra.sitra.entity.turnos.AtencionEntity;
import com.sitra.sitra.entity.turnos.LlamadaEntity;
import com.sitra.sitra.entity.turnos.OrdenAtencionEntity;
import com.sitra.sitra.exceptions.BusinessRuleException;
import com.sitra.sitra.exceptions.NotFoundException;
import com.sitra.sitra.expose.request.turnos.AtencionRequest;
import com.sitra.sitra.expose.response.turnos.AtencionResponse;
import com.sitra.sitra.expose.util.SecurityUtil;
import com.sitra.sitra.repository.turnos.AtencionRepository;
import com.sitra.sitra.service.maestros.impl.TablaMaestraServiceImpl;
import com.sitra.sitra.service.seguridad.UsuarioService;
import com.sitra.sitra.service.turnos.AtencionService;
import com.sitra.sitra.service.turnos.LlamadaService;
import com.sitra.sitra.service.turnos.OrdenAtencionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
public class AtencionServiceImpl implements AtencionService {

    private final AtencionRepository atencionRepository;

    private final OrdenAtencionService ordenAtencionService;
    private final LlamadaService llamadaService;
    private final UsuarioService usuarioService;

    @Override
    @Transactional
    public AtencionResponse save(AtencionRequest request) {
        String context = "startAtention";
        log.info("Registrando una atencion. [ CONTEXTO : {} ]", context);

        if (!TablaMaestraServiceImpl.tableCodeVentanilla.containsValue(request.getCodVentanilla())) throw new NotFoundException("Ventanilla no registrado");

        LlamadaEntity llamada = llamadaService.getWithOrderByOrderAtention(request.getOrdenAtencionId());

        OrdenAtencionEntity ordenAtencion = llamada.getOrdenAtencion();

        if (!ordenAtencion.getCodEstadoAtencion().equals(TablaMaestraServiceImpl.EN_LLAMADA)) throw new BusinessRuleException("Solo se puede atender a una orden que esta en llamada");

        if (!llamada.getAsesor().getUsuarioId().equals(request.getAsesorId())) throw new BusinessRuleException("No puede atender un asesor diferente al asesor que llamo.");
        if (!llamada.getCodVentanilla().equals(request.getCodVentanilla())) throw new BusinessRuleException("No se puede atender en otra ventanilla al que fue llamado.");
        if (llamada.getNumLlamada().equals(0)) throw new BusinessRuleException("No se puede atender a una orden que tiene 0 llamadas.");
        if (!llamada.getCodResultado().equals(TablaMaestraServiceImpl.PENDIENTE_LLAMADA)) throw new BusinessRuleException("Solo se puede atender a las llamadas que esten pendientes.");

        UsuarioEntity asesor = usuarioService.getUser(request.getAsesorId());

        ordenAtencion.setCodEstadoAtencion(TablaMaestraServiceImpl.ATENDIENDO);

        llamada.setCodResultado(TablaMaestraServiceImpl.SE_PRESENTO);
        llamada.setOrdenAtencion(ordenAtencion);

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
