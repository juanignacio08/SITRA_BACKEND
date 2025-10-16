package com.sitra.sitra.controller.maestros;

import com.sitra.sitra.expose.request.maestros.TablaMaestraRequest;
import com.sitra.sitra.expose.response.base.ResponseDTO;
import com.sitra.sitra.expose.response.maestros.TablaMaestraResponse;
import com.sitra.sitra.expose.util.AppUtil;
import com.sitra.sitra.service.maestros.TablaMaestraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("tablamaestra")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TablaMaestraController {

    private final TablaMaestraService tablaMaestraService;

    @PostMapping("/save")
    public ResponseEntity<ResponseDTO<Object>> save(@RequestBody @Valid TablaMaestraRequest request) {
        try {
            TablaMaestraResponse response = tablaMaestraService.save(request);
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "Registro de tabla maestra!");
            log.info("Registro de tabla maestra!");
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            log.error("Error en save: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/getItems/{codeTable}")
    public ResponseEntity<ResponseDTO<Object>> getItems(@PathVariable(name = "codeTable") String codeTable) {

        if (codeTable.length() != 3) throw new IllegalArgumentException("Error en el codigo de la tabla: " + codeTable);

        try {
            List<TablaMaestraResponse> responseList = tablaMaestraService.getItems(codeTable);
            ResponseDTO<Object> responseDTO = AppUtil.build(responseList, "Obteniendo items de tabla maestra! CodigoTabla : " + codeTable);
            log.info("Obteniendo items de tabla maestra [ CODIGOTABLA : {}]", codeTable);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            log.error("Error en getItems: {}", e.getMessage());
            throw e;
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDTO<Object>> update(@RequestBody @Valid TablaMaestraRequest request) {
        try {
            TablaMaestraResponse response = tablaMaestraService.update(request);
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "Tabla maestra actualizada!");
            log.info("Actualizando tabla maestra. [ TABLAMAESTRA : {} ]", request.getIdTablaMaestra());
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            log.error("Error en update: {}", e.getMessage());
            throw e;
        }
    }

    @PutMapping("/delete")
    public ResponseEntity<ResponseDTO<Object>> delete(@RequestParam(name = "tablaMaestraID") Long id) {
        try {
            String mensaje = tablaMaestraService.delete(id);
            ResponseDTO<Object> responseDTO = AppUtil.build(mensaje, mensaje);
            log.info("Eliminando tabla maestra. [ TABLAMAESTRA : {} ]", id);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            log.error("Error en delete: {}", e.getMessage());
            throw e;
        }
    }
}
