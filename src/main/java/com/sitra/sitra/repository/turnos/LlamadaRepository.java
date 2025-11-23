package com.sitra.sitra.repository.turnos;

import com.sitra.sitra.entity.turnos.LlamadaEntity;
import com.sitra.sitra.service.maestros.impl.TablaMaestraServiceImpl;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface LlamadaRepository extends JpaRepository<LlamadaEntity, Long> {
    Optional<LlamadaEntity> findByOrdenAtencion_OrdenAtencionIdAndEstadoAndEliminado(Long orderAtentionId, int status, boolean deleted);
    default Optional<LlamadaEntity> getByOrderAtention(Long orderAtentionId) {
        return findByOrdenAtencion_OrdenAtencionIdAndEstadoAndEliminado(orderAtentionId, 1, false);
    }

    @EntityGraph(attributePaths = {"ordenAtencion.persona"})
    Optional<LlamadaEntity> findByOrdenAtencion_OrdenAtencionIdAndEstadoAndEliminadoEquals(Long orderAtentionId, int status, boolean deleted);
    default Optional<LlamadaEntity> getWithOrderByOrderAtention(Long orderAtentionId) {
        return findByOrdenAtencion_OrdenAtencionIdAndEstadoAndEliminadoEquals(orderAtentionId, 1, false);
    }

    @EntityGraph(attributePaths = {"ordenAtencion.persona"})
    Optional<LlamadaEntity> findByLlamadaIdAndEstadoAndEliminado(Long llamadaId, int status, boolean deleted);
    default Optional<LlamadaEntity> getWithOrderByID(Long id) {
        return findByLlamadaIdAndEstadoAndEliminado(id, 1, false);
    }

    @EntityGraph(attributePaths = {"ordenAtencion.persona"})
    Optional<LlamadaEntity> findByFechaLlamadaAndCodResultadoAndCodVentanillaAndEstadoAndEliminado(LocalDate date, String codeResult, String codeVentanilla, int status, boolean deleted);
    default Optional<LlamadaEntity> getLlamadaInPending(LocalDate date, String codeVentanilla) {
        return findByFechaLlamadaAndCodResultadoAndCodVentanillaAndEstadoAndEliminado(date, TablaMaestraServiceImpl.PENDIENTE_LLAMADA, codeVentanilla, 1, false);
    }
    default Optional<LlamadaEntity> getLlamadaInAtention(LocalDate date, String codeVentanilla) {
        return findByFechaLlamadaAndCodResultadoAndCodVentanillaAndEstadoAndEliminado(date, TablaMaestraServiceImpl.SE_PRESENTO, codeVentanilla, 1, false);
    }
}
