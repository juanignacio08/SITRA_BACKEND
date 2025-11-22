package com.sitra.sitra.expose.request.turnos;

import com.sitra.sitra.entity.turnos.OrdenAtencionEntity;
import com.sitra.sitra.expose.util.SecurityUtil;
import com.sitra.sitra.service.maestros.impl.TablaMaestraServiceImpl;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.function.Function;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrdenAtencionRequest {

    @NotNull(message = "El id es requerido.")
    @Min(value = 1, message = "El id no debe de ser menor a 1")
    private Long ordenAtencionId;

    @NotNull(message = "El idPersona es requerido.")
    @Min(value = 1, message = "El idPersona no debe de ser menor a 1")
    private Long personaId;

    @NotNull(message = "El idReceptor es requerido.")
    @Min(value = 1, message = "El idReceptor no debe de ser menor a 1")
    private Long receptorId;

    @Size(min = 6, max = 6, message = "El codigo de prioridad debe tener exactamente 6 caracteres")
    private String codPrioridad;

    @NotBlank(message = "El codigo de estado de atencion es requerido.")
    @Size(min = 6, max = 6, message = "El codigo de prioridad debe tener exactamente 6 caracteres")
    private String codEstadoAtencion;

    @NotNull(message = "El estado es requerido.")
    private int estado;

    public static final Function<OrdenAtencionRequest, OrdenAtencionEntity> toEntity = request -> OrdenAtencionEntity.builder()
            .fecha(LocalDate.now())
            .hora(LocalTime.now())
            .codPrioridad(request.getCodPrioridad())
            .codEstadoAtencion(TablaMaestraServiceImpl.PENDIENTE)
            .estado(1)
            .actualizadoPor(SecurityUtil.getCurrentUserId())
            .fechaActualizacion(LocalDateTime.now())
            .creadoPor(SecurityUtil.getCurrentUserId())
            .fechaCreacion(LocalDateTime.now())
            .eliminado(false)
            .build();

    public static void toUpdate(OrdenAtencionRequest request, OrdenAtencionEntity entity) {
        entity.setCodEstadoAtencion(request.getCodEstadoAtencion());
        entity.setEstado(request.getEstado());
        entity.setActualizadoPor(SecurityUtil.getCurrentUserId());
        entity.setFechaActualizacion(LocalDateTime.now());
    }

}
