package com.sitra.sitra.service.turnos.impl;

import com.sitra.sitra.entity.seguridad.UsuarioEntity;
import com.sitra.sitra.entity.turnos.LlamadaEntity;
import com.sitra.sitra.entity.turnos.OrdenAtencionEntity;
import com.sitra.sitra.exceptions.BadRequestException;
import com.sitra.sitra.exceptions.BusinessRuleException;
import com.sitra.sitra.exceptions.NotFoundException;
import com.sitra.sitra.expose.request.turnos.LlamadaRequest;
import com.sitra.sitra.expose.response.seguridad.PersonaResponse;
import com.sitra.sitra.expose.response.turnos.LlamadaResponse;
import com.sitra.sitra.expose.response.turnos.PantallaResponse;
import com.sitra.sitra.expose.util.DateConvertUtil;
import com.sitra.sitra.expose.util.SecurityUtil;
import com.sitra.sitra.repository.turnos.LlamadaRepository;
import com.sitra.sitra.service.maestros.impl.TablaMaestraServiceImpl;
import com.sitra.sitra.service.seguridad.UsuarioService;
import com.sitra.sitra.service.turnos.LlamadaService;
import com.sitra.sitra.service.turnos.OrdenAtencionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
public class LlamadaServiceImpl implements LlamadaService {

    private final LlamadaRepository llamadaRepository;
    private final OrdenAtencionService ordenAtencionService;
    private final UsuarioService usuarioService;

    @Override
    @Transactional
    public LlamadaResponse save(LlamadaRequest request) {
        String context = "saveLLamada";
        log.info("Registrando una llamada. [ CONTEXTO : {} ]", context);

        OrdenAtencionEntity ordenAtencion = ordenAtencionService.getOrdenById(request.getOrdenAtencionId());
        UsuarioEntity asesor = usuarioService.getUser(request.getAsesorId());

        if (!TablaMaestraServiceImpl.tableCodeVentanilla.containsValue(request.getCodVentanilla())) throw new BadRequestException("Ventanilla no registrada.");

        LlamadaEntity entity = LlamadaRequest.toEntity.apply(request);
        entity.setOrdenAtencion(ordenAtencion);
        entity.setAsesor(asesor);

        LlamadaEntity saved = llamadaRepository.save(entity);

        return LlamadaResponse.toResponse.apply(saved);
    }

    @Override
    @Transactional
    public PantallaResponse callNext(String date, String codePriority, String codeVentanilla, Long asesorId) {
        String context = "callNextOrderAtention";
        log.info("LLamando al siguiente orden de atencion. [ DATE : {} | CODEPRIORITY : {} | CODEVENTANILLA : {} | ASESOR : {} | CONTEXTO : {} ]", date, codePriority, codeVentanilla, asesorId, context);

        if (!TablaMaestraServiceImpl.tableCodeVentanilla.containsValue(codeVentanilla)) throw new NotFoundException("Ventanilla no registrada.");
        if (!TablaMaestraServiceImpl.tablePreferential.containsValue(codePriority)) throw new BusinessRuleException("Prioridad no registrada.");

        LocalDate fecha = DateConvertUtil.parseFechaDDMMYYYY(date);

        UsuarioEntity asesor = usuarioService.getUser(asesorId);

        OrdenAtencionEntity orderAtentionInVentanilla = ordenAtencionService.getOrderInVentanilla(codePriority, fecha, codeVentanilla);

        OrdenAtencionEntity orderAtentionNext;

        if (orderAtentionInVentanilla != null) {
            if (orderAtentionInVentanilla.getCodEstadoAtencion().equals(TablaMaestraServiceImpl.ATENDIENDO)) throw new BusinessRuleException("No se puede llamar cuando se tiene un paciente en atencion");

            LlamadaEntity llamada = getByOrderAtention(orderAtentionInVentanilla.getOrdenAtencionId());
            if (!llamada.getAsesor().getUsuarioId().equals(asesorId)) throw new BusinessRuleException("No puede llamar otro asesor a una orden ya llamada con otro asesor");
            if (!llamada.getCodVentanilla().equals(codeVentanilla)) throw new BusinessRuleException("No puede llamar otra ventanilla a una orden ya llamada por otra ventanilla");
            if (!llamada.getCodResultado().equals(TablaMaestraServiceImpl.PENDIENTE_LLAMADA)) throw new BusinessRuleException("Solo se permite llamar a una orden pendiente");

            if (llamada.getNumLlamada() < 3) {
                llamada.setNumLlamada(llamada.getNumLlamada() + 1);
            } else {
                llamada.setCodResultado(TablaMaestraServiceImpl.NO_RESPONDIO);
                orderAtentionInVentanilla.setCodEstadoAtencion(TablaMaestraServiceImpl.AUSENTE);
            }

            LlamadaEntity updated = llamadaRepository.save(llamada);

            return PantallaResponse.builder()
                    .orderAtencionId(orderAtentionInVentanilla.getOrdenAtencionId())
                    .llamadaId(updated.getLlamadaId())
                    .paciente(PersonaResponse.toResponse.apply(orderAtentionInVentanilla.getPersona()))
                    .fecha(DateConvertUtil.formatLocalDateToDDMMYYYY(orderAtentionInVentanilla.getFecha()))
                    .codPriority(orderAtentionInVentanilla.getCodPrioridad())
                    .turno(orderAtentionInVentanilla.getTurno())
                    .codEstadoAtencion(orderAtentionInVentanilla.getCodEstadoAtencion())
                    .codVentanilla(updated.getCodVentanilla())
                    .numLlamada(updated.getNumLlamada())
                    .codResultado(updated.getCodResultado())
                    .build();

        } else {
            orderAtentionNext = ordenAtencionService.getNextOrderInPendingStatus(codePriority, fecha);
            if (orderAtentionNext == null) return null;

            orderAtentionNext.setCodEstadoAtencion(TablaMaestraServiceImpl.EN_LLAMADA);
            orderAtentionNext.setCodVentanilla(codeVentanilla);
            orderAtentionNext.setActualizadoPor(SecurityUtil.getCurrentUserId());
            orderAtentionNext.setFechaActualizacion(LocalDateTime.now());

            LlamadaResponse response = saveByOrderAtentionAndAsesorAndVentanillaAndDate(orderAtentionNext, asesor, codeVentanilla, fecha);

            return PantallaResponse.builder()
                    .orderAtencionId(orderAtentionNext.getOrdenAtencionId())
                    .llamadaId(response.getLlamadaId())
                    .paciente(PersonaResponse.toResponse.apply(orderAtentionNext.getPersona()))
                    .fecha(DateConvertUtil.formatLocalDateToDDMMYYYY(orderAtentionNext.getFecha()))
                    .codPriority(orderAtentionNext.getCodPrioridad())
                    .turno(orderAtentionNext.getTurno())
                    .codEstadoAtencion(orderAtentionNext.getCodEstadoAtencion())
                    .codVentanilla(response.getCodVentanilla())
                    .numLlamada(response.getNumLlamada())
                    .codResultado(response.getCodResultado())
                    .build();
        }
    }

    @Override
    public PantallaResponse markAsAbsent(Long llamadaId, String codeVentanilla) {
        String context = "markAsAbsentLLamadaAndOrder";
        log.info("Marcar como no presentado y ausente una llamada y orden. [ LLAMADA : {} | CONTEXTO : {} ]", llamadaId, context);

        LlamadaEntity entity = getWithOrderById(llamadaId);

        if (!TablaMaestraServiceImpl.tableCodeVentanilla.containsValue(codeVentanilla)) throw new BadRequestException("Ventanilla no registrada");

        if (!entity.getCodResultado().equals(TablaMaestraServiceImpl.PENDIENTE_LLAMADA)) throw new BusinessRuleException("Solo se puede dar como ausente una vez que la orden de atencion fue llamado.");
        if (!entity.getNumLlamada().equals(3)) throw new BusinessRuleException("Solo se puede marcar como ausente cuando se le haya llamado tres veces");
        if (!entity.getCodVentanilla().equals(codeVentanilla)) throw new BusinessRuleException("Solo puede marcar ausente la misma ventanilla que lo llamo.");

        entity.getOrdenAtencion().setCodEstadoAtencion(TablaMaestraServiceImpl.AUSENTE);
        entity.getOrdenAtencion().setCodVentanilla(codeVentanilla);
        entity.getOrdenAtencion().setActualizadoPor(SecurityUtil.getCurrentUserId());
        entity.getOrdenAtencion().setFechaActualizacion(LocalDateTime.now());

        entity.setCodResultado(TablaMaestraServiceImpl.NO_RESPONDIO);
        entity.setCodVentanilla(codeVentanilla);
        entity.setActualizadoPor(SecurityUtil.getCurrentUserId());
        entity.setFechaActualizacion(LocalDateTime.now());

        LlamadaEntity updated = llamadaRepository.save(entity);

        return PantallaResponse.builder()
                .orderAtencionId(entity.getOrdenAtencion().getOrdenAtencionId())
                .llamadaId(updated.getLlamadaId())
                .paciente(PersonaResponse.toResponse.apply(entity.getOrdenAtencion().getPersona()))
                .fecha(DateConvertUtil.formatLocalDateToDDMMYYYY(entity.getOrdenAtencion().getFecha()))
                .codPriority(entity.getOrdenAtencion().getCodPrioridad())
                .turno(entity.getOrdenAtencion().getTurno())
                .codEstadoAtencion(entity.getOrdenAtencion().getCodEstadoAtencion())
                .codVentanilla(updated.getCodVentanilla())
                .numLlamada(updated.getNumLlamada())
                .codResultado(updated.getCodResultado())
                .build();
    }

    @Override
    public LlamadaEntity getByOrderAtention(Long orderAtentionId) {
        if (orderAtentionId == null || orderAtentionId < 1) throw new BadRequestException("Id Incorrecto. [ ORDEN ATENCION ]");

        return llamadaRepository.getByOrderAtention(orderAtentionId).orElseThrow(() -> new NotFoundException("Recurso no encontrado [ LLAMADA ]"));
    }

    @Override
    public LlamadaEntity getWithOrderByOrderAtention(Long orderAtentionId) {
        if (orderAtentionId == null || orderAtentionId < 1) throw new BadRequestException("Id Incorrecto. [ ORDEN ATENCION ]");

        return llamadaRepository.getWithOrderByOrderAtention(orderAtentionId).orElseThrow(() -> new NotFoundException("Recurso no encontrado [ LLAMADA ]"));
    }

    @Override
    public LlamadaEntity getWithOrderById(Long id) {
        if (id == null || id < 1) throw new BadRequestException("Id Incorrecto. [ LLAMADA ]");

        return llamadaRepository.getWithOrderByID(id).orElseThrow(() -> new NotFoundException("Recurso no encontrado. [ LLAMADA ]"));
    }

    @Override
    public LlamadaEntity getWithOrderLLamadaInPendind(LocalDate date, String codeVentanilla) {
        if (codeVentanilla == null || codeVentanilla.length() != 6 || date == null) throw new BadRequestException("Error en la fecha o en el codigo de la ventanilla. [ LLAMADA ]");

        return llamadaRepository.getLlamadaInPending(date, codeVentanilla).orElse(null);
    }

    @Override
    public LlamadaEntity getWithOrderLlamadaInPresent(LocalDate date, String codeVentanilla) {
        if (codeVentanilla == null || codeVentanilla.length() != 6 || date == null) throw new BadRequestException("Error en la fecha o en el codigo de la ventanilla. [ LLAMADA ]");

        return llamadaRepository.getLlamadaInAtention(date, codeVentanilla).orElse(null);
    }

    private LlamadaResponse saveByOrderAtentionAndAsesorAndVentanillaAndDate(OrdenAtencionEntity ordenAtencion, UsuarioEntity asesor, String codeVentanilla, LocalDate date) {
        LlamadaEntity entity = LlamadaEntity.builder()
                .ordenAtencion(ordenAtencion)
                .asesor(asesor)
                .codVentanilla(codeVentanilla)
                .fechaLlamada(date)
                .horaLlamada(LocalTime.now())
                .numLlamada(1)
                .codResultado(TablaMaestraServiceImpl.PENDIENTE_LLAMADA)
                .estado(1)
                .actualizadoPor(SecurityUtil.getCurrentUserId())
                .fechaActualizacion(LocalDateTime.now())
                .creadoPor(SecurityUtil.getCurrentUserId())
                .fechaCreacion(LocalDateTime.now())
                .eliminado(false)
                .build();

        LlamadaEntity saved = llamadaRepository.save(entity);

        return LlamadaResponse.toResponse.apply(saved);
    }

}
