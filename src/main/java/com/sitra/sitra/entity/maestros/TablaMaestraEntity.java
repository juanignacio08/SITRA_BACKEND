package com.sitra.sitra.entity.maestros;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tablamaestra", schema = "maestros")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TablaMaestraEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idtablamaestra")
    private Long idTablaMaestra;

    @Column(name = "codigotabla", nullable = false, length = 3)
    private String codigoTabla;

    @Column(name = "codigoitem", nullable = false, length = 3)
    private String codigoItem;

    @Column(name = "codigo", nullable = false, length = 6, unique = true)
    private String codigo;

    @Column(name = "orden")
    private Integer orden;

    @Column(name = "abreviatura", length = 6)
    private String abreviatura;

    @Column(name = "denominacion", length = 100, nullable = false)
    private String denominacion;

    @Column(name = "essistema", nullable = false)
    private Integer esSistema;

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
