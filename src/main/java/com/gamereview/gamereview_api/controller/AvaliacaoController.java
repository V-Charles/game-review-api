package com.gamereview.gamereview_api.controller;

import com.gamereview.gamereview_api.model.Avaliacao;
import com.gamereview.gamereview_api.service.AvaliacaoService;
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
@RequestMapping("/avaliacoes")
@Tag(name = "Avaliações", description = "Endpoints para gerenciamento de notas, comentários e progresso dos jogos")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService service;

    @Autowired
    private PagedResourcesAssembler<Avaliacao> pagedResourcesAssembler;

    @PostMapping
    @Operation(summary = "Criar uma nova avaliação para um jogo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Avaliação criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação (campos vazios ou valores inválidos)"),
            @ApiResponse(responseCode = "409", description = "Conflito de dados (ex: IDs de usuário ou jogo inexistentes)")
    })

    public ResponseEntity<EntityModel<Avaliacao>> create(@RequestBody @Valid Avaliacao avaliacao){
        Avaliacao saved = service.create(avaliacao);
        return ResponseEntity.status(HttpStatus.CREATED).body(toHateoas(saved));
    }

    @GetMapping
    @Operation(summary = "Listar todas as avaliações de forma paginada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de avaliações recuperada com sucesso")
    })

    public ResponseEntity<PagedModel<EntityModel<Avaliacao>>> findAll(@PageableDefault(size = 10) Pageable pageable){
        Page<Avaliacao> page = service.findAll(pageable);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(page, this::toHateoas));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar uma avaliação pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Avaliação encontrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "ID fornecido é inválido (erro de tipo)"),
            @ApiResponse(responseCode = "404", description = "Avaliação não encontrada")
    })

    public ResponseEntity<EntityModel<Avaliacao>> findById(@PathVariable Integer id){
        return service.findById(id)
                .map(avaliacao -> ResponseEntity.ok(toHateoas(avaliacao)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar uma avaliação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Avaliação atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação ou ID inválido"),
            @ApiResponse(responseCode = "404", description = "Avaliação não encontrada"),
            @ApiResponse(responseCode = "409", description = "Conflito de dados")
    })

    public ResponseEntity<EntityModel<Avaliacao>> update(@PathVariable Integer id, @RequestBody @Valid Avaliacao avaliacao){
        return service.update(id, avaliacao)
                .map(update -> ResponseEntity.ok(toHateoas(update)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir uma avaliação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Avaliação excluída com sucesso"),
            @ApiResponse(responseCode = "400", description = "ID fornecido é inválido"),
            @ApiResponse(responseCode = "404", description = "Avaliação não encontrada")
    })

    public ResponseEntity<Void> delete(@PathVariable Integer id){
        if(!service.delete(id)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar avaliações por ID do jogo (paginado)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetro ID do jogo fornecido é inválido")
    })

    public ResponseEntity<PagedModel<EntityModel<Avaliacao>>> searchByJogo(
            @RequestParam Integer jogoId,
            @PageableDefault(size = 10) Pageable pageable){
        Page<Avaliacao> page = service.searchByJogo(jogoId, pageable);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(page, this::toHateoas));
    }

    private EntityModel<Avaliacao> toHateoas(Avaliacao avaliacao){
        return EntityModel.of(avaliacao,
                linkTo(methodOn(AvaliacaoController.class).findById(avaliacao.getId())).withSelfRel(),
                linkTo(methodOn(AvaliacaoController.class).findAll(Pageable.unpaged())).withRel("todas_avaliacoes"));
    }
}