package com.sitra.sitra.controller.seguridad;

import com.sitra.sitra.expose.request.seguridad.PersonaRequest;
import com.sitra.sitra.expose.response.base.ResponseDTO;
import com.sitra.sitra.expose.response.seguridad.PersonaResponse;
import com.sitra.sitra.expose.util.AppUtil;
import com.sitra.sitra.service.seguridad.PersonaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping("persona")
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
public class PersonaController {
    
    private final PersonaService personaService;

    private final RestTemplate restTemplate = new RestTemplate();

    String token = "sk_10017.KIsjqlKiWYGTM72N1MLgfVWSDakDxiki";

    @PostMapping("/save")
    public ResponseEntity<ResponseDTO<Object>> save(@RequestBody @Valid PersonaRequest request) {
        try {
            PersonaResponse response = personaService.save(request);
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "Registro de persona!");
            log.info("Registro de persona!");
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            log.error("Error en save: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/getPerson/{numberDocument}")
    public ResponseEntity<ResponseDTO<Object>> getByNumberDocument(@PathVariable(name = "numberDocument") String numberDocument) {
        try {
            PersonaResponse response = personaService.getByNumberDocument(numberDocument);
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "Obteniendo persona! Numero del documneto de identidad: " + numberDocument);
            log.info("Obteniendo persona [ NUMERODOCUMENTOIDENTIDAD : {}]", numberDocument);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            log.error("Error en getByNumberDocument: {}", e.getMessage());
            throw e;
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDTO<Object>> update(@RequestBody @Valid PersonaRequest request) {
        try {
            PersonaResponse response = personaService.update(request);
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "persona actualizada!");
            log.info("Actualizando persona. [ PERSONA : {} ]", request.getNumeroDocumentoIdentidad());
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            log.error("Error en update: {}", e.getMessage());
            throw e;
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDTO<Object>> delete(@RequestParam(name = "numberDocument") String numberDocument) {
        try {
            PersonaResponse response = personaService.deleteByNumberDocument(numberDocument);
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "Persona eliminada: " + numberDocument);
            log.info("Eliminando persona. [ PERSONA : {} ]", numberDocument);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            log.error("Error en delete: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/dni/{numero}")
    public ResponseEntity<?> getDni(@PathVariable String numero) {
        String url = "https://api.decolecta.com/v1/reniec/dni?numero=" + numero;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, String.class
            );
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body("Error al consultar DNI: " + e.getMessage());
        }
    }
    
}
