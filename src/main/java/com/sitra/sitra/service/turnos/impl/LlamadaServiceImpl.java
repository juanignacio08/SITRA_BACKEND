package com.sitra.sitra.service.turnos.impl;

import com.sitra.sitra.entity.seguridad.UsuarioEntity;
import com.sitra.sitra.entity.turnos.LlamadaEntity;
import com.sitra.sitra.entity.turnos.OrdenAtencionEntity;
import com.sitra.sitra.exceptions.BadRequestException;
import com.sitra.sitra.exceptions.BusinessRuleException;
import com.sitra.sitra.exceptions.NotFoundException;
import com.sitra.sitra.expose.request.turnos.LlamadaRequest;
import com.sitra.sitra.expose.response.turnos.LlamadaResponse;
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
    public LlamadaResponse callNext(String date, String codePriority, String codeVentanilla, Long asesorId) {
        String context = "callNextOrderAtention";
        log.info("LLamando al siguiente orden de atencion. [ DATE : {} | CODEPRIORITY : {} | CODEVENTANILLA : {} | ASESOR : {} | CONTEXTO : {} ]", date, codePriority, codeVentanilla, asesorId, context);

        if (!TablaMaestraServiceImpl.tableCodeVentanilla.containsValue(codeVentanilla)) throw new NotFoundException("Ventanilla no registrada.");
        if (!TablaMaestraServiceImpl.tablePreferential.containsValue(codePriority)) throw new BusinessRuleException("Prioridad no registrada.");

        LocalDate fecha = DateConvertUtil.parseFechaDDMMYYYY(date);

        UsuarioEntity asesor = usuarioService.getUser(asesorId);

        //Verificar si existe alguien en llamada
        OrdenAtencionEntity orderAtentionInCall = ordenAtencionService.getOrderInCallStatus(codePriority, fecha);
        OrdenAtencionEntity orderAtentionNext;

        //Si existe, llamar ese mismo.
        if (orderAtentionInCall != null) {
            LlamadaEntity llamada = getByOrderAtention(orderAtentionInCall.getOrdenAtencionId());
            if (!llamada.getAsesor().getUsuarioId().equals(asesorId)) throw new BusinessRuleException("No puede llamar otro asesor a una orden ya llamada con otro asesor");
            if (!llamada.getCodVentanilla().equals(codeVentanilla)) throw new BusinessRuleException("No puede llamar otra ventanilla a una orden ya llamada por otra ventanilla");
            if (!llamada.getCodResultado().equals(TablaMaestraServiceImpl.PENDIENTE_LLAMADA)) throw new BusinessRuleException("Solo se permite llamar a una orden pendiente");

            if (llamada.getNumLlamada() < 3) {
                llamada.setNumLlamada(llamada.getNumLlamada() + 1);
            } else {
                llamada.setCodResultado(TablaMaestraServiceImpl.NO_RESPONDIO);
                orderAtentionInCall.setCodEstadoAtencion(TablaMaestraServiceImpl.AUSENTE);
            }

            LlamadaEntity updated = llamadaRepository.save(llamada);

            return LlamadaResponse.toResponse.apply(updated);
        } else {
            //Si no existe, llamar al primero de la lista y nume llamada = 1
            orderAtentionNext = ordenAtencionService.getNextOrderInPendingStatus(codePriority, fecha);
            if (orderAtentionNext == null) return null;

            orderAtentionNext.setCodEstadoAtencion(TablaMaestraServiceImpl.EN_LLAMADA);
            orderAtentionNext.setActualizadoPor(SecurityUtil.getCurrentUserId());
            orderAtentionNext.setFechaActualizacion(LocalDateTime.now());

            LlamadaResponse response = saveByOrderAtentionAndAsesorAndVentanillaAndDate(orderAtentionNext, asesor, codeVentanilla, fecha);
            return response;
        }
    }

    @Override
    public LlamadaResponse markAsAbsent(Long llamadaId) {
        String context = "markAsAbsentLLamadaAndOrder";
        log.info("Marcar como no presentado y ausente una llamada y orden. [ LLAMADA : {} | CONTEXTO : {} ]", llamadaId, context);

        LlamadaEntity entity = getWithOrderById(llamadaId);

        if (!entity.getCodResultado().equals(TablaMaestraServiceImpl.PENDIENTE_LLAMADA)) throw new BusinessRuleException("Solo se puede dar como ausente una vez que la orden de atencion fue llamado.");
        if (!entity.getNumLlamada().equals(3)) throw new BusinessRuleException("Solo se puede marcar como ausente cuando se le haya llamado tres veces");

        entity.getOrdenAtencion().setCodEstadoAtencion(TablaMaestraServiceImpl.AUSENTE);
        entity.getOrdenAtencion().setActualizadoPor(SecurityUtil.getCurrentUserId());
        entity.getOrdenAtencion().setFechaActualizacion(LocalDateTime.now());

        entity.setCodResultado(TablaMaestraServiceImpl.NO_RESPONDIO);
        entity.setActualizadoPor(SecurityUtil.getCurrentUserId());
        entity.setFechaActualizacion(LocalDateTime.now());

        LlamadaEntity updated = llamadaRepository.save(entity);

        return LlamadaResponse.toResponse.apply(updated);
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
