package com.gamereview.gamereview_api.repositories;

import com.gamereview.gamereview_api.model.ListasDeJogos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListaDeJogosRepository extends JpaRepository<ListasDeJogos, Integer> {

    Page<ListasDeJogos> findByUsuarioId(Integer usuarioId, Pageable pageable);
    Page<ListasDeJogos> findByIsPublicaTrue(Pageable pageable);
}
