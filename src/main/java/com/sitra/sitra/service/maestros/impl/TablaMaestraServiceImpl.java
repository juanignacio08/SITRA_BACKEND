package com.sitra.sitra.service.maestros.impl;

import com.sitra.sitra.entity.maestros.TablaMaestraEntity;
import com.sitra.sitra.expose.request.maestros.TablaMaestraRequest;
import com.sitra.sitra.expose.response.maestros.TablaMaestraResponse;
import com.sitra.sitra.repository.maestros.TablaMaestraRepository;
import com.sitra.sitra.service.maestros.TablaMaestraService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TablaMaestraServiceImpl implements TablaMaestraService {

    private final TablaMaestraRepository tablaMaestraRepository;

    private String context;

    @Override
    @Transactional
    public TablaMaestraResponse save(TablaMaestraRequest request) {
        context = "saveTablaMaestra";
        log.info("Registrando un nuevo registro de tabla maestra. [ CONTEXTO : {} ]", context);

        TablaMaestraEntity entity = TablaMaestraRequest.toEntity.apply(request);

        TablaMaestraEntity save = tablaMaestraRepository.save(entity);

        return TablaMaestraResponse.toResponse.apply(save);
    }

    @Override
    public List<TablaMaestraResponse> getItems(String codeTable) {
        context = "getItemsTablaMaestra";
        log.info("Buscando items de una tabla, en tabla maestra. [ CODIGOTABLA : {} | CONTEXT : {} ]", codeTable, context);

        List<TablaMaestraEntity> list = tablaMaestraRepository.getItems(codeTable);

        return list.stream().map(TablaMaestraResponse.toResponse).toList();
    }
}
