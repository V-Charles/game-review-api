package com.gamereview.gamereview_api.controller;

import com.gamereview.gamereview_api.model.ListasDeJogos;
import com.gamereview.gamereview_api.service.ListasDeJogosService;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/listas")
@Tag(name = "Listas de Jogos", description = "Endpoints para gerenciamento de listas personalizadas de jogos dos usuários")
public class ListasDeJogosController {

    @Autowired
    private ListasDeJogosService service;

    @Autowired
    private PagedResourcesAssembler<ListasDeJogos> pagedResourcesAssembler;

    @PostMapping
    @Operation(summary = "Criar uma nova lista de jogos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Lista criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação (campos vazios)"),
            @ApiResponse(responseCode = "409", description = "Conflito de dados (ex: ID do usuário inexistente)")
    })

    public ResponseEntity<EntityModel<ListasDeJogos>> create(@RequestBody @Valid ListasDeJogos lista) {
        ListasDeJogos saved = service.create(lista);
        return ResponseEntity.status(HttpStatus.CREATED).body(toHateoas(saved));
    }

    @GetMapping
    @Operation(summary = "Listar todas as listas de jogos de forma paginada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relação de listas recuperada com sucesso")
    })

    public ResponseEntity<PagedModel<EntityModel<ListasDeJogos>>> findAll(@PageableDefault(size = 10) Pageable pageable) {
        Page<ListasDeJogos> page = service.findAll(pageable);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(page, this::toHateoas));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar uma lista de jogos pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista encontrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "ID fornecido é inválido"),
            @ApiResponse(responseCode = "404", description = "Lista não encontrada")
    })

    public ResponseEntity<EntityModel<ListasDeJogos>> findById(@PathVariable Integer id) {
        return service.findById(id)
                .map(lista -> ResponseEntity.ok(toHateoas(lista)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar uma lista de jogos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação ou ID inválido"),
            @ApiResponse(responseCode = "404", description = "Lista não encontrada"),
            @ApiResponse(responseCode = "409", description = "Conflito de dados")
    })

    public ResponseEntity<EntityModel<ListasDeJogos>> update(@PathVariable Integer id, @RequestBody @Valid ListasDeJogos lista) {
        return service.update(id, lista)
                .map(update -> ResponseEntity.ok(toHateoas(update)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir uma lista de jogos pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Lista excluída com sucesso"),
            @ApiResponse(responseCode = "400", description = "ID fornecido é inválido"),
            @ApiResponse(responseCode = "404", description = "Lista não encontrada")
    })

    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!service.delete(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search/publicas")
    @Operation(summary = "Buscar apenas listas públicas (paginado)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    })

    public ResponseEntity<PagedModel<EntityModel<ListasDeJogos>>> searchPublicas(@PageableDefault(size = 10) Pageable pageable) {
        Page<ListasDeJogos> page = service.searchPublicas(pageable);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(page, this::toHateoas));
    }

    @GetMapping("/search/usuario")
    @Operation(summary = "Buscar listas pelo ID do Usuário (paginado)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "ID do usuário fornecido é inválido")
    })

    public ResponseEntity<PagedModel<EntityModel<ListasDeJogos>>> searchByUsuario(@RequestParam Integer usuarioId, @PageableDefault(size = 10) Pageable pageable) {
        Page<ListasDeJogos> page = service.searchByUsuario(usuarioId, pageable);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(page, this::toHateoas));
    }

    private EntityModel<ListasDeJogos> toHateoas(ListasDeJogos lista) {
        return EntityModel.of(lista,
                linkTo(methodOn(ListasDeJogosController.class).findById(lista.getId())).withSelfRel(),
                linkTo(methodOn(ListasDeJogosController.class).findAll(Pageable.unpaged())).withRel("todas_listas"));
    }
}