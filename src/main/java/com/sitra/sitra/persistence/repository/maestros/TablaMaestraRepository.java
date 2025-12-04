package com.sitra.sitra.persistence.repository.maestros;

import com.sitra.sitra.entity.maestros.TablaMaestraEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TablaMaestraRepository extends JpaRepository<TablaMaestraEntity, Long> {

    List<TablaMaestraEntity> findByCodigoTablaAndCodigoItemNotAndEstadoAndEliminado(String codeTable, String codeItem, int estado, Boolean eliminado);
    default List<TablaMaestraEntity> getItems(String codeTable) {
        return findByCodigoTablaAndCodigoItemNotAndEstadoAndEliminado(codeTable, "000", 1, false);
    }

    Optional<TablaMaestraEntity> findByIdTablaMaestraAndEliminado(Long id, Boolean eliminado);
    default Optional<TablaMaestraEntity> getByID(Long id) {
        return findByIdTablaMaestraAndEliminado(id, false);
    }

    Optional<TablaMaestraEntity> findByCodigoAndEliminado(String code, Boolean eliminado);
    default Optional<TablaMaestraEntity> getByCode(String code) {
        return findByCodigoAndEliminado(code, false);
    }

    boolean existsByCodigoAndEliminado(String code, Boolean eliminado);
    default boolean existsByCode(String code) {
        return existsByCodigoAndEliminado(code, false);
    }

}
