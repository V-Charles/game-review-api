package com.gamereview.gamereview_api.service;

import com.gamereview.gamereview_api.model.Jogo;
import com.gamereview.gamereview_api.repository.JogoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JogoService {

    @Autowired
    private JogoRepository repository;

    public Jogo create(Jogo jogo) {
        return repository.save(jogo);
    }

    public Page<Jogo> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Optional<Jogo> findById(Integer id) {
        return repository.findById(id);
    }

    public Optional<Jogo> update(Integer id, Jogo jogo) {
        if (!repository.existsById(id)) {
            return Optional.empty();
        }
        jogo.setId(id);
        return Optional.of(repository.save(jogo));
    }

    public boolean delete(Integer id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }

    public Page<Jogo> searchByTitulo(String titulo, Pageable pageable) {
        return repository.findByTituloContainingIgnoreCase(titulo, pageable);
    }
}