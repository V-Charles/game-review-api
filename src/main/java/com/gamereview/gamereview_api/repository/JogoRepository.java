package com.gamereview.gamereview_api.repository;

import com.gamereview.gamereview_api.model.Jogo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JogoRepository extends JpaRepository<Jogo, Integer> {

    Page<Jogo> findByTituloContainingIgnoreCase(String titulo, Pageable pageable);
    Page<Jogo> findByEstudioId(Integer estudioId, Pageable pageable);
}
