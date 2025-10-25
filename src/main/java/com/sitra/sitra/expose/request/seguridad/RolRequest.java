package com.sitra.sitra.expose.request.seguridad;

import com.sitra.sitra.entity.seguridad.RolEntity;
import com.sitra.sitra.expose.util.SecurityUtil;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.function.Function;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RolRequest {

    @NotNull(message = "El id es requerido.")
    @Min(value = 1, message = "El id no debe de ser menor a 1")
    Long rolId;

    @NotBlank(message = "La denominacion es requerido")
    @Size(max = 100, message = "La denominacion debe tener menos de 100 caracteres")
    private String denominacion;

    private int estado;

    public static final Function<RolRequest, RolEntity> toEntity = request -> RolEntity.builder()
            .denominacion(request.getDenominacion())
            .estado(1)
            .actualizadoPor(SecurityUtil.getCurrentUserId())
            .fechaActualizacion(LocalDateTime.now())
            .creadoPor(SecurityUtil.getCurrentUserId())
            .fechaCreacion(LocalDateTime.now())
            .eliminado(false)
            .build();

    public static void toUpdate(RolRequest request, RolEntity entity) {
        entity.setDenominacion(request.getDenominacion());
        entity.setEstado(request.getEstado());
        entity.setActualizadoPor(SecurityUtil.getCurrentUserId());
        entity.setFechaActualizacion(LocalDateTime.now());
    }


}
