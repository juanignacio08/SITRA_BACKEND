package com.sitra.sitra.repository.seguridad;

import com.sitra.sitra.entity.seguridad.PersonaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonaRepository extends JpaRepository<PersonaEntity, Long> {

    boolean existsByNumeroDocumentoIdentidadAndEliminado(String numberDocument, Boolean eliminado);
    default boolean existsPersona(String numberDocument) {
        return existsByNumeroDocumentoIdentidadAndEliminado(numberDocument, false);
    }

    Optional<PersonaEntity> findByNumeroDocumentoIdentidadAndEliminado(String numberDocument, Boolean eliminado);
    default Optional<PersonaEntity> getByNumberDocument(String numberDocument) {
        return findByNumeroDocumentoIdentidadAndEliminado(numberDocument, false);
    }

    Optional<PersonaEntity> findByPersonaIdAndEliminado(Long id, Boolean eliminado);
    default Optional<PersonaEntity> getByID(Long id) {
        return findByPersonaIdAndEliminado(id, false);
    }

}
