package com.sitra.sitra.persistence.repository.turnos;

import com.sitra.sitra.entity.turnos.AtencionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AtencionRepository extends JpaRepository<AtencionEntity, Long> {
    Optional<AtencionEntity> findByAtencionIdAndEstadoAndEliminado(Long atencionId, int status, boolean deleted);
    default Optional<AtencionEntity> getByID(Long atencionId) {
        return findByAtencionIdAndEstadoAndEliminado(atencionId, 1, false);
    }

    Optional<AtencionEntity> findByOrdenAtencion_OrdenAtencionIdAndEstadoAndEliminado(Long orderAtentionId, int status, boolean deleted);
    default Optional<AtencionEntity> getByOrderAtention(Long orderAtentionId) {
        return findByOrdenAtencion_OrdenAtencionIdAndEstadoAndEliminado(orderAtentionId, 1, false);
    }
}
