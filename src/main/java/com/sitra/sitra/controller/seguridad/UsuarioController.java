package com.sitra.sitra.controller.seguridad;

import com.sitra.sitra.expose.request.seguridad.UsuarioRequest;
import com.sitra.sitra.expose.response.base.ResponseDTO;
import com.sitra.sitra.expose.response.seguridad.UsuarioResponse;
import com.sitra.sitra.expose.util.AppUtil;
import com.sitra.sitra.service.seguridad.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("usuario")
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
public class UsuarioController {
    
    private final UsuarioService usuarioService;

    @PostMapping("/save")
    public ResponseEntity<ResponseDTO<Object>> save(@RequestBody @Valid UsuarioRequest request) {
        try {
            UsuarioResponse response = usuarioService.save(request);
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "Registro de usuario!");
            log.info("Registro de usuario!");
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            log.error("Error en save: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/getUsuario/{id}")
    public ResponseEntity<ResponseDTO<Object>> getById(@PathVariable(name = "id") Long id) {
        try {
            UsuarioResponse response = usuarioService.getById(id);
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "Obteniendo usuario!");
            log.info("Obteniendo usuario [ usuario : {} ]", id);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            log.error("Error en getById: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/getUsuarios")
    public ResponseEntity<ResponseDTO<Object>> getUsuarios() {
        try {
            List<UsuarioResponse> list = usuarioService.getList();
            ResponseDTO<Object> responseDTO = AppUtil.build(list, "Obteniendo usuarioes!");
            log.info("Obteniendo usuarioes");
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            log.error("Error en getUsuarios: {}", e.getMessage());
            throw e;
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDTO<Object>> update(@RequestBody @Valid UsuarioRequest request) {
        try {
            UsuarioResponse response = usuarioService.update(request);
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "usuario actualizado!");
            log.info("Actualizando usuario. [ usuario: {} ]", request.getUsuarioId());
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            log.error("Error en update: {}", e.getMessage());
            throw e;
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDTO<Object>> delete(@RequestParam(name = "id") Long id) {
        try {
            UsuarioResponse response = usuarioService.delete(id);
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "usuario eliminado: " + id);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            log.error("Error en delete: {}", e.getMessage());
            throw e;
        }
    }
}
