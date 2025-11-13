package com.sitra.sitra.expose.request.turnos;

import com.sitra.sitra.entity.turnos.OrdenAtencionEntity;
import com.sitra.sitra.exceptions.BadRequestException;
import com.sitra.sitra.exceptions.NotFoundException;
import com.sitra.sitra.expose.util.SecurityUtil;
import com.sitra.sitra.service.maestros.impl.TablaMaestraServiceImpl;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Range;

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

    @Min(value = 1, message = "El idAsesor no debe de ser menor a 1")
    private Long asesorId;

    @Size(min = 6, max = 6, message = "El codigo de prioridad debe tener exactamente 6 caracteres")
    private String codPrioridad;

    @NotBlank(message = "El codigo de estado de atencion es requerido.")
    @Size(min = 6, max = 6, message = "El codigo de prioridad debe tener exactamente 6 caracteres")
    private String codEstadoAtencion;

    @NotNull(message = "El numero de llamadas es requerido.")
    @Range(min = 0, max = 5, message = "El numero de llamadas debe de estar en un rango de 0-5")
    private Integer numLlamadas;

    @NotNull(message = "El estado es requerido.")
    private int estado;

    @Size(min = 6, max = 6, message = "El codigo de ventanilla debe tener exactamente 6 caracteres")
    private String codVentanilla;

    public static final Function<OrdenAtencionRequest, OrdenAtencionEntity> toEntity = request -> OrdenAtencionEntity.builder()
            .fecha(LocalDate.now())
            .hora(LocalTime.now())
            .codPrioridad(request.getCodPrioridad())
            .codEstadoAtencion(TablaMaestraServiceImpl.PENDIENTE)
            .numLlamadas(0)
            .estado(1)
            .actualizadoPor(SecurityUtil.getCurrentUserId())
            .fechaActualizacion(LocalDateTime.now())
            .creadoPor(SecurityUtil.getCurrentUserId())
            .fechaCreacion(LocalDateTime.now())
            .eliminado(false)
            .build();

    public static void toUpdate(OrdenAtencionRequest request, OrdenAtencionEntity entity) {
        entity.setCodEstadoAtencion(request.getCodEstadoAtencion());
        if (!entity.getCodEstadoAtencion().equals(TablaMaestraServiceImpl.PENDIENTE)) {
            if (request.getCodVentanilla() == null || request.getCodVentanilla().length() != 6) throw new BadRequestException("Se requiere el codigo de la ventanilla");

            if (!TablaMaestraServiceImpl.tableCodeVentanilla.containsValue(request.getCodVentanilla())) throw new NotFoundException("Registro no encontrado. [ VENTANILLA ]");

            entity.setCodVentanilla(request.getCodVentanilla());
        }

        entity.setNumLlamadas(request.getNumLlamadas());
        entity.setEstado(request.getEstado());
        entity.setActualizadoPor(SecurityUtil.getCurrentUserId());
        entity.setFechaActualizacion(LocalDateTime.now());
    }

}
