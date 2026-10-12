package com.gamereview.gamereview_api.repository;

import com.gamereview.gamereview_api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByUserName(String userName);
}
