package com.gamereview.gamereview_api.repositories;

import com.gamereview.gamereview_api.model.PerfilDoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PerfilDoUsuarioRepository extends JpaRepository<PerfilDoUsuario, Integer> {

    Optional<PerfilDoUsuario> findByUsuarioId(Integer usuarioId);
}
