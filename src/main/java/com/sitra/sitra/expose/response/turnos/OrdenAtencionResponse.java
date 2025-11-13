package com.sitra.sitra.expose.response.turnos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sitra.sitra.entity.turnos.OrdenAtencionEntity;
import com.sitra.sitra.expose.response.seguridad.PersonaResponse;
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
public class OrdenAtencionResponse {
    private Long ordenAtencionId;
    private PersonaResponse persona;
    private UsuarioResponse receptor;
    private UsuarioResponse asesor;
    private String fecha;
    private LocalTime hora;
    private String codPrioridad;
    private Integer turno;
    private String codEstadoAtencion;
    private Integer numLlamadas;
    private String codVentanilla;
    private int estado;

    public static final Function<OrdenAtencionEntity, OrdenAtencionResponse> toResponse = entity -> OrdenAtencionResponse.builder()
            .ordenAtencionId(entity.getOrdenAtencionId())
            .persona(PersonaResponse.builder()
                    .personaId(entity.getPersona().getPersonaId())
                    .build())
            .receptor(UsuarioResponse.builder()
                    .usuarioId(entity.getReceptor().getUsuarioId())
                    .build())
            .asesor(UsuarioResponse.builder()
                    .usuarioId(entity.getAsesor() != null ? entity.getAsesor().getUsuarioId(): null)
                    .build())
            .fecha(DateConvertUtil.formatLocalDateToDDMMYYYY(entity.getFecha()))
            .hora(entity.getHora())
            .codPrioridad(entity.getCodPrioridad())
            .turno(entity.getTurno())
            .codEstadoAtencion(entity.getCodEstadoAtencion())
            .numLlamadas(entity.getNumLlamadas())
            .codVentanilla(entity.getCodVentanilla())
            .estado(entity.getEstado())
            .build();

    public static final Function<OrdenAtencionEntity, OrdenAtencionResponse> toResponseDetailPerson = entity -> OrdenAtencionResponse.builder()
            .ordenAtencionId(entity.getOrdenAtencionId())
            .persona(PersonaResponse.toResponse.apply(entity.getPersona()))
            .receptor(UsuarioResponse.builder()
                    .usuarioId(entity.getReceptor().getUsuarioId())
                    .build())
            .asesor(entity.getAsesor() != null ? UsuarioResponse.builder()
                    .usuarioId(entity.getAsesor().getUsuarioId())
                    .build() : null)
            .fecha(DateConvertUtil.formatLocalDateToDDMMYYYY(entity.getFecha()))
            .hora(entity.getHora())
            .codPrioridad(entity.getCodPrioridad())
            .turno(entity.getTurno())
            .codEstadoAtencion(entity.getCodEstadoAtencion())
            .numLlamadas(entity.getNumLlamadas())
            .codVentanilla(entity.getCodVentanilla())
            .estado(entity.getEstado())
            .build();

    public static final Function<OrdenAtencionEntity, OrdenAtencionResponse> toResponseDetail = entity -> OrdenAtencionResponse.builder()
            .ordenAtencionId(entity.getOrdenAtencionId())
            .persona(PersonaResponse.toResponse.apply(entity.getPersona()))
            .receptor(UsuarioResponse.toResponseDetail.apply(entity.getReceptor()))
            .asesor(entity.getAsesor() != null ? UsuarioResponse.toResponseDetail.apply(entity.getAsesor()) : null)
            .fecha(DateConvertUtil.formatLocalDateToDDMMYYYY(entity.getFecha()))
            .hora(entity.getHora())
            .codPrioridad(entity.getCodPrioridad())
            .turno(entity.getTurno())
            .codEstadoAtencion(entity.getCodEstadoAtencion())
            .numLlamadas(entity.getNumLlamadas())
            .estado(entity.getEstado())
            .build();

}
