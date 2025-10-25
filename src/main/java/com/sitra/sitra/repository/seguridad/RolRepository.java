package com.sitra.sitra.repository.seguridad;

import com.sitra.sitra.entity.seguridad.RolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<RolEntity, Long> {

    Optional<RolEntity> findByRolIdAndEliminado(Long id, boolean eliminado);
    default Optional<RolEntity> getByID(Long id) {
        return findByRolIdAndEliminado(id, false);
    }
}
