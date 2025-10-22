package com.sitra.sitra.service.maestros.impl;

import com.sitra.sitra.entity.maestros.TablaMaestraEntity;
import com.sitra.sitra.exceptions.BadRequestException;
import com.sitra.sitra.exceptions.DuplicateKeyError;
import com.sitra.sitra.exceptions.NotFoundException;
import com.sitra.sitra.expose.request.maestros.TablaMaestraRequest;
import com.sitra.sitra.expose.response.maestros.TablaMaestraResponse;
import com.sitra.sitra.repository.maestros.TablaMaestraRepository;
import com.sitra.sitra.service.maestros.TablaMaestraService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

        if (existsByCode(request.getCodigoTabla() + request.getCodigoItem())) throw new DuplicateKeyError("Error! Codigo duplicado.");

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

    @Override
    public TablaMaestraResponse getByCode(String code) {
        context = "getItemByCode";
        log.info("Obteniendo un item de una tabla. [ CODIGOITEM : {} | CONTEXT : {} ]", code, context);

        TablaMaestraEntity entity = getItemByCode(code);

        return TablaMaestraResponse.toResponse.apply(entity);
    }

    @Override
    public TablaMaestraResponse update(TablaMaestraRequest request) {
        context = "updateTablaMaestra";
        log.info("Actualizando un registro de tabla maestra. [ TABLAMAESTRAID : {} | CONTEXTO : {} ]", request.getIdTablaMaestra(), context);

        TablaMaestraEntity entity = getById(request.getIdTablaMaestra());

        TablaMaestraRequest.toUpdate(request, entity);

        TablaMaestraEntity save = tablaMaestraRepository.save(entity);

        return TablaMaestraResponse.toResponse.apply(save);
    }

    @Override
    public String delete(Long id) {
        context = "deleteTablaMaestra";
        log.info("Eliminando un registro de la base de datos. [TABLAMAESTRAID : {} | CONTEXTO : {} ] ", id, context);

        TablaMaestraEntity entity = getById(id);

        entity.setEliminado(true);
        entity.setActualizadoPor(1L);
        entity.setFechaActualizacion(LocalDateTime.now());

        tablaMaestraRepository.save(entity);

        return "Tabla Maestra con id " + id + " fue eliminado.";

    }

    private TablaMaestraEntity getById(Long id) {
        if (id == null || id < 1) throw new BadRequestException("Id incorrecto. [ TABLAMAESTRA ]");

        return tablaMaestraRepository.getByID(id)
                .orElseThrow(() -> new NotFoundException("Recurso no encontrado. [ TABLA MAESTRA ]"));
    }

    private TablaMaestraEntity getItemByCode(String code) {
        if (code == null || code.length() != 6) throw new BadRequestException("Codigo incorrecto. [ TABLAMAESTRA ]");

        return tablaMaestraRepository.getByCode(code)
                .orElseThrow(() -> new NotFoundException("Recurso no encontrado. [ TABLA MAESTRA ]"));
    }

    private boolean existsByCode(String code) {
        if (code == null || code.length() != 6) throw new BadRequestException("Codigo incorrecto. [ TABLAMAESTRA ]");

        return tablaMaestraRepository.existsByCode(code);
    }
}
