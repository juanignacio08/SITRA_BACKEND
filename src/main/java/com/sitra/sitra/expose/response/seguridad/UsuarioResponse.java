package com.sitra.sitra.expose.response.seguridad;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sitra.sitra.entity.seguridad.UsuarioEntity;
import lombok.*;

import java.util.function.Function;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsuarioResponse {
    private Long usuarioId;
    private String usuario;
    private String contrasena;
    private RolResponse rol;
    private PersonaResponse persona;
    private int estado;

    public static final Function<UsuarioEntity, UsuarioResponse> toResponse = entity -> UsuarioResponse.builder()
            .usuarioId(entity.getUsuarioId())
            .usuario(entity.getUsuario())
            .contrasena(entity.getContrasena())
            .rol(new RolResponse(
                    entity.getRol().getRolId(),
                    null,
                    1
            ))
            .persona(new PersonaResponse(
                    entity.getPersona().getPersonaId(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    1
            ))
            .estado(entity.getEstado())
            .build();

    public static final Function<UsuarioEntity, UsuarioResponse> toResponseDetail = entity -> UsuarioResponse.builder()
            .usuarioId(entity.getUsuarioId())
            .usuario(entity.getUsuario())
            .contrasena(entity.getContrasena())
            .rol(RolResponse.toResponse.apply(entity.getRol()))
            .persona(PersonaResponse.toResponse.apply(entity.getPersona()))
            .estado(entity.getEstado())
            .build();
}
