package com.sitra.sitra.persistence.projections;

import java.time.LocalDate;
import java.time.LocalTime;

public interface OrdenAtencionDetailProjection {
    Long getOrdenatencionid();
    Long getReceptorid();
    LocalDate getFecha();
    LocalTime getHora();
    Integer getTurno();
    String getCodestadoatencion();
    String getCodventanillaOa();

    Long getPersonaid();
    String getNombres();
    String getApellidopaterno();
    String getApellidomaterno();
    String getNumerodocumentoidentidad();

    Long getLlamadaid();
    Long getAsesoridLl();
    String getCodventanillaLl();
    LocalDate getFechallamada();
    LocalTime getHorallamada();
    Integer getNumllamada();
    String getCodresultado();

    Long getAtencionid();
    LocalDate getFechaatencion();
    LocalTime getHorainicio();
    LocalTime getHorafin();
    String getCodventanillaAte();
    Long getAsesoridAte();
}

