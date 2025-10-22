package com.sitra.sitra.expose.response.seguridad;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sitra.sitra.entity.seguridad.PersonaEntity;
import lombok.*;

import java.util.function.Function;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonaResponse {
    private Long personaId;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String nombre;
    private String tipoDocumentoIdentidad;
    private String numeroDocumentoIdentidad;
    private int estado;

    public static final Function<PersonaEntity, PersonaResponse> toResponse = entity -> PersonaResponse.builder()
            .personaId(entity.getPersonaId())
            .apellidoPaterno(entity.getApellidoPaterno())
            .apellidoMaterno(entity.getApellidoMaterno())
            .nombre(entity.getNombre())
            .tipoDocumentoIdentidad(entity.getTipoDocumentoIdentidad())
            .numeroDocumentoIdentidad(entity.getNumeroDocumentoIdentidad())
            .estado(entity.getEstado())
            .build();
}
