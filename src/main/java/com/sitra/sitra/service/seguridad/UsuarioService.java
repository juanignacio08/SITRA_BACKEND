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
    UsuarioResponse sigIn(String user, String password);
    UsuarioResponse sigInDefault(String user, String password);

    UsuarioEntity getUserDetail(Long id);
    UsuarioEntity getUser(Long id);
    UsuarioEntity getUserByUser(String user);
    List<UsuarioEntity> getUsers();
    boolean existsUserByNumberDocument(String email);
}
