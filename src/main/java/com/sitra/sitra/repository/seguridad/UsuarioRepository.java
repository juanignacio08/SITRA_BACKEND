package com.sitra.sitra.repository.seguridad;

import com.sitra.sitra.entity.seguridad.UsuarioEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    boolean existsByUsuarioAndEliminado(String user, boolean eliminado);
    default boolean existsUserByNumberDocument(String numberDocument) {
        return existsByUsuarioAndEliminado(numberDocument, false);
    }

    @EntityGraph(attributePaths = {"rol", "persona"})
    Optional<UsuarioEntity> findByUsuarioIdAndEliminado(Long userId, boolean eliminado);
    default Optional<UsuarioEntity> getDetailByID(Long userId) {
        return findByUsuarioIdAndEliminado(userId, false);
    }

    Optional<UsuarioEntity> findByUsuarioAndEliminado(String user, boolean eliminado);
    default Optional<UsuarioEntity> getByUser(String user) {
        return findByUsuarioAndEliminado(user, false);
    }

    @EntityGraph(attributePaths = {"rol", "persona"})
    List<UsuarioEntity> findByEliminado(boolean eliminado);
    default List<UsuarioEntity> getUsers() {
        return findByEliminado(false);
    }

}
