package com.sitra.sitra.service.seguridad.impl;

import com.sitra.sitra.entity.seguridad.RolEntity;
import com.sitra.sitra.exceptions.BadRequestException;
import com.sitra.sitra.exceptions.NotFoundException;
import com.sitra.sitra.expose.request.seguridad.RolRequest;
import com.sitra.sitra.expose.response.seguridad.RolResponse;
import com.sitra.sitra.expose.util.SecurityUtil;
import com.sitra.sitra.repository.seguridad.RolRepository;
import com.sitra.sitra.service.seguridad.RolService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
public class RolServiceImpl implements RolService {

    private final RolRepository rolRepository;

    private String context;

    @Override
    @Transactional
    public RolResponse save(RolRequest request) {
        context = "saveRol";
        log.info("Registrando un nuevo rol. [ CONTEXTO : {} ]", context);

        RolEntity entity = RolRequest.toEntity.apply(request);

        RolEntity save = rolRepository.save(entity);

        return RolResponse.toResponse.apply(save);
    }

    @Override
    public RolResponse getById(Long id) {
        context = "getByIdRol";
        log.info("Buscando un rol. [ ROL : {} | CONTEXTO : {} ]", id, context);

        RolEntity entity = getRol(id);

        return RolResponse.toResponse.apply(entity);
    }

    @Override
    public List<RolResponse> getList() {
        context = "getRols";
        log.info("Buscando roles. [ CONTEXTO : {} ]", context);

        List<RolEntity> list = getRols();

        return list.stream()
                .map(RolResponse.toResponse)
                .toList();
    }

    @Override
    @Transactional
    public RolResponse update(RolRequest request) {
        context = "updateRol";
        log.info("Actualizando un rol. [ ROL : {} | CONTEXTO : {} ]", request.getRolId(), context);

        RolEntity entity = getRol(request.getRolId());

        RolRequest.toUpdate(request, entity);

        RolEntity save = rolRepository.save(entity);

        return RolResponse.toResponse.apply(save);
    }

    @Override
    @Transactional
    public RolResponse delete(Long id) {
        context = "deleteRol";
        log.info("Eliminando rol. [ ROL : {} | CONTEXTO : {} ]", id, context);

        RolEntity entity = getRol(id);
        entity.setEliminado(true);
        entity.setActualizadoPor(SecurityUtil.getCurrentUserId());
        entity.setFechaActualizacion(LocalDateTime.now());

        RolEntity save = rolRepository.save(entity);

        return RolResponse.toResponse.apply(save);
    }

    @Override
    public RolEntity getRol(Long id) {
        if (id == null || id < 1) throw new BadRequestException("Id incorrecto. [ ROL ]");

        return rolRepository.getByID(id)
                .orElseThrow(() -> new NotFoundException("Recurso no encontrado. [ ROL ]"));
    }

    @Override
    public List<RolEntity> getRols() {
        return rolRepository.getRols();
    }

    @Override
    public boolean existsRol(Long id) {
        if (id == null || id < 1) throw new BadRequestException("Id incorrecto. [ ROL ]");

        return rolRepository.existsRol(id);
    }
}
