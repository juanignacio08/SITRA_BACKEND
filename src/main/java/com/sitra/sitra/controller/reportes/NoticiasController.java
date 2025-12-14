package com.sitra.sitra.controller.reportes;

import com.sitra.sitra.expose.request.reportes.NoticiasRequest;
import com.sitra.sitra.expose.response.base.ResponseDTO;
import com.sitra.sitra.expose.response.reportes.NoticiasResponse;
import com.sitra.sitra.expose.util.AppUtil;
import com.sitra.sitra.service.reportes.NoticiasService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("noticias")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NoticiasController {
    private final NoticiasService noticiasService;

    @PostMapping("/save")
    public ResponseEntity<ResponseDTO<Object>> save(@RequestBody @Valid NoticiasRequest request) {
        try {
            NoticiasResponse response = noticiasService.save(request);
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "Registro de Noticia!");
            log.info("Registro de noticia!");
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            log.error("Error en save: {}", e.getMessage());
            throw e;
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDTO<Object>> update(@RequestBody @Valid NoticiasRequest request) {
        try {
            NoticiasResponse response = noticiasService.update(request);
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "Noticia actualizada!");
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            log.error("Error en update: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<ResponseDTO<Object>> getAll() {
        try {
            List<NoticiasResponse> response = noticiasService.getAll();
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "Noticias encontradas");
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            log.error("Error en getAll: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/getAllActives")
    public ResponseEntity<ResponseDTO<Object>> getAllActives() {
        try {
            List<NoticiasResponse> response = noticiasService.getAllActives();
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "Noticias encontradas");
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            log.error("Error en getAllActives: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/getById")
    public ResponseEntity<ResponseDTO<Object>> getById(
            @RequestParam(name = "noticia") Long noticia
    ) {
        try {
            NoticiasResponse response = noticiasService.getById(noticia);
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "Noticia encontrada");
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            log.error("Error en getById: {}", e.getMessage());
            throw e;
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDTO<Object>> delete(
            @RequestParam(name = "noticia") Long noticia
    ) {
        try {
            NoticiasResponse response = noticiasService.delete(noticia);
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "Noticia eliminada");
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            log.error("Error en delete: {}", e.getMessage());
            throw e;
        }
    }
}
