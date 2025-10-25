package com.sitra.sitra.expose.response.seguridad;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sitra.sitra.entity.seguridad.RolEntity;
import com.sitra.sitra.expose.request.seguridad.RolRequest;
import lombok.*;

import java.util.function.Function;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RolResponse {
    private Long rolId;
    private String denominacion;
    private int estado;

    public static final Function<RolEntity, RolResponse> toResponse = entity -> RolResponse.builder()
            .rolId(entity.getRolId())
            .denominacion(entity.getDenominacion())
            .estado(entity.getEstado())
            .build();
}
