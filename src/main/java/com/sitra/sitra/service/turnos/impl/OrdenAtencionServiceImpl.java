package com.sitra.sitra.service.turnos.impl;

import com.sitra.sitra.entity.seguridad.PersonaEntity;
import com.sitra.sitra.entity.seguridad.UsuarioEntity;
import com.sitra.sitra.entity.turnos.OrdenAtencionEntity;
import com.sitra.sitra.exceptions.BadRequestException;
import com.sitra.sitra.exceptions.BusinessRuleException;
import com.sitra.sitra.exceptions.NotFoundException;
import com.sitra.sitra.expose.request.turnos.OrdenAtencionRequest;
import com.sitra.sitra.expose.response.turnos.OrdenAtencionResponse;
import com.sitra.sitra.expose.util.DateConvertUtil;
import com.sitra.sitra.repository.turnos.OrdenAtencionRepository;
import com.sitra.sitra.service.maestros.impl.TablaMaestraServiceImpl;
import com.sitra.sitra.service.seguridad.PersonaService;
import com.sitra.sitra.service.seguridad.UsuarioService;
import com.sitra.sitra.service.turnos.OrdenAtencionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
public class OrdenAtencionServiceImpl implements OrdenAtencionService {

    private final OrdenAtencionRepository ordenAtencionRepository;

    private final PersonaService personaService;
    private final UsuarioService usuarioService;

    @Override
    @Transactional
    public OrdenAtencionResponse save(OrdenAtencionRequest request) {
        String context = "saveOrderAttention";
        log.info("Registrando un nuevo orden de atencion. [ CONTEXTO : {} ]", context);

        PersonaEntity person = personaService.getPersonByID(request.getPersonaId());
        UsuarioEntity user = usuarioService.getUser(request.getReceptorId());

        if (!TablaMaestraServiceImpl.tablePreferential.containsValue(request.getCodPrioridad())) throw new NotFoundException("Recurso no encontrado. [ TABLA_PRIORIDAD ]");

        OrdenAtencionEntity entity = OrdenAtencionRequest.toEntity.apply(request);
        entity.setPersona(person);
        entity.setReceptor(user);

        Integer turno = switch (entity.getCodPrioridad()) {
            case TablaMaestraServiceImpl.NORMAL -> {
                List<OrdenAtencionEntity> list = ordenAtencionRepository.getByCodeStatusAndCodePrio(
                        TablaMaestraServiceImpl.PENDIENTE,
                        TablaMaestraServiceImpl.NORMAL,
                        entity.getFecha()
                );
                yield list.isEmpty() ? 1 : list.getLast().getTurno() + 1;
            }
            case TablaMaestraServiceImpl.PREFERENCIAL -> {
                List<OrdenAtencionEntity> list = ordenAtencionRepository.getByCodeStatusAndCodePrio(
                        TablaMaestraServiceImpl.PENDIENTE,
                        TablaMaestraServiceImpl.PREFERENCIAL,
                        entity.getFecha()
                );
                yield list.isEmpty() ? 1 : list.getLast().getTurno() + 1;
            }
            case TablaMaestraServiceImpl.URGENTE -> {
                List<OrdenAtencionEntity> list = ordenAtencionRepository.getByCodeStatusAndCodePrio(
                        TablaMaestraServiceImpl.PENDIENTE,
                        TablaMaestraServiceImpl.URGENTE,
                        entity.getFecha()
                );
                yield list.isEmpty() ? 1 : list.getLast().getTurno() + 1;
            }
            default -> throw new BusinessRuleException("Codigo Preferencial no registrado");

        };

        entity.setTurno(turno);

        OrdenAtencionEntity saved = ordenAtencionRepository.save(entity);

        return OrdenAtencionResponse.toResponse.apply(saved);
    }

    @Override
    @Transactional
    public OrdenAtencionResponse update(OrdenAtencionRequest request) {
        String context = "updateOrdenAtencion";
        log.info("Actualizando una orden de atencion. [ ORDENATENCION : {} | CONTEXTO : {} ]", request.getOrdenAtencionId(), context);

        OrdenAtencionEntity entity = getOrdenById(request.getOrdenAtencionId());
        if (!TablaMaestraServiceImpl.tableOrderAttentionStatus.containsValue(request.getCodEstadoAtencion())) throw new BadRequestException("El codigo de estado de atencion no esta disponible.");

        OrdenAtencionRequest.toUpdate(request, entity);

        OrdenAtencionEntity saved = ordenAtencionRepository.save(entity);

        return OrdenAtencionResponse.toResponse.apply(saved);
    }

    @Override
    public Page<OrdenAtencionResponse> getPagedAttentionOrdersNormalsInPendingStatus(int page, int size, String date) {
        String context = "getOrdersNormalsInPendingStatus";
        log.info("Obteniendo lista paginada de los ordenes de atencion normales y pendientes. [CONTEXT : {}]", context);

        String codePreferential = TablaMaestraServiceImpl.NORMAL;
        String codeStatus = TablaMaestraServiceImpl.PENDIENTE;
        LocalDate fecha = DateConvertUtil.parseFechaDDMMYYYY(date);

        Pageable pageable = PageRequest.of(page, size, Sort.by("turno").ascending());

        Page<OrdenAtencionEntity> entityPage = ordenAtencionRepository.getOrdersByCodePreferentialAndCodeStatus(codePreferential, codeStatus, fecha, pageable);

        return entityPage.map(OrdenAtencionResponse.toResponseDetailPerson);
    }

    @Override
    public Page<OrdenAtencionResponse> getPagedAttentionOrdersPreferentialInPendingStatus(int page, int size, String date) {
        String context = "getOrdersPreferentialInPendingStatus";
        log.info("Obteniendo lista paginada de los ordenes de atencion preferenciales y pendientes. [CONTEXT : {}]", context);

        String codePreferential = TablaMaestraServiceImpl.PREFERENCIAL;
        String codeStatus = TablaMaestraServiceImpl.PENDIENTE;
        LocalDate fecha = DateConvertUtil.parseFechaDDMMYYYY(date);

        Pageable pageable = PageRequest.of(page, size, Sort.by("turno").ascending());

        Page<OrdenAtencionEntity> entityPage = ordenAtencionRepository.getOrdersByCodePreferentialAndCodeStatus(codePreferential, codeStatus, fecha, pageable);

        return entityPage.map(OrdenAtencionResponse.toResponseDetailPerson);
    }

    @Override
    public List<OrdenAtencionResponse> getAttentionOrderInCallsStatus(String date) {
        String context = "getOrdersInCallsStatus";
        log.info("Obteniendo lista de las ordenes de atencion en estado de llamada y en una determinada fecha. [ STATUS : {} | DATE : {} | CONTEXT : {} ]", "EN_LLAMADA", date, context);

        String codeStatus = TablaMaestraServiceImpl.EN_LLAMADA;
        LocalDate fecha = DateConvertUtil.parseFechaDDMMYYYY(date);

        List<OrdenAtencionEntity> list = ordenAtencionRepository.getByCodeStatusAndDate(codeStatus, fecha);

        return list.stream()
                .map(OrdenAtencionResponse.toResponseDetailPerson)
                .toList();
    }

    @Override
    public OrdenAtencionResponse getById(Long id) {
        String context = "getById";
        log.info("Obteniendo una orden de atencion por su id. [ ORDENATENCION : {} | CONTEXTO : {} ]", id, context);

        if (id == null || id < 1) throw new BadRequestException("Id Incorrecto. [ ORDENATENCION ]");

        OrdenAtencionEntity entity = ordenAtencionRepository.getDetailActiveByID(id)
                .orElseThrow(() -> new NotFoundException("Recurso no encontrado. [ ORDENATENCION ]"));

        return OrdenAtencionResponse.toResponseDetail.apply(entity);
    }

    @Override
    public OrdenAtencionEntity getOrdenById(Long id) {
        if (id == null || id < 1) throw new BadRequestException("Id Incorrecto. [ ORDENATENCION ]");

        return ordenAtencionRepository.getActiveByID(id).orElseThrow(() -> new NotFoundException("Recurso no encontrado. [ ORDENATENCION ]"));
    }

    @Override
    public OrdenAtencionEntity getOrderInCallStatus(String codePriority, LocalDate date) {
        if (codePriority == null || codePriority.length() != 6) throw new BadRequestException("PriorityCode Incorrect. [ OrderAtention ]");

        return ordenAtencionRepository.getOrderInCall(codePriority, date).orElse(null);
    }

    @Override
    public OrdenAtencionEntity getNextOrderInPendingStatus(String codePriority, LocalDate date) {
        if (codePriority == null || codePriority.length() != 6) throw new BadRequestException("PriorityCode Incorrect. [ OrderAtention ]");

        return ordenAtencionRepository.getNextOrderPending(codePriority, date).orElse(null);
    }

    @Override
    public OrdenAtencionEntity getOrderInVentanilla(String codePriority, LocalDate date) {
        if (codePriority == null || codePriority.length() != 6) throw new BadRequestException("PriorityCode Incorrect. [ OrderAtention ]");

        return ordenAtencionRepository.getOrderInVentanilla(codePriority, date).orElse(null);
    }

}
