package com.sitra.sitra.service.reportes;

import com.sitra.sitra.expose.request.reportes.NoticiasRequest;
import com.sitra.sitra.expose.response.reportes.NoticiasResponse;

import java.util.List;

public interface NoticiasService {
    NoticiasResponse save(NoticiasRequest request);
    NoticiasResponse update(NoticiasRequest request);
    List<NoticiasResponse> getAll();
    List<NoticiasResponse> getAllActives();
    NoticiasResponse getById(Long id);
    NoticiasResponse delete(Long id);

}
