package com.gamereview.gamereview_api.controller;

import com.gamereview.gamereview_api.service.ApiKeyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api-keys")
@Tag(name = "API Keys", description = "Endpoints para gerenciamento de chaves de API")
public class ApiKeyController {

    @Autowired
    private ApiKeyService service;

    @PostMapping("/usuario/{usuarioId}")
    @Operation(summary = "Gerar uma nova chave de API para o usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Chave gerada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<Object> gerarChave(@PathVariable Integer usuarioId) {
        return service.gerarChave(usuarioId).map(apiKey -> {
            Map<String, String> response = new HashMap<>();
            response.put("chave", apiKey.getChave());
            return ResponseEntity.status(HttpStatus.CREATED).body((Object) response);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}