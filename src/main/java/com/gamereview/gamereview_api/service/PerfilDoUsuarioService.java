package com.gamereview.gamereview_api.service;

import com.gamereview.gamereview_api.model.PerfilDoUsuario;
import com.gamereview.gamereview_api.repository.PerfilDoUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PerfilDoUsuarioService {

    @Autowired
    private PerfilDoUsuarioRepository repository;

    public PerfilDoUsuario create(PerfilDoUsuario perfil) {
        return repository.save(perfil);
    }

    public Page<PerfilDoUsuario> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Optional<PerfilDoUsuario> findById(Integer id) {
        return repository.findById(id);
    }

    public Optional<PerfilDoUsuario> update(Integer id, PerfilDoUsuario perfil) {
        if (!repository.existsById(id)) {
            return Optional.empty();
        }
        perfil.setId(id);
        return Optional.of(repository.save(perfil));
    }

    public boolean delete(Integer id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }

    public Optional<PerfilDoUsuario> searchByUsuarioId(Integer usuarioId) {
        return repository.findByUsuarioId(usuarioId);
    }
}