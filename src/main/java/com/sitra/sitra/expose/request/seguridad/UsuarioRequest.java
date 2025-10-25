package com.sitra.sitra.expose.request.seguridad;

import com.sitra.sitra.entity.seguridad.UsuarioEntity;
import com.sitra.sitra.expose.util.SecurityUtil;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.function.Function;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UsuarioRequest {

    @NotNull(message = "El id es requerido.")
    @Min(value = 1, message = "El id no debe de ser menor a 1")
    private Long usuarioId;

    @NotBlank(message = "El usuario es requerido")
    @Size(max = 100, message = "El usuario debe tener menos de 100 caracteres")
    private String usuario;

    @NotBlank(message = "La contraseña es requerido")
    private String contrasena;

    @NotNull(message = "El id del rol es requerido.")
    @Min(value = 1, message = "El id del rol no debe de ser menor a 1")
    private Long rolId;

    @NotNull(message = "El id de la persona es requerido.")
    @Min(value = 1, message = "El id de la persona no debe de ser menor a 1")
    private Long personaId;

    private int estado;

    public static final Function<UsuarioRequest, UsuarioEntity> toEntity = request -> UsuarioEntity.builder()
            .usuario(request.getUsuario())
            .contrasena(request.getContrasena())
            .estado(1)
            .actualizadoPor(SecurityUtil.getCurrentUserId())
            .fechaActualizacion(LocalDateTime.now())
            .creadoPor(SecurityUtil.getCurrentUserId())
            .fechaCreacion(LocalDateTime.now())
            .eliminado(false)
            .build();
}
