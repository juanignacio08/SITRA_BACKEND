package com.sitra.sitra.controller.maestros;

import com.sitra.sitra.expose.request.maestros.TablaMaestraRequest;
import com.sitra.sitra.expose.response.maestros.TablaMaestraResponse;
import com.sitra.sitra.service.maestros.TablaMaestraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("tablamaestra")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TablaMaestraController {

    private final TablaMaestraService tablaMaestraService;

    @GetMapping("/get/{name}")
    public String prueba(@PathVariable(name = "name") String name) {
        return "Esto es una prueba...";
    }

    @PostMapping("/save")
    public ResponseEntity<Object> save(@RequestBody @Valid TablaMaestraRequest request) {
        TablaMaestraResponse response = tablaMaestraService.save(request);
        return ResponseEntity.ok(response);
    }

}
