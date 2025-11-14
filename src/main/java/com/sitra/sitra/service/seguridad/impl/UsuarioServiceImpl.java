package com.sitra.sitra.service.seguridad.impl;

import com.sitra.sitra.entity.seguridad.PersonaEntity;
import com.sitra.sitra.entity.seguridad.RolEntity;
import com.sitra.sitra.entity.seguridad.UsuarioEntity;
import com.sitra.sitra.exceptions.BadRequestException;
import com.sitra.sitra.exceptions.DuplicateKeyError;
import com.sitra.sitra.exceptions.NotFoundException;
import com.sitra.sitra.expose.request.seguridad.UsuarioRequest;
import com.sitra.sitra.expose.response.seguridad.UsuarioResponse;
import com.sitra.sitra.expose.util.PasswordUtil;
import com.sitra.sitra.expose.util.SecurityUtil;
import com.sitra.sitra.repository.seguridad.UsuarioRepository;
import com.sitra.sitra.service.seguridad.PersonaService;
import com.sitra.sitra.service.seguridad.RolService;
import com.sitra.sitra.service.seguridad.UsuarioService;
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
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PersonaService personaService;
    private final RolService rolService;

    private String context;

    @Override
    @Transactional
    public UsuarioResponse save(UsuarioRequest request) {
        context = "saveUser";
        log.info("Registrando un nuevo usuario. [ CONTEXTO : {} ]", context);

        PersonaEntity persona = personaService.getPersonByNumberDocument(request.getNumeroDocumento());
        RolEntity rol = rolService.getRol(request.getRolId());

        if (existsUserByNumberDocument(request.getNumeroDocumento())) throw new DuplicateKeyError("El usuario ingresado ya existe!");

        UsuarioEntity entity = UsuarioRequest.toEntity.apply(request);
        entity.setPersona(persona);
        entity.setRol(rol);

        UsuarioEntity save = usuarioRepository.save(entity);

        return UsuarioResponse.toResponse.apply(save);
    }

    @Override
    public UsuarioResponse getById(Long id) {
        context = "getUserById";
        log.info("Buscando un usuario. [ USER : {} | CONTEXTO : {} ]", id, context);

        UsuarioEntity entity = getUserDetail(id);

        return UsuarioResponse.toResponseDetail.apply(entity);
    }

    @Override
    public List<UsuarioResponse> getList() {
        context = "getUsers";
        log.info("Buscando un usuarios. [ CONTEXTO : {} ]", context);

        List<UsuarioEntity> list = getUsers();

        return list.stream()
                .map(UsuarioResponse.toResponseDetail)
                .toList();
    }

    @Override
    @Transactional
    public UsuarioResponse update(UsuarioRequest request) {
        context = "updateUser";
        log.info("Actualizando el registro de un usuario. [ USUARIO : {} | CONTEXTO : {} ]", request.getUsuarioId(), context);

        UsuarioEntity entity = getUser(request.getUsuarioId());

        UsuarioRequest.toUpdate(request, entity);

        RolEntity rol = rolService.getRol(request.getRolId());

        entity.setRol(rol);

        UsuarioEntity save = usuarioRepository.save(entity);

        return UsuarioResponse.toResponse.apply(save);
    }

    @Override
    @Transactional
    public UsuarioResponse delete(Long id) {
        context = "deleteUser";
        log.info("Eliminando un usuario. [ USER : {} | CONTEXTO : {} ]", id, context);

        UsuarioEntity entity = getUser(id);

        entity.setEliminado(true);
        entity.setActualizadoPor(SecurityUtil.getCurrentUserId());
        entity.setFechaActualizacion(LocalDateTime.now());

        UsuarioEntity save = usuarioRepository.save(entity);
        return UsuarioResponse.toResponse.apply(save);
    }

    @Override
    public UsuarioResponse sigIn(String user, String password) {
        context = "getByUserAndPassword";
        log.info("Obteniendo un usuario por Email y Password. [ USER : {} | PASSWORD : {} | CONTEXTO : {} ]", user, password, context);

        UsuarioEntity entity = getUserByUser(user);

        if (!PasswordUtil.comparar(password, entity.getContrasena())) throw new NotFoundException("Usuario no encontrado.");

        return UsuarioResponse.toResponse.apply(entity);
    }

    @Override
    public UsuarioEntity getUserDetail(Long id) {
        if (id == null || id < 1) throw new BadRequestException("Id incorrecto. [ Usuario ]");

        return usuarioRepository.getDetailByID(id)
                .orElseThrow(() -> new NotFoundException("Registro no encontrado. [ Usuario ]"));
    }

    @Override
    public UsuarioEntity getUser(Long id) {
        if (id == null || id < 1) throw new BadRequestException("Id incorrecto. [ Usuario ]");

        return usuarioRepository.getByID(id)
                .orElseThrow(() -> new NotFoundException("Registro no encontrado. [ Usuario ]"));
    }

    @Override
    public UsuarioEntity getUserByUser(String user) {
        return usuarioRepository.getByUser(user)
                .orElseThrow(() -> new NotFoundException("Registro no encontrado. [ Usuario ]"));
    }

    @Override
    public List<UsuarioEntity> getUsers() {
        return usuarioRepository.getUsers();
    }

    @Override
    public boolean existsUserByNumberDocument(String numberDocument) {
        return usuarioRepository.existsUserByNumberDocument(numberDocument);
    }
}
