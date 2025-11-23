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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TablaMaestraServiceImpl implements TablaMaestraService {

    private final TablaMaestraRepository tablaMaestraRepository;

    public static Map<String, String> tablePreferential = new HashMap<>(Map.of(
            "NORMAL", "001001",
            "PREFERENCIAL", "001002",
            "URGENTE", "001003"
    ));

    public static final String NORMAL = "001001";
    public static final String PREFERENCIAL = "001002";
    public static final String URGENTE = "001003";

    public static Map<String, String> tableOrderAttentionStatus = new HashMap<>(Map.of(
            "PENDIENTE", "002001",
            "EN_LLAMADA", "002002",
            "ATENDIENDO", "002003",
            "AUSENTE", "002004",
            "ATENDIDO", "002005"
    ));

    public static final String PENDIENTE = "002001";
    public static final String EN_LLAMADA = "002002";
    public static final String ATENDIENDO = "002003";
    public static final String AUSENTE = "002004";
    public static final String ATENDIDO = "002005";

    public static Map<String, String> tableCodeVentanilla = new HashMap<>(Map.of(
            "VENTANILLA_1", "003001",
            "VENTANILLA_2", "003002"
    ));

    public static Map<String, String> tableCodeResultadoAtencion = new HashMap<>(Map.of(
            "PENDIENTE", "005001",
            "SE_PRESENTO", "005002",
            "NO_RESPONDIO", "005003",
            "ATENDIDO", "005004"
    ));

    public static final String PENDIENTE_LLAMADA = "005001";
    public static final String SE_PRESENTO = "005002";
    public static final String NO_RESPONDIO = "005003";
    public static final String ATENDIDO_LLAMADA = "005004";

    @Override
    @Transactional
    public TablaMaestraResponse save(TablaMaestraRequest request) {
        String context = "saveTablaMaestra";
        log.info("Registrando un nuevo registro de tabla maestra. [ CONTEXTO : {} ]", context);

        if (existsByCode(request.getCodigoTabla() + request.getCodigoItem())) throw new DuplicateKeyError("Error! Codigo duplicado.");

        TablaMaestraEntity entity = TablaMaestraRequest.toEntity.apply(request);

        TablaMaestraEntity save = tablaMaestraRepository.save(entity);

        return TablaMaestraResponse.toResponse.apply(save);
    }

    @Override
    public List<TablaMaestraResponse> getItems(String codeTable) {
        String context = "getItemsTablaMaestra";
        log.info("Buscando items de una tabla, en tabla maestra. [ CODIGOTABLA : {} | CONTEXT : {} ]", codeTable, context);

        List<TablaMaestraEntity> list = tablaMaestraRepository.getItems(codeTable);
        return list.stream().map(TablaMaestraResponse.toResponse).toList();
    }

    @Override
    public TablaMaestraResponse getByCode(String code) {
        String context = "getItemByCode";
        log.info("Obteniendo un item de una tabla. [ CODIGOITEM : {} | CONTEXT : {} ]", code, context);

        TablaMaestraEntity entity = getItemByCode(code);

        return TablaMaestraResponse.toResponse.apply(entity);
    }

    @Override
    public TablaMaestraResponse update(TablaMaestraRequest request) {
        String context = "updateTablaMaestra";
        log.info("Actualizando un registro de tabla maestra. [ TABLAMAESTRAID : {} | CONTEXTO : {} ]", request.getIdTablaMaestra(), context);

        TablaMaestraEntity entity = getById(request.getIdTablaMaestra());

        TablaMaestraRequest.toUpdate(request, entity);

        TablaMaestraEntity save = tablaMaestraRepository.save(entity);

        return TablaMaestraResponse.toResponse.apply(save);
    }

    @Override
    public String delete(Long id) {
        String context = "deleteTablaMaestra";
        log.info("Eliminando un registro de la base de datos. [TABLAMAESTRAID : {} | CONTEXTO : {} ] ", id, context);

        TablaMaestraEntity entity = getById(id);

        entity.setEliminado(true);
        entity.setActualizadoPor(1L);
        entity.setFechaActualizacion(LocalDateTime.now());

        tablaMaestraRepository.save(entity);

        return "Tabla Maestra con id " + id + " fue eliminado.";

    }

    @Override
    public TablaMaestraEntity getByCodeRegister(String code) {
        return getItemByCode(code);
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
