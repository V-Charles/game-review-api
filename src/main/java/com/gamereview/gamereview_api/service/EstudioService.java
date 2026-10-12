package com.gamereview.gamereview_api.service;

import com.gamereview.gamereview_api.model.Estudio;
import com.gamereview.gamereview_api.repository.EstudioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EstudioService {

    @Autowired
    private EstudioRepository repository;

    public Estudio create(Estudio estudio) {
        return repository.save(estudio);
    }

    public Page<Estudio> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Optional<Estudio> findById(Integer id) {
        return repository.findById(id);
    }

    public Optional<Estudio> update(Integer id, Estudio estudio) {
        if (!repository.existsById(id)) {
            return Optional.empty();
        }
        estudio.setId(id);
        return Optional.of(repository.save(estudio));
    }

    public boolean delete(Integer id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }

    public Page<Estudio> searchByName(String nome, Pageable pageable) {
        return repository.findByNomeEstudioContainingIgnoreCase(nome, pageable);
    }
}