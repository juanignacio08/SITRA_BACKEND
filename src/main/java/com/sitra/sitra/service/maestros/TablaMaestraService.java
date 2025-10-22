package com.sitra.sitra.service.maestros;

import com.sitra.sitra.expose.request.maestros.TablaMaestraRequest;
import com.sitra.sitra.expose.response.maestros.TablaMaestraResponse;

import java.util.List;

public interface TablaMaestraService {
    TablaMaestraResponse save(TablaMaestraRequest request);
    List<TablaMaestraResponse> getItems(String codeTable);
    TablaMaestraResponse getByCode(String code);
    TablaMaestraResponse update(TablaMaestraRequest request);
    String delete(Long id);
}
