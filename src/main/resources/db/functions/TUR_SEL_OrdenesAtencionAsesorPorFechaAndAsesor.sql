-- *************************************************************************************
-- CREATED BY: CREIFER
-- DESCRIPTION: Selección de órdenes de atención, llamadas y atenciones asociadas por
--              fecha y asesor.
-- DATE: 02.12.2025
-- LAST UPDATE: 02.12.2025
-- *************************************************************************************
-- DROP FUNCTION IF EXISTS turnos.TUR_SEL_OrdenesAtencionAsesorPorFechaAndAsesor;

CREATE OR REPLACE FUNCTION turnos.TUR_SEL_OrdenesAtencionAsesorPorFechaAndAsesor(
    p_asesor_id BIGINT,
    p_fecha DATE
)
RETURNS TABLE (
	ordenatencionid        BIGINT,
    receptorid             BIGINT,
    fecha                  DATE,
    hora                   TIME,
    turno                  INTEGER,
    codestadoatencion      CHAR(6),
    codventanilla_oa       CHAR(6),

    personaid              BIGINT,
	nombres					VARCHAR(100),
    apellidopaterno        VARCHAR(100),
    apellidomaterno        VARCHAR(100),
    numerodocumentoidentidad VARCHAR(20),

    llamadaid              BIGINT,
    asesorid_ll            BIGINT,
    codventanilla_ll       CHAR(6),
    fechallamada           DATE,
    horallamada            TIME,
    numllamada             INTEGER,
    codresultado           CHAR(6),

    atencionid             BIGINT,
    fechaatencion          DATE,
    horainicio             TIME,
    horafin                TIME,
    codventanilla_ate      CHAR(6),
    asesorid_ate           BIGINT
)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT
        oa.ordenatencionid,
        oa.receptorid,
        oa.fecha,
        oa.hora,
        oa.turno,
        oa.codestadoatencion,
        oa.codventanilla,

        per.personaid,
		per.nombres,
        per.apellidopaterno,
        per.apellidomaterno,
        per.numerodocumentoidentidad,

        ll.llamadaid,
        ll.asesorid,
        ll.codventanilla,
        ll.fechallamada,
        ll.horallamada,
        ll.numllamada,
        ll.codresultado,

        ate.atencionid,
        ate.fecha AS fechaatencion,
        ate.horainicio,
        ate.horafin,
        ate.codventanilla,
        ate.asesorid
    FROM turnos.ordenatencion oa
    JOIN seguridad.persona per
        ON per.personaid = oa.personaid
        AND per.estado = 1
        AND per.eliminado = false

    JOIN turnos.llamada ll
        ON oa.ordenatencionid = ll.ordenatencionid
        AND ll.asesorid = p_asesor_id
        AND ll.estado = 1
        AND ll.eliminado = false

    LEFT JOIN turnos.atencion ate
        ON oa.ordenatencionid = ate.ordenatencionid
        AND ate.asesorid = p_asesor_id
        AND ate.estado = 1
        AND ate.eliminado = false

    WHERE
        oa.fecha = p_fecha
        AND oa.estado = 1
        AND oa.eliminado = false

    ORDER BY oa.turno, oa.fecha;
END;
$$;

-- SELECT * FROM turnos.TUR_SEL_OrdenesAtencionAsesorPorFechaAndAsesor(22, '2025-12-20');
