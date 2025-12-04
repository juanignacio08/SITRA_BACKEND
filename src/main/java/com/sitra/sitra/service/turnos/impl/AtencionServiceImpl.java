package com.sitra.sitra.service.turnos.impl;

import com.sitra.sitra.entity.seguridad.UsuarioEntity;
import com.sitra.sitra.entity.turnos.AtencionEntity;
import com.sitra.sitra.entity.turnos.LlamadaEntity;
import com.sitra.sitra.entity.turnos.OrdenAtencionEntity;
import com.sitra.sitra.exceptions.BadRequestException;
import com.sitra.sitra.exceptions.BusinessRuleException;
import com.sitra.sitra.exceptions.NotFoundException;
import com.sitra.sitra.expose.request.turnos.AtencionRequest;
import com.sitra.sitra.expose.response.seguridad.PersonaResponse;
import com.sitra.sitra.expose.response.turnos.AtencionResponse;
import com.sitra.sitra.expose.response.turnos.PantallaResponse;
import com.sitra.sitra.expose.util.DateConvertUtil;
import com.sitra.sitra.persistence.repository.turnos.AtencionRepository;
import com.sitra.sitra.service.maestros.impl.TablaMaestraServiceImpl;
import com.sitra.sitra.service.seguridad.UsuarioService;
import com.sitra.sitra.service.turnos.AtencionService;
import com.sitra.sitra.service.turnos.LlamadaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
public class AtencionServiceImpl implements AtencionService {

    private final AtencionRepository atencionRepository;

    private final LlamadaService llamadaService;
    private final UsuarioService usuarioService;

    @Override
    @Transactional
    public PantallaResponse save(AtencionRequest request) {
        String context = "startAtention";
        log.info("Registrando una atencion. [ CONTEXTO : {} ]", context);

        if (!TablaMaestraServiceImpl.tableCodeVentanilla.containsValue(request.getCodVentanilla())) throw new NotFoundException("Ventanilla no registrado");

        LlamadaEntity llamada = llamadaService.getWithOrderByOrderAtention(request.getOrdenAtencionId());

        OrdenAtencionEntity ordenAtencion = llamada.getOrdenAtencion();

        if (!ordenAtencion.getCodEstadoAtencion().equals(TablaMaestraServiceImpl.EN_LLAMADA)) throw new BusinessRuleException("Solo se puede atender a una orden que esta en llamada");
        if (!ordenAtencion.getCodVentanilla().equals(request.getCodVentanilla())) throw new BusinessRuleException("No se puede atender en otra ventanilla al que fue llamado.");
        if (!llamada.getAsesor().getUsuarioId().equals(request.getAsesorId())) throw new BusinessRuleException("No puede atender un asesor diferente al asesor que llamo.");
        if (!llamada.getCodVentanilla().equals(request.getCodVentanilla())) throw new BusinessRuleException("No se puede atender en otra ventanilla al que fue llamado.");
        if (llamada.getNumLlamada().equals(0)) throw new BusinessRuleException("No se puede atender a una orden que tiene 0 llamadas.");
        if (!llamada.getCodResultado().equals(TablaMaestraServiceImpl.PENDIENTE_LLAMADA)) throw new BusinessRuleException("Solo se puede atender a las llamadas que esten pendientes.");

        UsuarioEntity asesor = usuarioService.getUser(request.getAsesorId());

        ordenAtencion.setCodEstadoAtencion(TablaMaestraServiceImpl.ATENDIENDO);
        ordenAtencion.setCodVentanilla(request.getCodVentanilla());

        llamada.setCodResultado(TablaMaestraServiceImpl.SE_PRESENTO);
        llamada.setOrdenAtencion(ordenAtencion);
        llamada.setCodVentanilla(request.getCodVentanilla());

        AtencionEntity entity = AtencionRequest.toEntity.apply(request);
        entity.setAsesor(asesor);
        entity.setOrdenAtencion(ordenAtencion);

        AtencionEntity saved = atencionRepository.save(entity);

        return PantallaResponse.builder()
                .orderAtencionId(ordenAtencion.getOrdenAtencionId())
                .llamadaId(llamada.getLlamadaId())
                .atencionId(saved.getAtencionId())
                .paciente(PersonaResponse.toResponse.apply(llamada.getOrdenAtencion().getPersona()))
                .fecha(DateConvertUtil.formatLocalDateToDDMMYYYY(ordenAtencion.getFecha()))
                .codPriority(ordenAtencion.getCodPrioridad())
                .turno(ordenAtencion.getTurno())
                .codEstadoAtencion(ordenAtencion.getCodEstadoAtencion())
                .codVentanilla(llamada.getCodVentanilla())
                .numLlamada(llamada.getNumLlamada())
                .codResultado(llamada.getCodResultado())
                .build();
    }

    @Override
    @Transactional
    public PantallaResponse finishAtention(AtencionRequest request) {
        String context = "finishAtention";
        log.info("Finalizando una atencion. [ ATENCION : {} | ORDENATENCION : {} | CONTEXTO : {} ]", request.getAtencionId(), request.getOrdenAtencionId(), context);

        AtencionEntity atencion = getById(request.getAtencionId());

        if (!atencion.getCodVentanilla().equals(request.getCodVentanilla())) throw new BusinessRuleException("Solo se puede finalizar la atencion la misma ventanilla que la inicio");
        if (!atencion.getAsesor().getUsuarioId().equals(request.getAsesorId())) throw new BusinessRuleException("Solo puede finalizar la atencion el mismo asesor");
        if (!atencion.getOrdenAtencion().getOrdenAtencionId().equals(request.getOrdenAtencionId())) throw new BusinessRuleException("No se puede atender en otra ventanilla al que fue llamado.");

        LlamadaEntity llamada = llamadaService.getWithOrderByOrderAtention(request.getOrdenAtencionId());

        OrdenAtencionEntity ordenAtencion = llamada.getOrdenAtencion();

        if (!ordenAtencion.getCodEstadoAtencion().equals(TablaMaestraServiceImpl.ATENDIENDO)) throw new BusinessRuleException("Solo se puede finalizar la atencion a una orden que esta siendo atendiendo");
        if (!ordenAtencion.getCodVentanilla().equals(request.getCodVentanilla())) throw new BusinessRuleException("");

        if (!llamada.getAsesor().getUsuarioId().equals(request.getAsesorId())) throw new BusinessRuleException("No puede atender un asesor diferente al asesor que llamo.");
        if (!llamada.getCodVentanilla().equals(request.getCodVentanilla())) throw new BusinessRuleException("No se puede atender en otra ventanilla al que fue llamado.");
        if (llamada.getNumLlamada().equals(0)) throw new BusinessRuleException("No se puede atender a una orden que tiene 0 llamadas.");
        if (!llamada.getCodResultado().equals(TablaMaestraServiceImpl.SE_PRESENTO)) throw new BusinessRuleException("Solo se puede finalizar a las llamadas que los pacientes se presentaron.");

        ordenAtencion.setCodEstadoAtencion(TablaMaestraServiceImpl.ATENDIDO);
        ordenAtencion.setCodVentanilla(request.getCodVentanilla());

        llamada.setCodResultado(TablaMaestraServiceImpl.ATENDIDO_LLAMADA);
        llamada.setOrdenAtencion(ordenAtencion);
        llamada.setCodVentanilla(request.getCodVentanilla());

        AtencionRequest.toUpdate(request, atencion);
        atencion.setHoraFin(LocalTime.now());
        atencion.setOrdenAtencion(ordenAtencion);

        AtencionEntity updated = atencionRepository.save(atencion);

        return PantallaResponse.builder()
                .orderAtencionId(ordenAtencion.getOrdenAtencionId())
                .llamadaId(llamada.getLlamadaId())
                .atencionId(updated.getAtencionId())
                .paciente(PersonaResponse.toResponse.apply(llamada.getOrdenAtencion().getPersona()))
                .fecha(DateConvertUtil.formatLocalDateToDDMMYYYY(ordenAtencion.getFecha()))
                .codPriority(ordenAtencion.getCodPrioridad())
                .turno(ordenAtencion.getTurno())
                .codEstadoAtencion(ordenAtencion.getCodEstadoAtencion())
                .codVentanilla(llamada.getCodVentanilla())
                .numLlamada(llamada.getNumLlamada())
                .codResultado(llamada.getCodResultado())
                .build();
    }

    @Override
    public PantallaResponse getScreen(String date, String codeVentanilla) {
        String context = "getScreen";
        log.info("Obteniendo al paciente que esta siendo llamado o atendiendose. [ FECHA : {} | CONTEXTO : {} ]",date, context);

        LocalDate fecha = DateConvertUtil.parseFechaDDMMYYYY(date);

        //Buscamos a los que estan en llamada
        LlamadaEntity inCall = llamadaService.getWithOrderLLamadaInPendind(fecha, codeVentanilla);

        //Buscamos a los que estan atendiendose
        LlamadaEntity inAtention = llamadaService.getWithOrderLlamadaInPresent(fecha, codeVentanilla);

        if (inCall != null) {
            if (inAtention != null) {
                throw new BusinessRuleException("No puede haber mas de una orden de atencion que este llamando y atendiendose en la misma ventanilla");
            } else {
                return PantallaResponse.builder()
                        .orderAtencionId(inCall.getOrdenAtencion().getOrdenAtencionId())
                        .llamadaId(inCall.getLlamadaId())
                        .paciente(PersonaResponse.toResponse.apply(inCall.getOrdenAtencion().getPersona()))
                        .fecha(DateConvertUtil.formatLocalDateToDDMMYYYY(inCall.getOrdenAtencion().getFecha()))
                        .codPriority(inCall.getOrdenAtencion().getCodPrioridad())
                        .turno(inCall.getOrdenAtencion().getTurno())
                        .codEstadoAtencion(inCall.getOrdenAtencion().getCodEstadoAtencion())
                        .codVentanilla(inCall.getCodVentanilla())
                        .numLlamada(inCall.getNumLlamada())
                        .codResultado(inCall.getCodResultado())
                        .build();
            }
        } else {
            if (inAtention != null) {
                AtencionEntity atencion = getByOrderAtention(inAtention.getOrdenAtencion().getOrdenAtencionId());

                return PantallaResponse.builder()
                        .orderAtencionId(inAtention.getOrdenAtencion().getOrdenAtencionId())
                        .llamadaId(inAtention.getLlamadaId())
                        .atencionId(atencion.getAtencionId())
                        .paciente(PersonaResponse.toResponse.apply(inAtention.getOrdenAtencion().getPersona()))
                        .fecha(DateConvertUtil.formatLocalDateToDDMMYYYY(inAtention.getOrdenAtencion().getFecha()))
                        .codPriority(inAtention.getOrdenAtencion().getCodPrioridad())
                        .turno(inAtention.getOrdenAtencion().getTurno())
                        .codEstadoAtencion(inAtention.getOrdenAtencion().getCodEstadoAtencion())
                        .codVentanilla(inAtention.getCodVentanilla())
                        .numLlamada(inAtention.getNumLlamada())
                        .codResultado(inAtention.getCodResultado())
                        .build();
            } else {
                return null;
            }
        }
    }

    @Override
    public Page<AtencionResponse> getListPaginatedByDate(int page, int size, String date) {
        return null;
    }

    @Override
    public Page<AtencionResponse> getListPaginatedByDateAndVentanilla(int page, int size, String date, String codeVentanilla) {
        return null;
    }

    @Override
    public AtencionEntity getById(Long id) {
        if (id == null || id < 1) throw new BadRequestException("Id Incorrecto. [ ATENCION ]");

        return atencionRepository.getByID(id).orElseThrow(() -> new NotFoundException("Recurso no encontrado. [ ATENCION ]"));
    }

    @Override
    public AtencionEntity getByOrderAtention(Long orderAtentionId) {
        if (orderAtentionId == null || orderAtentionId < 1) throw new BadRequestException("Id Incorrecto. [ ORDEN_ATENCION ]");

        return atencionRepository.getByOrderAtention(orderAtentionId).orElseThrow(() -> new NotFoundException("Recurso no encontrado. [ ATENCION ]"));
    }
}
