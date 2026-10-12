package com.gamereview.gamereview_api.controller;

import com.gamereview.gamereview_api.model.Avaliacao;
import com.gamereview.gamereview_api.repository.AvaliacaoRepository;
import io.swagger.v3.oas.annotations.Operation;
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
    private AvaliacaoRepository repository;

    @Autowired
    private PagedResourcesAssembler<Avaliacao> pagedResourcesAssembler;

    @PostMapping
    @Operation(summary = "Criar uma nova avaliação para um jogo")
    public ResponseEntity<EntityModel<Avaliacao>> create(@RequestBody @Valid Avaliacao avaliacao){
        Avaliacao saved = repository.save(avaliacao);
        return ResponseEntity.status(HttpStatus.CREATED).body(toHateoas(saved));
    }

    @GetMapping
    @Operation(summary = "Listar todas as avaliações de forma paginada")
    public ResponseEntity<PagedModel<EntityModel<Avaliacao>>> findAll(@PageableDefault(size = 10) Pageable pageable){
        Page<Avaliacao> page = repository.findAll(pageable);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(page, this::toHateoas));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar uma avaliação pelo ID")
    public ResponseEntity<EntityModel<Avaliacao>> findById(@PathVariable Integer id){
        return repository.findById(id)
                .map(avaliacao -> ResponseEntity.ok(toHateoas(avaliacao)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar uma avaliação")
    public ResponseEntity<EntityModel<Avaliacao>> update(@PathVariable Integer id, @RequestBody @Valid Avaliacao avaliacao){
        if(!repository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        avaliacao.setId(id);
        Avaliacao update = repository.save(avaliacao);
        return ResponseEntity.ok(toHateoas(update));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir uma avaliação")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        if(!repository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar avaliações por ID do jogo (paginado)")
    public ResponseEntity<PagedModel<EntityModel<Avaliacao>>> searchByJogo(
            @RequestParam Integer jogoId,
            @PageableDefault(size = 10) Pageable pageable){
        Page<Avaliacao> page = repository.findByJogoId(jogoId, pageable);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(page, this::toHateoas));
    }

    private EntityModel<Avaliacao> toHateoas(Avaliacao avaliacao){
        return EntityModel.of(avaliacao,
                linkTo(methodOn(AvaliacaoController.class).findById(avaliacao.getId())).withSelfRel(),
                linkTo(methodOn(AvaliacaoController.class).findAll(Pageable.unpaged())).withRel("todas_avaliacoes"));
    }
}