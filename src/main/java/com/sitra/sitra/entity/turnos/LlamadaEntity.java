package com.sitra.sitra.entity.turnos;

import com.sitra.sitra.entity.seguridad.UsuarioEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "llamada", schema = "turnos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LlamadaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "llamadaid")
    private Long llamadaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordenatencionid", nullable = false)
    private OrdenAtencionEntity ordenAtencion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asesorid", nullable = false)
    private UsuarioEntity asesor;

    @Column(name = "codventanilla", length = 6, nullable = false)
    private String codVentanilla;

    @Column(name = "fechallamada", nullable = false)
    private LocalDate fechaLlamada;

    @Column(name = "horallamada", nullable = false)
    private LocalTime horaLlamada;

    @Column(name = "numllamada", nullable = false)
    private Integer numLlamada;

    @Column(name = "codresultado", length = 6)
    private String codResultado;

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
