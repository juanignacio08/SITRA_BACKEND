package com.sitra.sitra.entity.turnos;

import com.sitra.sitra.entity.seguridad.PersonaEntity;
import com.sitra.sitra.entity.seguridad.UsuarioEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "ordenatencion", schema = "turnos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdenAtencionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ordenatencionid")
    private Long ordenAtencionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personaid", nullable = false)
    private PersonaEntity persona;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receptorid", nullable = false)
    private UsuarioEntity receptor;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "hora", nullable = false)
    private LocalTime hora;

    @Column(name = "codprioridad", length = 6)
    private String codPrioridad;

    @Column(name = "turno", nullable = false)
    private Integer turno;

    @Column(name = "codestadoatencion", nullable = false, length = 6)
    private String codEstadoAtencion;

    @Column(name = "codventanilla", length = 6)
    private String codVentanilla;

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
