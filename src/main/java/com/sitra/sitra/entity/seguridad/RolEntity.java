package com.sitra.sitra.entity.seguridad;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "rol", schema = "seguridad")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rolid")
    private Long rolId;

    @Column(name = "denominacion", nullable = false, length = 100)
    private String denominacion;

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
    private Boolean eliminado = Boolean.FALSE;

}