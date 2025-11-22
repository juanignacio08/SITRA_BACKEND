package com.sitra.sitra.entity.turnos;

import com.sitra.sitra.entity.seguridad.UsuarioEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "atencion", schema = "turnos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AtencionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "atencionid")
    private Long atencionId;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "horainicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "horafin")
    private LocalTime horaFin;

    @Column(name = "codventanilla", length = 6, nullable = false)
    private String codVentanilla;

    @Column(name = "observacion")
    private String observacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asesorid", nullable = false)
    private UsuarioEntity asesor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordenatencionid", nullable = false)
    private OrdenAtencionEntity ordenAtencion;

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
