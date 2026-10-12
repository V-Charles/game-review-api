package com.gamereview.gamereview_api.service;

import com.gamereview.gamereview_api.model.Avaliacao;
import com.gamereview.gamereview_api.repository.AvaliacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AvaliacaoService {

    @Autowired
    private AvaliacaoRepository repository;

    public Avaliacao create(Avaliacao avaliacao) {
        return repository.save(avaliacao);
    }

    public Page<Avaliacao> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Optional<Avaliacao> findById(Integer id) {
        return repository.findById(id);
    }

    public Optional<Avaliacao> update(Integer id, Avaliacao avaliacao) {
        if (!repository.existsById(id)) {
            return Optional.empty();
        }
        avaliacao.setId(id);
        return Optional.of(repository.save(avaliacao));
    }

    public boolean delete(Integer id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }

    public Page<Avaliacao> searchByJogo(Integer jogoId, Pageable pageable) {
        return repository.findByJogoId(jogoId, pageable);
    }
}