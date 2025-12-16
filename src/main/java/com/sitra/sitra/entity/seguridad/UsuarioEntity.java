package com.sitra.sitra.entity.seguridad;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuario", schema = "seguridad")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuarioid")
    private Long usuarioId;

    @Column(name = "usuario", nullable = false, length = 100)
    private String usuario;

    @Column(name = "codventanilla", length = 6)
    private String codVentanilla;

    @Column(name = "contrasena", nullable = false, length = 255)
    private String contrasena;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rolid", nullable = false) // Mapea al campo rolId de la tabla
    private RolEntity rol;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "personaid", nullable = false) // Mapea al campo personaId de la tabla
    private PersonaEntity persona;

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
