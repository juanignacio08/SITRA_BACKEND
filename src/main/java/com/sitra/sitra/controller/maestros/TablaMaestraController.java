package com.sitra.sitra.controller.maestros;

import com.sitra.sitra.entity.maestros.TablaMaestraEntity;
import com.sitra.sitra.service.maestros.TablaMaestraService;
import com.sitra.sitra.service.maestros.impl.TablaMaestraServiceImpl;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Template;
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
    public ResponseEntity<Object> save(@RequestBody TablaMaestraEntity entity) {
        TablaMaestraEntity response = tablaMaestraService.save(entity);
        return ResponseEntity.ok(response);
    }

}
