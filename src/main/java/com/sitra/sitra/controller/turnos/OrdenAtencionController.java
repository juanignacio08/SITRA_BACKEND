package com.sitra.sitra.controller.turnos;

import com.sitra.sitra.expose.request.turnos.OrdenAtencionRequest;
import com.sitra.sitra.expose.response.base.ConsultaResponseDTO;
import com.sitra.sitra.expose.response.base.ResponseDTO;
import com.sitra.sitra.expose.response.turnos.OrdenAtencionResponse;
import com.sitra.sitra.expose.util.AppUtil;
import com.sitra.sitra.service.turnos.OrdenAtencionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("ordenAtencion")
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
public class OrdenAtencionController {

    private final OrdenAtencionService ordenAtencionService;

    @PostMapping("/save")
    public ResponseEntity<ResponseDTO<Object>> save(@RequestBody @Valid OrdenAtencionRequest request) {
        try {
            OrdenAtencionResponse response = ordenAtencionService.save(request);
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "Registro de orden de atencion!");
            log.info("Registro de Orden de atencion!");
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            log.error("Error en save: {}", e.getMessage());
            throw e;
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDTO<Object>> update(@RequestBody @Valid OrdenAtencionRequest request) {
        try {
            OrdenAtencionResponse response = ordenAtencionService.update(request);
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "Orden Atencion actualizada!");
            log.info("Actualizando Orden de Atencion. [ ORDEN-ATENCION : {} ]", request.getOrdenAtencionId());
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            log.error("Error en update: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/getNormalPaginated")
    public ResponseEntity<ConsultaResponseDTO> getNormalPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(name = "date") String fecha
    ) {
        try {
            if (page < 0) page = 0;
            if (size < 1 || size > 100) size = 10;

            Page<OrdenAtencionResponse> responsePage = ordenAtencionService.getPagedAttentionOrdersNormalsInPendingStatus(page, size, fecha);

            if (page >= responsePage.getTotalPages() && responsePage.getTotalPages() > 0) throw new IllegalArgumentException("El número de página solicitado (" + page + ") está fuera del rango permitido (0 - " + (responsePage.getTotalPages() - 1) + ")");

            ConsultaResponseDTO responseDTO = AppUtil.buildPaginated(responsePage, "Orden de atencion paginada!");
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            log.error("Error en getPaginatedNormal: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/getPreferentialPaginated")
    public ResponseEntity<ConsultaResponseDTO> getPreferentialPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(name = "date") String fecha
    ) {
        try {
            if (page < 0) page = 0;
            if (size < 1 || size > 100) size = 10;

            Page<OrdenAtencionResponse> responsePage = ordenAtencionService.getPagedAttentionOrdersPreferentialInPendingStatus(page, size, fecha);

            if (page >= responsePage.getTotalPages() && responsePage.getTotalPages() > 0) throw new IllegalArgumentException("El número de página solicitado (" + page + ") está fuera del rango permitido (0 - " + (responsePage.getTotalPages() - 1) + ")");

            ConsultaResponseDTO responseDTO = AppUtil.buildPaginated(responsePage, "Orden de atencion paginada!");
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            log.error("Error en getPaginatedPreferential: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/getAttentionOrderInCallsStatus")
    public ResponseEntity<ResponseDTO<Object>> getAttentionOrderInCallsStatus(@RequestParam(name = "date") String date) {
        try {
            List<OrdenAtencionResponse> list = ordenAtencionService.getAttentionOrderInCallsStatus(date);
            ResponseDTO<Object> responseDTO = AppUtil.build(list, "Obteniendo Orden de atencion en estado llamando!");
            log.info("Obteniendo Orden de Atencion en estado llamando");
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            log.error("Error en getAttentionOrderInCallsStatus: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/getById")
    public ResponseEntity<ResponseDTO<Object>> getById(@RequestParam(name = "id") Long id) {
        try {
            OrdenAtencionResponse response = ordenAtencionService.getById(id);
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "Obteniendo Orden de atencion!");
            log.info("Obteniendo Orden de Atencion por Id. [ ORDENATENCION : {} ]", id);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            log.error("Error en getById: {}", e.getMessage());
            throw e;
        }
    }

}