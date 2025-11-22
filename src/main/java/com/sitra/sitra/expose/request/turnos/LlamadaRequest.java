package com.sitra.sitra.expose.request.turnos;

import com.sitra.sitra.entity.turnos.LlamadaEntity;
import com.sitra.sitra.entity.turnos.OrdenAtencionEntity;
import com.sitra.sitra.entity.seguridad.UsuarioEntity;
import com.sitra.sitra.expose.util.SecurityUtil;
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
public class LlamadaRequest {

    @NotNull(message = "El id de OrdenAtencion es requerido.")
    @Min(value = 1, message = "El id de OrdenAtencion no debe de ser menor a 1")
    private Long ordenAtencionId;

    @NotNull(message = "El id del asesor es requerido.")
    @Min(value = 1, message = "El id del asesor no debe de ser menor a 1")
    private Long asesorId;

    @NotBlank(message = "El código de ventanilla es requerido.")
    @Size(min = 6, max = 6, message = "El código de ventanilla debe tener exactamente 6 caracteres.")
    private String codVentanilla;

    @NotNull(message = "El número de llamada es requerido.")
    private Integer numLlamada;

    @Size(min = 6, max = 6, message = "El código de resultado debe tener exactamente 6 caracteres.")
    private String codResultado;

    @NotNull(message = "El estado es requerido.")
    private Integer estado;

    public static final Function<LlamadaRequest, LlamadaEntity> toEntity = request -> LlamadaEntity.builder()
            .codVentanilla(request.getCodVentanilla())
            .fechaLlamada(LocalDate.now())
            .horaLlamada(LocalTime.now())
            .numLlamada(request.getNumLlamada())
            .codResultado(request.getCodResultado())
            .estado(request.getEstado())
            .actualizadoPor(SecurityUtil.getCurrentUserId())
            .fechaActualizacion(LocalDateTime.now())
            .creadoPor(SecurityUtil.getCurrentUserId())
            .fechaCreacion(LocalDateTime.now())
            .eliminado(false)
            .build();

    public static void toUpdate(LlamadaRequest request, LlamadaEntity entity) {
        entity.setNumLlamada(request.getNumLlamada());
        entity.setCodResultado(request.getCodResultado());
        entity.setEstado(request.getEstado());
        entity.setActualizadoPor(SecurityUtil.getCurrentUserId());
        entity.setFechaActualizacion(LocalDateTime.now());
    }

}
