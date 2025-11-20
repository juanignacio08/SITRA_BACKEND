package com.sitra.sitra.expose.request.turnos;

import com.sitra.sitra.entity.turnos.AtencionEntity;
import com.sitra.sitra.exceptions.BadRequestException;
import com.sitra.sitra.expose.util.SecurityUtil;
import com.sitra.sitra.service.maestros.impl.TablaMaestraServiceImpl;
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

    @NotNull(message = "La fecha es requerida.")
    private LocalDate fecha;

    @NotNull(message = "La hora de inicio es requerida.")
    private LocalTime horaInicio;

    private LocalTime horaFin;

    @NotBlank(message = "El código de ventanilla es requerido.")
    @Size(min = 6, max = 6, message = "El código de ventanilla debe tener exactamente 6 caracteres")
    private String codVentanilla;

    private String observacion;

    @NotBlank(message = "El código de estado de atención es requerido.")
    @Size(min = 6, max = 6, message = "El código de estado de atención debe tener exactamente 6 caracteres")
    private String codEstadoAtencion;

    @NotNull(message = "El estado es requerido.")
    private Integer estado;

    public static final Function<AtencionRequest, AtencionEntity> toEntity = request -> AtencionEntity.builder()
            .fecha(request.getFecha())
            .horaInicio(request.getHoraInicio())
            .horaFin(request.getHoraFin())
            .codVentanilla(request.getCodVentanilla())
            .observacion(request.getObservacion())
            .codEstadoAtencion(request.getCodEstadoAtencion())
            .estado(request.getEstado())
            .actualizadoPor(SecurityUtil.getCurrentUserId())
            .fechaActualizacion(LocalDateTime.now())
            .creadoPor(SecurityUtil.getCurrentUserId())
            .fechaCreacion(LocalDateTime.now())
            .eliminado(false)
            .build();

    public static void toUpdate(AtencionRequest request, AtencionEntity entity) {

        entity.setCodEstadoAtencion(request.getCodEstadoAtencion());

        if (!entity.getCodEstadoAtencion().equals(TablaMaestraServiceImpl.PENDIENTE)) {
            if (request.getCodVentanilla() == null || request.getCodVentanilla().length() != 6)
                throw new BadRequestException("Se requiere el código de la ventanilla");

            if (!TablaMaestraServiceImpl.tableCodeVentanilla.containsValue(request.getCodVentanilla()))
                throw new BadRequestException("Registro no encontrado. [ VENTANILLA ]");

            entity.setCodVentanilla(request.getCodVentanilla());
        }

        entity.setFecha(request.getFecha());
        entity.setHoraInicio(request.getHoraInicio());
        entity.setHoraFin(request.getHoraFin());
        entity.setObservacion(request.getObservacion());
        entity.setEstado(request.getEstado());

        entity.setActualizadoPor(SecurityUtil.getCurrentUserId());
        entity.setFechaActualizacion(LocalDateTime.now());
    }
}
