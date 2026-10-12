package com.gamereview.gamereview_api.service;

import com.gamereview.gamereview_api.model.Usuario;
import com.gamereview.gamereview_api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    public Usuario create(Usuario usuario) {
        return repository.save(usuario);
    }

    public Page<Usuario> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Optional<Usuario> findById(Integer id) {
        return repository.findById(id);
    }

    public Optional<Usuario> update(Integer id, Usuario usuario) {
        if (!repository.existsById(id)) {
            return Optional.empty();
        }
        usuario.setId(id);
        return Optional.of(repository.save(usuario));
    }

    public boolean delete(Integer id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }

    public Optional<Usuario> searchByEmail(String email) {
        return repository.findByEmail(email);
    }
}