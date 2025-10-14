package com.sitra.sitra.repository.maestros;

import com.sitra.sitra.entity.maestros.TablaMaestraEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TablaMaestraRepository extends JpaRepository<TablaMaestraEntity, Long> {

    List<TablaMaestraEntity> findByCodigoTablaAndCodigoItemNotAndEstadoAndEliminado(String codeTable, String codeItem, int estado, Boolean eliminado);
    default List<TablaMaestraEntity> getItems(String codeTable) {
        return findByCodigoTablaAndCodigoItemNotAndEstadoAndEliminado(codeTable, "000", 1, false);
    }

}
