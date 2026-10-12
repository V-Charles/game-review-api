package com.gamereview.gamereview_api.repository;

import com.gamereview.gamereview_api.model.Estudio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstudioRepository extends JpaRepository<Estudio, Integer> {

    Page<Estudio> findByNomeEstudioContainingIgnoreCase(String nomeEstudio, Pageable pageable);
}
