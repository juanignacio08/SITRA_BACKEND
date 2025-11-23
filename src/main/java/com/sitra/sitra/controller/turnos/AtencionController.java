package com.sitra.sitra.controller.turnos;

import com.sitra.sitra.expose.request.turnos.AtencionRequest;
import com.sitra.sitra.expose.request.turnos.OrdenAtencionRequest;
import com.sitra.sitra.expose.response.base.ResponseDTO;
import com.sitra.sitra.expose.response.turnos.AtencionResponse;
import com.sitra.sitra.expose.response.turnos.OrdenAtencionResponse;
import com.sitra.sitra.expose.util.AppUtil;
import com.sitra.sitra.service.turnos.AtencionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("atencion")
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
public class AtencionController {

    private final AtencionService atencionService;

    @PostMapping("/save")
    public ResponseEntity<ResponseDTO<Object>> save(@RequestBody @Valid AtencionRequest request) {
        try {
            AtencionResponse response = atencionService.save(request);
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "Registro de atencion!");
            log.info("Registro de Atencion!");
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            log.error("Error en save: {}", e.getMessage());
            throw e;
        }
    }

    @PutMapping("/finish")
    public ResponseEntity<ResponseDTO<Object>> finish(@RequestBody @Valid AtencionRequest request) {
        try {
            AtencionResponse response = atencionService.finishAtention(request);
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "Atencion finalizada!");
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            log.error("Error en finish: {}", e.getMessage());
            throw e;
        }
    }
}
