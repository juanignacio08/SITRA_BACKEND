package com.sitra.sitra.expose.response.maestros;

//import com.fasterxml.jackson.annotation.JsonInclude;
import com.sitra.sitra.entity.maestros.TablaMaestraEntity;
import lombok.*;

import java.util.function.Function;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class TablaMaestraResponse {

    private Long idTablaMaestra;
    private String codigoTabla;
    private String codigoItem;
    private String codigo;
    private Integer orden;
    private String abreviatura;
    private String denominacion;
    private Integer esSistema;
    private int estado;

    public static final Function<TablaMaestraEntity, TablaMaestraResponse> toResponse = entity -> TablaMaestraResponse.builder()
            .idTablaMaestra(entity.getIdTablaMaestra())
            .codigoTabla(entity.getCodigoTabla())
            .codigoItem(entity.getCodigoItem())
            .codigo(entity.getCodigo())
            .orden(entity.getOrden())
            .abreviatura(entity.getAbreviatura())
            .denominacion(entity.getDenominacion())
            .esSistema(entity.getEsSistema())
            .estado(entity.getEstado())
            .build();
}
