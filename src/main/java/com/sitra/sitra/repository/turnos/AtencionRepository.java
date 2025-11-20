package com.sitra.sitra.repository.turnos;

import com.sitra.sitra.entity.turnos.AtencionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AtencionRepository extends JpaRepository<AtencionEntity, Long> {

}
