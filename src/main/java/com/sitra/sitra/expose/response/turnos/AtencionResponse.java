package com.sitra.sitra.expose.response.turnos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sitra.sitra.entity.turnos.AtencionEntity;
import com.sitra.sitra.expose.response.seguridad.UsuarioResponse;
import com.sitra.sitra.expose.util.DateConvertUtil;
import lombok.*;

import java.time.LocalTime;
import java.util.function.Function;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AtencionResponse {

    private Long atencionId;
    private UsuarioResponse asesor;
    private OrdenAtencionResponse ordenAtencion;
    private String fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String codVentanilla;
    private String observacion;
    private String codEstadoAtencion;
    private Integer estado;

    // -----------------------------------------
    // BASIC RESPONSE (IDs mínimos)
    // -----------------------------------------
    public static final Function<AtencionEntity, AtencionResponse> toResponse = entity -> AtencionResponse.builder()
            .atencionId(entity.getAtencionId())
            .asesor(entity.getAsesor() != null ? UsuarioResponse.builder()
                    .usuarioId(entity.getAsesor().getUsuarioId())
                    .build() : null)
            .ordenAtencion(OrdenAtencionResponse.builder()
                    .ordenAtencionId(entity.getOrdenAtencion().getOrdenAtencionId())
                    .build())
            .fecha(DateConvertUtil.formatLocalDateToDDMMYYYY(entity.getFecha()))
            .horaInicio(entity.getHoraInicio())
            .horaFin(entity.getHoraFin())
            .codVentanilla(entity.getCodVentanilla())
            .observacion(entity.getObservacion())
            .codEstadoAtencion(entity.getCodEstadoAtencion())
            .estado(entity.getEstado())
            .build();

    // -----------------------------------------
    // RESPONSE CON DETALLE DEL ORDEN
    // (pero sin detalle completo del asesor)
    // -----------------------------------------
    public static final Function<AtencionEntity, AtencionResponse> toResponseDetailOrder = entity -> AtencionResponse.builder()
            .atencionId(entity.getAtencionId())
            .asesor(entity.getAsesor() != null ? UsuarioResponse.builder()
                    .usuarioId(entity.getAsesor().getUsuarioId())
                    .build() : null)
            .ordenAtencion(OrdenAtencionResponse.toResponse.apply(entity.getOrdenAtencion()))
            .fecha(DateConvertUtil.formatLocalDateToDDMMYYYY(entity.getFecha()))
            .horaInicio(entity.getHoraInicio())
            .horaFin(entity.getHoraFin())
            .codVentanilla(entity.getCodVentanilla())
            .observacion(entity.getObservacion())
            .codEstadoAtencion(entity.getCodEstadoAtencion())
            .estado(entity.getEstado())
            .build();

    // -----------------------------------------
    // RESPONSE COMPLETO (asesor + orden con detalle)
    // -----------------------------------------
    public static final Function<AtencionEntity, AtencionResponse> toResponseDetail = entity -> AtencionResponse.builder()
            .atencionId(entity.getAtencionId())
            .asesor(entity.getAsesor() != null ?
                    UsuarioResponse.toResponseDetail.apply(entity.getAsesor()) : null)
            .ordenAtencion(OrdenAtencionResponse.toResponseDetail.apply(entity.getOrdenAtencion()))
            .fecha(DateConvertUtil.formatLocalDateToDDMMYYYY(entity.getFecha()))
            .horaInicio(entity.getHoraInicio())
            .horaFin(entity.getHoraFin())
            .observacion(entity.getObservacion())
            .codVentanilla(entity.getCodVentanilla())
            .codEstadoAtencion(entity.getCodEstadoAtencion())
            .estado(entity.getEstado())
            .build();

}
