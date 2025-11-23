package com.sitra.sitra.expose.response.turnos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sitra.sitra.expose.response.seguridad.PersonaResponse;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PantallaResponse {
    private Long orderAtencionId;
    private Long llamadaId;
    private Long atencionId;
    private PersonaResponse paciente;
    private String fecha;
    private String codPriority;
    private Integer turno;
    private String codEstadoAtencion;
    private String codVentanilla;
    private Integer numLlamada;
    private String codResultado;
}
