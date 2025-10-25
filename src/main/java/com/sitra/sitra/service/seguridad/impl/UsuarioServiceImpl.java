package com.sitra.sitra.service.seguridad.impl;

import com.sitra.sitra.entity.seguridad.UsuarioEntity;
import com.sitra.sitra.expose.request.seguridad.UsuarioRequest;
import com.sitra.sitra.expose.response.seguridad.UsuarioResponse;
import com.sitra.sitra.repository.seguridad.UsuarioRepository;
import com.sitra.sitra.service.seguridad.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private String context;

    @Override
    @Transactional
    public UsuarioResponse save(UsuarioRequest request) {
        return null;
    }

    @Override
    public UsuarioResponse getById(Long id) {
        return null;
    }

    @Override
    public List<UsuarioResponse> getList() {
        return List.of();
    }

    @Override
    @Transactional
    public UsuarioResponse update(UsuarioRequest request) {
        return null;
    }

    @Override
    @Transactional
    public UsuarioResponse delete(Long id) {
        return null;
    }

    @Override
    public UsuarioEntity getUser(Long id) {
        return null;
    }

    @Override
    public List<UsuarioEntity> getUsers() {
        return List.of();
    }
}
