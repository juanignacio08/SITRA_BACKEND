package com.sitra.sitra.repository.seguridad;

import com.sitra.sitra.entity.seguridad.RolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends JpaRepository<RolEntity, Long> {

}
