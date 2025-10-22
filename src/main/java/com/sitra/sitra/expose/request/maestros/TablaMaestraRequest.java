package com.sitra.sitra.expose.request.maestros;

import com.sitra.sitra.entity.maestros.TablaMaestraEntity;
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
public class TablaMaestraRequest {

    @NotNull(message = "El id es requerido")
    @Min(value = 1, message = "El id debe ser mayor a cero")
    private Long idTablaMaestra;

    @NotBlank(message = "El codigo de la tabla es requerido")
    @Size(min = 3, max = 3, message = "El codigo de la tabla debe tener exactamente 3 caracteres")
    private String codigoTabla;

    @NotBlank(message = "El codigo de la tabla es requerido")
    @Size(min = 3, max = 3, message = "El codigo de la tabla debe tener exactamente 3 caracteres")
    private String codigoItem;

    private Integer orden;

    @NotBlank(message = "La abreviatura es requerido")
    @Size(max = 6, message = "La abreviatura debe de tener menos de 6 caracteres.")
    private String abreviatura;

    @NotBlank(message = "La denominacion es requerido")
    @Size(max = 100, message = "La denominacion debe de tener menos de 100 caracteres.")
    private String denominacion;

    private Integer esSistema;

    @Range(min = 0, max = 1, message = "El estado debe de estar en el rango [ 0 - 1]")
    private int estado;

    public static final Function<TablaMaestraRequest, TablaMaestraEntity> toEntity = request -> TablaMaestraEntity.builder()
            .codigoTabla(request.getCodigoTabla())
            .codigoItem(request.getCodigoItem())
            .codigo(request.getCodigoTabla() + request.getCodigoItem())
            .orden(request.getOrden())
            .abreviatura(request.getAbreviatura())
            .denominacion(request.getDenominacion())
            .esSistema(request.getEsSistema())
            .estado(1)
            .actualizadoPor(SecurityUtil.getCurrentUserId())
            .fechaActualizacion(LocalDateTime.now())
            .creadoPor(SecurityUtil.getCurrentUserId())
            .fechaCreacion(LocalDateTime.now())
            .eliminado(false)
            .build();

    public static void toUpdate(TablaMaestraRequest request, TablaMaestraEntity entity) {
        entity.setOrden(request.getOrden());
        entity.setAbreviatura(request.getAbreviatura());
        entity.setDenominacion(request.getDenominacion());
        entity.setEsSistema(request.getEsSistema());
        entity.setEstado(request.getEstado() == 0 ? 0 : 1);
        entity.setActualizadoPor(SecurityUtil.getCurrentUserId());
        entity.setFechaActualizacion(LocalDateTime.now());
    }

}
