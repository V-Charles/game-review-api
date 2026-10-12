package com.gamereview.gamereview_api.controller;

import com.gamereview.gamereview_api.model.PerfilDoUsuario;
import com.gamereview.gamereview_api.repository.PerfilDoUsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/perfis")
@Tag(name = "Perfis de Usuário", description = "Endpoints para gerenciamento de perfis (biografia, data de nascimento)")
public class PerfilDoUsuarioController {

    @Autowired
    private PerfilDoUsuarioRepository repository;

    @Autowired
    private PagedResourcesAssembler<PerfilDoUsuario> pagedResourcesAssembler;

    @PostMapping
    @Operation(summary = "Criar um perfil estendido para um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Perfil criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação (campos inválidos)"),
            @ApiResponse(responseCode = "409", description = "Conflito de dados (ex: usuário já possui perfil ou ID de usuário inexistente)")
    })
    public ResponseEntity<EntityModel<PerfilDoUsuario>> create(@RequestBody @Valid PerfilDoUsuario perfil) {
        PerfilDoUsuario saved = repository.save(perfil);
        return ResponseEntity.status(HttpStatus.CREATED).body(toHateoas(saved));
    }

    @GetMapping
    @Operation(summary = "Listar todos os perfis de forma paginada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de perfis recuperada com sucesso")
    })
    public ResponseEntity<PagedModel<EntityModel<PerfilDoUsuario>>> findAll(@PageableDefault(size = 10) Pageable pageable) {
        Page<PerfilDoUsuario> page = repository.findAll(pageable);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(page, this::toHateoas));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar um perfil pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil encontrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "ID fornecido é inválido (erro de tipo)"),
            @ApiResponse(responseCode = "404", description = "Perfil não encontrado")
    })
    public ResponseEntity<EntityModel<PerfilDoUsuario>> findById(@PathVariable Integer id) {
        return repository.findById(id)
                .map(perfil -> ResponseEntity.ok(toHateoas(perfil)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um perfil")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação ou ID inválido"),
            @ApiResponse(responseCode = "404", description = "Perfil não encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflito de dados")
    })
    public ResponseEntity<EntityModel<PerfilDoUsuario>> update(@PathVariable Integer id, @RequestBody @Valid PerfilDoUsuario perfil) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        perfil.setId(id);
        PerfilDoUsuario update = repository.save(perfil);
        return ResponseEntity.ok(toHateoas(update));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir um perfil")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Perfil excluído com sucesso"),
            @ApiResponse(responseCode = "400", description = "ID fornecido é inválido"),
            @ApiResponse(responseCode = "404", description = "Perfil não encontrado")
    })
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar perfil pelo ID do Usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil encontrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetro fornecido é inválido"),
            @ApiResponse(responseCode = "404", description = "Nenhum perfil encontrado para o usuário fornecido")
    })
    public ResponseEntity<EntityModel<PerfilDoUsuario>> searchByUsuarioId(@RequestParam Integer usuarioId) {
        Optional<PerfilDoUsuario> perfil = repository.findByUsuarioId(usuarioId);
        return perfil.map(value -> ResponseEntity.ok(toHateoas(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private EntityModel<PerfilDoUsuario> toHateoas(PerfilDoUsuario perfil) {
        return EntityModel.of(perfil,
                linkTo(methodOn(PerfilDoUsuarioController.class).findById(perfil.getId())).withSelfRel(),
                linkTo(methodOn(PerfilDoUsuarioController.class).findAll(Pageable.unpaged())).withRel("todos_perfis"));
    }
}