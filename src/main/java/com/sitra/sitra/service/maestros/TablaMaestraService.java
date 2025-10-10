package com.sitra.sitra.service.maestros;

import com.sitra.sitra.expose.request.maestros.TablaMaestraRequest;
import com.sitra.sitra.expose.response.maestros.TablaMaestraResponse;

public interface TablaMaestraService {
    TablaMaestraResponse save(TablaMaestraRequest request);
}
