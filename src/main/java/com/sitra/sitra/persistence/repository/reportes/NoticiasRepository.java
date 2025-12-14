package com.sitra.sitra.persistence.repository.reportes;

import com.sitra.sitra.entity.reportes.NoticiasEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoticiasRepository extends JpaRepository<NoticiasEntity, Long> {

    Optional<NoticiasEntity> findByNoticiasIdAndEliminado(Long id, boolean deleted);
    default Optional<NoticiasEntity> getByID(Long id) {
        return findByNoticiasIdAndEliminado(id, false);
    }

    List<NoticiasEntity> findByEliminadoOrderByNoticiasIdAsc(boolean deleted);
    default List<NoticiasEntity> getAll() {
        return findByEliminadoOrderByNoticiasIdAsc(false);
    }

    List<NoticiasEntity> findByEstadoAndEliminadoOrderByNoticiasIdAsc(Integer status, boolean deleted);
    default List<NoticiasEntity> getAllActives() {
        return findByEstadoAndEliminadoOrderByNoticiasIdAsc(1, false);
    }

}
