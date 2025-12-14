package com.sitra.sitra.entity.reportes;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "noticias", schema = "reportes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticiasEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "noticiasid")
    private Long noticiasId;

    @Column(name = "contenido", columnDefinition = "TEXT", nullable = false)
    private String contenido;

    @Column(name = "estado", nullable = false)
    private Integer estado;

    @Column(name = "actualizadopor", nullable = false)
    private Long actualizadoPor;

    @Column(name = "fechaactualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    @Column(name = "creadopor", nullable = false)
    private Long creadoPor;

    @Column(name = "fechacreacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "eliminado", nullable = false)
    private Boolean eliminado = false;
}