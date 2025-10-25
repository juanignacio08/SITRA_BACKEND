package com.sitra.sitra.service.seguridad;

import com.sitra.sitra.entity.seguridad.RolEntity;
import com.sitra.sitra.expose.request.seguridad.RolRequest;
import com.sitra.sitra.expose.response.seguridad.RolResponse;

import java.util.List;

public interface RolService {
    RolResponse save(RolRequest request);
    RolResponse getById(Long id);
    List<RolResponse> getList();
    RolResponse update(RolRequest request);
    RolResponse delete(Long id);

    RolEntity getRol(Long id);
    List<RolEntity> getRols();
    boolean existsRol(Long id);
}
