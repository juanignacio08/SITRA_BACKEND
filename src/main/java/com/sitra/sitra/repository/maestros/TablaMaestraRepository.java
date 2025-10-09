package com.sitra.sitra.repository.maestros;

import com.sitra.sitra.entity.maestros.TablaMaestraEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TablaMaestraRepository extends JpaRepository<TablaMaestraEntity, Long> {

}
