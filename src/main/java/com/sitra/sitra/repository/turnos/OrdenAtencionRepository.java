package com.sitra.sitra.repository.turnos;

import com.sitra.sitra.entity.turnos.OrdenAtencionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrdenAtencionRepository extends JpaRepository<OrdenAtencionEntity, Long> {
    List<OrdenAtencionEntity> findByCodEstadoAtencionAndCodPrioridadAndFechaAndEstadoAndEliminadoOrderByTurnoAsc(String codeStatus, String codePreferential, LocalDate date, int estado, boolean eliminado);
    default List<OrdenAtencionEntity> getByCodeStatusAndCodePrio(String codeStatus, String codePreferential, LocalDate date) {
        return findByCodEstadoAtencionAndCodPrioridadAndFechaAndEstadoAndEliminadoOrderByTurnoAsc(codeStatus, codePreferential, date, 1, false);
    }

    @EntityGraph(attributePaths = {"persona"})
    Page<OrdenAtencionEntity> findByCodPrioridadAndCodEstadoAtencionAndFechaAndEstadoAndEliminadoOrderByTurnoAsc(String codePreferential, String codeStatus, LocalDate date, int estado, boolean eliminado, Pageable pageable);
    default Page<OrdenAtencionEntity> getOrdersByCodePreferentialAndCodeStatus(String codePreferential, String codeStatus, LocalDate date, Pageable pageable) {
        return findByCodPrioridadAndCodEstadoAtencionAndFechaAndEstadoAndEliminadoOrderByTurnoAsc(codePreferential, codeStatus, date, 1,false, pageable);
    }

    Optional<OrdenAtencionEntity> findByOrdenAtencionIdAndEstadoAndEliminado(Long id, int estado, boolean eliminado);
    default Optional<OrdenAtencionEntity> getActiveByID(Long id) {
        return findByOrdenAtencionIdAndEstadoAndEliminado(id, 1, false);
    }

    @EntityGraph(attributePaths = {"persona", "usuario.persona", "usuario.rol"})
    Optional<OrdenAtencionEntity> findByOrdenAtencionIdAndEstadoAndEliminadoEquals(Long id, int estado, boolean eliminado);
    default Optional<OrdenAtencionEntity> getDetailActiveByID(Long id) {
        return findByOrdenAtencionIdAndEstadoAndEliminadoEquals(id, 1, false);
    }

    @EntityGraph(attributePaths = {"persona"})
    List<OrdenAtencionEntity> findByCodEstadoAtencionAndFechaAndEstadoAndEliminadoOrderByTurnoAsc(String codeStatus, LocalDate date, int status, boolean deleted);
    default List<OrdenAtencionEntity> getByCodeStatusAndDate(String codeStatus, LocalDate date) {
        return findByCodEstadoAtencionAndFechaAndEstadoAndEliminadoOrderByTurnoAsc(codeStatus, date, 1, false);
    }

}
