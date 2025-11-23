package com.sitra.sitra.repository.turnos;

import com.sitra.sitra.entity.turnos.LlamadaEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
}
