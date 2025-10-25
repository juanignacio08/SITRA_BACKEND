package com.sitra.sitra.controller.seguridad;

import com.sitra.sitra.expose.request.seguridad.RolRequest;
import com.sitra.sitra.expose.response.base.ResponseDTO;
import com.sitra.sitra.expose.response.seguridad.RolResponse;
import com.sitra.sitra.expose.util.AppUtil;
import com.sitra.sitra.service.seguridad.RolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("rol")
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
public class RolController {

    private final RolService rolService;

    @PostMapping("/save")
    public ResponseEntity<ResponseDTO<Object>> save(@RequestBody @Valid RolRequest request) {
        try {
            RolResponse response = rolService.save(request);
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "Registro de rol!");
            log.info("Registro de rol!");
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            log.error("Error en save: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/getRol/{id}")
    public ResponseEntity<ResponseDTO<Object>> getById(@PathVariable(name = "id") Long id) {
        try {
            RolResponse response = rolService.getById(id);
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "Obteniendo rol!");
            log.info("Obteniendo rol [ ROL : {} ]", id);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            log.error("Error en getById: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/getRols")
    public ResponseEntity<ResponseDTO<Object>> getRols() {
        try {
            List<RolResponse> list = rolService.getList();
            ResponseDTO<Object> responseDTO = AppUtil.build(list, "Obteniendo roles!");
            log.info("Obteniendo roles");
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            log.error("Error en getRols: {}", e.getMessage());
            throw e;
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDTO<Object>> update(@RequestBody @Valid RolRequest request) {
        try {
            RolResponse response = rolService.update(request);
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "Rol actualizado!");
            log.info("Actualizando rol. [ ROL: {} ]", request.getRolId());
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            log.error("Error en update: {}", e.getMessage());
            throw e;
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDTO<Object>> delete(@RequestParam(name = "id") Long id) {
        try {
            RolResponse response = rolService.delete(id);
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "Rol eliminado: " + id);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            log.error("Error en delete: {}", e.getMessage());
            throw e;
        }
    }
}
