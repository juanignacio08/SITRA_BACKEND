package com.sitra.sitra.persistence.repository.turnos;

import com.sitra.sitra.entity.turnos.OrdenAtencionEntity;
import com.sitra.sitra.persistence.projections.OrdenAtencionDetailProjection;
import com.sitra.sitra.service.maestros.impl.TablaMaestraServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    Optional<OrdenAtencionEntity> findFirstByFechaAndEstadoAndEliminadoOrderByTurnoDesc(LocalDate date, int status, boolean deleted);
    default Optional<OrdenAtencionEntity> getFirstByFecha(LocalDate date) {
        return findFirstByFechaAndEstadoAndEliminadoOrderByTurnoDesc(date, 1, false);
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

    @EntityGraph(attributePaths = {"persona", "receptor.persona", "receptor.rol"})
    Optional<OrdenAtencionEntity> findByOrdenAtencionIdAndEstadoAndEliminadoEquals(Long id, int estado, boolean eliminado);
    default Optional<OrdenAtencionEntity> getDetailActiveByID(Long id) {
        return findByOrdenAtencionIdAndEstadoAndEliminadoEquals(id, 1, false);
    }

    @EntityGraph(attributePaths = {"persona"})
    List<OrdenAtencionEntity> findByCodEstadoAtencionAndFechaAndEstadoAndEliminadoOrderByTurnoAsc(String codeStatus, LocalDate date, int status, boolean deleted);
    default List<OrdenAtencionEntity> getByCodeStatusAndDate(String codeStatus, LocalDate date) {
        return findByCodEstadoAtencionAndFechaAndEstadoAndEliminadoOrderByTurnoAsc(codeStatus, date, 1, false);
    }

    @EntityGraph(attributePaths = {"persona"})
    Optional<OrdenAtencionEntity> findFirstByCodPrioridadAndCodEstadoAtencionAndFechaAndEstadoAndEliminadoOrderByTurnoAsc(String codePriority, String codeStatus, LocalDate date, int status, boolean deteled);
    default Optional<OrdenAtencionEntity> getOrderInCall(String codePriority, LocalDate date) {
        return findFirstByCodPrioridadAndCodEstadoAtencionAndFechaAndEstadoAndEliminadoOrderByTurnoAsc(codePriority, TablaMaestraServiceImpl.EN_LLAMADA, date, 1, false);
    }
    default Optional<OrdenAtencionEntity> getNextOrderPending(String codePriority, LocalDate date) {
        return findFirstByCodPrioridadAndCodEstadoAtencionAndFechaAndEstadoAndEliminadoOrderByTurnoAsc(codePriority, TablaMaestraServiceImpl.PENDIENTE, date, 1, false);
    }

    @EntityGraph(attributePaths = {"persona"})
    Optional<OrdenAtencionEntity> findFirstByCodPrioridadAndCodEstadoAtencionInAndFechaAndCodVentanillaAndEstadoAndEliminadoOrderByTurnoAsc(
            String codePriority,
            List<String> codeStatusList,
            LocalDate date,
            String codeVentanilla,
            int status,
            boolean deleted
    );
    default Optional<OrdenAtencionEntity> getOrderInVentanilla(String codePriority, LocalDate date, String codeVentanilla) {
        return findFirstByCodPrioridadAndCodEstadoAtencionInAndFechaAndCodVentanillaAndEstadoAndEliminadoOrderByTurnoAsc(
                codePriority,
                List.of(
                        TablaMaestraServiceImpl.EN_LLAMADA,
                        TablaMaestraServiceImpl.ATENDIENDO
                ),
                date,
                codeVentanilla,
                1,
                false
        );
    }

    @EntityGraph(attributePaths = {"persona"})
    List<OrdenAtencionEntity> findByFechaAndReceptor_UsuarioIdAndEliminadoOrderByTurnoAsc(LocalDate date, Long receptorId, boolean deleted);
    default List<OrdenAtencionEntity> getByDateAndReceptor(LocalDate date, Long receptorId) {
        return findByFechaAndReceptor_UsuarioIdAndEliminadoOrderByTurnoAsc(date, receptorId, false);
    }

    @EntityGraph(attributePaths = {"persona"})
    List<OrdenAtencionEntity> findByFechaAndEliminadoOrderByTurnoAsc(LocalDate date, boolean deleted);
    default List<OrdenAtencionEntity> getByDate(LocalDate date) {
        return findByFechaAndEliminadoOrderByTurnoAsc(date, false);
    }

    @Query(value = "SELECT * FROM turnos.TUR_SEL_OrdenesAtencionDetallado(:p_fecha)", nativeQuery = true)
    List<OrdenAtencionDetailProjection> getRecordsByDate(@Param("p_fecha") LocalDate fecha);

    @Query(value = "SELECT * FROM turnos.TUR_SEL_OrdenesAtencionAsesorPorFechaAndAsesor(:p_asesor_id, :p_fecha)", nativeQuery = true)
    List<OrdenAtencionDetailProjection> getRecordsByAsesorAndDate(@Param("p_asesor_id") Long asesorId, @Param("p_fecha") LocalDate fecha);
}
