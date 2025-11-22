package com.sitra.sitra.controller.turnos;

import com.sitra.sitra.expose.request.turnos.LlamadaRequest;
import com.sitra.sitra.expose.response.base.ResponseDTO;
import com.sitra.sitra.expose.response.turnos.LlamadaResponse;
import com.sitra.sitra.expose.util.AppUtil;
import com.sitra.sitra.service.turnos.LlamadaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("llamada")
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
public class LlamadaController {

    private final LlamadaService llamadaService;

    @PostMapping("/save")
    public ResponseEntity<ResponseDTO<Object>> save(@RequestBody @Valid LlamadaRequest request) {
        try {
            LlamadaResponse response = llamadaService.save(request);
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "Registro de una llamada!");
            log.info("Registro de Llamada!");
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            log.error("Error en save: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/callNext")
    public ResponseEntity<ResponseDTO<Object>> callNext(
            @RequestParam(name = "date") String date,
            @RequestParam(name = "codePriority") String codePriority,
            @RequestParam(name = "codeVentanilla") String codeVentanilla,
            @RequestParam(name = "asesorId") Long asesorId
    ) {
        try {
            LlamadaResponse response = llamadaService.callNext(date, codePriority, codeVentanilla, asesorId);
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "Llamando al siguiente!");
            log.info("Obteniendo siguiente Orden de Atencion en estado llamando");
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            log.error("Error en callNext: {}", e.getMessage());
            throw e;
        }
    }

    @PutMapping("/markAsAbsent")
    public ResponseEntity<ResponseDTO<Object>> markAsAbsent(
            @RequestParam(name = "llamadaId") Long llamadaId
    ) {
        try {
            LlamadaResponse response = llamadaService.markAsAbsent(llamadaId);
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "Llamada marcada como ausente!");
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            log.error("Error en markAsAbsent: {}", e.getMessage());
            throw e;
        }
    }
}
