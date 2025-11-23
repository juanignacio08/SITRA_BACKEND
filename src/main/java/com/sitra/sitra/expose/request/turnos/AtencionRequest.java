package com.sitra.sitra.expose.request.turnos;

import com.sitra.sitra.entity.turnos.AtencionEntity;
import com.sitra.sitra.expose.util.SecurityUtil;
import jakarta.validation.constraints.*;
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
public class AtencionRequest {

    @NotNull(message = "El id es requerido")
    @Min(value = 1, message = "El id debe de ser mayor a 1")
    private Long atencionId;

    @NotNull(message = "El id del asesor es requerido.")
    @Min(value = 1, message = "El id del asesor no debe ser menor a 1")
    private Long asesorId;

    @NotNull(message = "El id de orden de atención es requerido.")
    @Min(value = 1, message = "El id de orden de atención no debe ser menor a 1")
    private Long ordenAtencionId;

    private String fecha;

    private LocalTime horaInicio;

    private LocalTime horaFin;

    @NotBlank(message = "El código de ventanilla es requerido.")
    @Size(min = 6, max = 6, message = "El código de ventanilla debe tener exactamente 6 caracteres")
    private String codVentanilla;

    private String observacion;

    @NotNull(message = "El estado es requerido.")
    private Integer estado;

    public static final Function<AtencionRequest, AtencionEntity> toEntity = request -> AtencionEntity.builder()
            .fecha(LocalDate.now())
            .horaInicio(LocalTime.now())
            .codVentanilla(request.getCodVentanilla())
            .estado(request.getEstado())
            .actualizadoPor(SecurityUtil.getCurrentUserId())
            .fechaActualizacion(LocalDateTime.now())
            .creadoPor(SecurityUtil.getCurrentUserId())
            .fechaCreacion(LocalDateTime.now())
            .eliminado(false)
            .build();

    public static void toUpdate(AtencionRequest request, AtencionEntity entity) {
        entity.setHoraFin(request.getHoraFin());
        entity.setObservacion(request.getObservacion());
        entity.setEstado(request.getEstado());
        entity.setActualizadoPor(SecurityUtil.getCurrentUserId());
        entity.setFechaActualizacion(LocalDateTime.now());
    }
}
