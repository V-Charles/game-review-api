package com.gamereview.gamereview_api.repositories;

import com.gamereview.gamereview_api.enums.StatusProgresso;
import com.gamereview.gamereview_api.model.Avaliacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Integer> {

    Page<Avaliacao> findByJogoId(Integer jogoId, Pageable pageable);
    Page<Avaliacao> findByUsuarioId(Integer usuarioId, Pageable pageable);
    Page<Avaliacao> findByStatus(StatusProgresso status, Pageable pageable);
}
