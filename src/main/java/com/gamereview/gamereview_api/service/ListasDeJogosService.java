package com.gamereview.gamereview_api.service;

import com.gamereview.gamereview_api.model.ListasDeJogos;
import com.gamereview.gamereview_api.repository.ListasDeJogosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ListasDeJogosService {

    @Autowired
    private ListasDeJogosRepository repository;

    public ListasDeJogos create(ListasDeJogos lista) {
        return repository.save(lista);
    }

    public Page<ListasDeJogos> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Optional<ListasDeJogos> findById(Integer id) {
        return repository.findById(id);
    }

    public Optional<ListasDeJogos> update(Integer id, ListasDeJogos lista) {
        if (!repository.existsById(id)) {
            return Optional.empty();
        }
        lista.setId(id);
        return Optional.of(repository.save(lista));
    }

    public boolean delete(Integer id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }

    public Page<ListasDeJogos> searchPublicas(Pageable pageable) {
        return repository.findByIsPublicaTrue(pageable);
    }

    public Page<ListasDeJogos> searchByUsuario(Integer usuarioId, Pageable pageable) {
        return repository.findByUsuarioId(usuarioId, pageable);
    }
}