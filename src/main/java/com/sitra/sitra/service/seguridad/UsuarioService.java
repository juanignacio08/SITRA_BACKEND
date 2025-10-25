package com.sitra.sitra.service.seguridad;

import com.sitra.sitra.entity.seguridad.UsuarioEntity;
import com.sitra.sitra.expose.request.seguridad.UsuarioRequest;
import com.sitra.sitra.expose.response.seguridad.UsuarioResponse;

import java.util.List;

public interface UsuarioService {
    UsuarioResponse save(UsuarioRequest request);
    UsuarioResponse getById(Long id);
    List<UsuarioResponse> getList();
    UsuarioResponse update(UsuarioRequest request);
    UsuarioResponse delete(Long id);

    UsuarioEntity getUser(Long id);
    List<UsuarioEntity> getUsers();
}
