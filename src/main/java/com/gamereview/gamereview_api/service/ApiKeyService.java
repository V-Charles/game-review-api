package com.gamereview.gamereview_api.service;

import com.gamereview.gamereview_api.model.ApiKey;
import com.gamereview.gamereview_api.repository.ApiKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ApiKeyService {

    @Autowired
    private ApiKeyRepository apiKeyRepository;

    @Autowired
    private UsuarioService usuarioService;

    public Optional<ApiKey> gerarChave(Integer usuarioId) {
        return usuarioService.findById(usuarioId).map(usuario -> {
            ApiKey novaChave = new ApiKey();
            novaChave.setChave(UUID.randomUUID().toString());
            novaChave.setUsuario(usuario);
            novaChave.setAtivo(true);
            return apiKeyRepository.save(novaChave);
        });
    }
}