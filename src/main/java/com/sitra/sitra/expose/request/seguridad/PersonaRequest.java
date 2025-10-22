package com.sitra.sitra.expose.request.seguridad;

import com.sitra.sitra.entity.seguridad.PersonaEntity;
import com.sitra.sitra.expose.util.SecurityUtil;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDateTime;
import java.util.function.Function;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PersonaRequest {

    @NotNull(message = "El id es requerido")
    @Min(value = 1, message = "El id debe ser mayor a cero")
    private Long personaId;

    @NotBlank(message = "El apellido paterno es requerido")
    @Size(max = 100, message = "El apellido paterno debe tener menos de 100 caracteres")
    private String apellidoPaterno;

    @NotBlank(message = "El apellido Materno es requerido")
    @Size(max = 100, message = "El apellido Materno debe tener menos de 100 caracteres")
    private String apellidoMaterno;

    @NotBlank(message = "El nombre es requerido")
    @Size(max = 100, message = "El nombre debe tener menos de 100 caracteres")
    private String nombre;

    @NotBlank(message = "El tipo de Documento de Identidad es requerido")
    @Size(min = 6, max = 6, message = "El tipo de Documento de Identidad debe tener exactamente 6 caracteres")
    private String tipoDocumentoIdentidad;

    @NotBlank(message = "El numero de documento es requerido")
    @Size(max = 20, message = "El numero de documento debe tener menos de 20 caracteres")
    private String numeroDocumentoIdentidad;

    @Range(min = 0, max = 1, message = "El estado debe de estar en el rango [ 0 - 1]")
    private int estado;

    public static final Function<PersonaRequest, PersonaEntity> toEntity = request -> PersonaEntity.builder()
            .apellidoPaterno(request.getApellidoPaterno())
            .apellidoMaterno(request.getApellidoMaterno())
            .nombre(request.getNombre())
            .tipoDocumentoIdentidad(request.getTipoDocumentoIdentidad())
            .numeroDocumentoIdentidad(request.getNumeroDocumentoIdentidad())
            .estado(1)
            .actualizadoPor(SecurityUtil.getCurrentUserId())
            .fechaActualizacion(LocalDateTime.now())
            .creadoPor(SecurityUtil.getCurrentUserId())
            .fechaCreacion(LocalDateTime.now())
            .eliminado(false)
            .build();

    public static void toUpdate(PersonaRequest request, PersonaEntity entity) {
        entity.setApellidoPaterno(request.getApellidoPaterno());
        entity.setApellidoMaterno(request.getApellidoMaterno());
        entity.setNombre(request.getNombre());
        entity.setTipoDocumentoIdentidad(request.getTipoDocumentoIdentidad());
        entity.setNumeroDocumentoIdentidad(request.getNumeroDocumentoIdentidad());
        entity.setEstado(request.getEstado());
        entity.setActualizadoPor(SecurityUtil.getCurrentUserId());
        entity.setFechaActualizacion(LocalDateTime.now());
    }

}
