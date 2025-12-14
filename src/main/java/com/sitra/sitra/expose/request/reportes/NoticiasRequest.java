package com.sitra.sitra.expose.request.reportes;

import com.sitra.sitra.entity.reportes.NoticiasEntity;
import com.sitra.sitra.expose.util.SecurityUtil;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDateTime;
import java.util.function.Function;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class NoticiasRequest {

    @NotNull(message = "El Id es requerido")
    private Long noticiasId;

    @NotBlank(message = "El contenido de la noticia es requerido")
    private String contenido; // TEXT NOT NULL

    @NotNull(message = "El estado es requerido")
    @Range(min = 0, max = 1, message = "El estado debe de estar en el rango [ 0 - 1]")
    private Integer estado; // INT NOT NULL

    public static final Function<NoticiasRequest, NoticiasEntity> toEntity = request -> NoticiasEntity.builder()
            .contenido(request.getContenido())
            .estado(request.getEstado())
            .actualizadoPor(SecurityUtil.getCurrentUserId())
            .fechaActualizacion(LocalDateTime.now())
            .creadoPor(SecurityUtil.getCurrentUserId())
            .fechaCreacion(LocalDateTime.now())
            .eliminado(false)
            .build();

    public static void toUpdate(NoticiasRequest request, NoticiasEntity entity) {
        entity.setContenido(request.getContenido());
        entity.setEstado(request.getEstado());
        entity.setActualizadoPor(SecurityUtil.getCurrentUserId());
        entity.setFechaActualizacion(LocalDateTime.now());
    }

}