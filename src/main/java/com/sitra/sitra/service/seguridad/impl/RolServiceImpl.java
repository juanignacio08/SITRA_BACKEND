package com.sitra.sitra.service.seguridad.impl;

import com.sitra.sitra.expose.request.seguridad.RolRequest;
import com.sitra.sitra.expose.response.seguridad.RolResponse;
import com.sitra.sitra.service.seguridad.RolService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
public class RolServiceImpl implements RolService {
    @Override
    public RolResponse save(RolRequest request) {
        return null;
    }

    @Override
    public RolResponse getById(Long id) {
        return null;
    }

    @Override
    public List<RolResponse> getList() {
        return List.of();
    }

    @Override
    public RolResponse update(RolRequest request) {
        return null;
    }

    @Override
    public RolResponse delete(Long id) {
        return null;
    }
}
