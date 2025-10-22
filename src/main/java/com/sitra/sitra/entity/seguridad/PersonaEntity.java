package com.sitra.sitra.entity.seguridad;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "persona", schema = "seguridad")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "personaid")
    private Long personaId;

    @Column(name = "apellidopaterno", nullable = false, length = 100)
    private String apellidoPaterno;

    @Column(name = "apellidomaterno", nullable = false, length = 100)
    private String apellidoMaterno;

    @Column(name = "nombres", nullable = false, length = 100)
    private String nombre;

    @Column(name = "tipodocumentoidentidad", nullable = false, length = 6)
    private String tipoDocumentoIdentidad;

    @Column(name = "numerodocumentoidentidad", nullable = false, length = 20)
    private String numeroDocumentoIdentidad;

    @Column(name = "estado", nullable = false)
    private int estado;

    @Column(name = "actualizadopor", nullable = false)
    private Long actualizadoPor;

    @Column(name = "fechaactualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    @Column(name = "creadopor", nullable = false)
    private Long creadoPor;

    @Column(name = "fechacreacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "eliminado", nullable = false)
    private Boolean eliminado;
}
