package com.sitra.sitra.expose.response.reportes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sitra.sitra.entity.reportes.NoticiasEntity;
import lombok.*;

import java.util.function.Function;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NoticiasResponse {

    private Long noticiasId;
    private String contenido;
    private Integer estado;

    public static final Function<NoticiasEntity, NoticiasResponse> toResponse = entity -> NoticiasResponse.builder()
            .noticiasId(entity.getNoticiasId())
            .contenido(entity.getContenido())
            .estado(entity.getEstado())
            .build();
}