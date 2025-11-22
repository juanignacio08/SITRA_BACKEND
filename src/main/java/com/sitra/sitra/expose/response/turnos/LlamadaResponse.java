package com.sitra.sitra.expose.response.turnos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sitra.sitra.entity.turnos.LlamadaEntity;
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
public class LlamadaResponse {

    private Long llamadaId;
    private OrdenAtencionResponse ordenAtencion;
    private UsuarioResponse asesor;
    private String codVentanilla;
    private String fechaLlamada;
    private LocalTime horaLLamada;
    private Integer numLlamada;
    private String codResultado;
    private int estado;

    public static final Function<LlamadaEntity, LlamadaResponse> toResponse = entity -> LlamadaResponse.builder()
            .llamadaId(entity.getLlamadaId())
            .ordenAtencion(OrdenAtencionResponse.builder()
                    .ordenAtencionId(entity.getOrdenAtencion().getOrdenAtencionId())
                    .build())
            .asesor(UsuarioResponse.builder()
                    .usuarioId(entity.getAsesor().getUsuarioId())
                    .build())
            .fechaLlamada(DateConvertUtil.formatLocalDateToDDMMYYYY(entity.getFechaLlamada()))
            .horaLLamada(entity.getHoraLlamada())
            .numLlamada(entity.getNumLlamada())
            .codResultado(entity.getCodResultado())
            .estado(entity.getEstado())
            .build();

}
