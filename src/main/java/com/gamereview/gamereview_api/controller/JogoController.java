package com.gamereview.gamereview_api.controller;

import com.gamereview.gamereview_api.model.Jogo;
import com.gamereview.gamereview_api.repository.JogoRepository;
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
@RequestMapping("/jogos")
@Tag(name = "Jogos", description = "Endpoints para gerenciamento de jogos e relacionamento com estúdios")
public class JogoController {

    @Autowired
    private JogoRepository repository;

    @Autowired
    private PagedResourcesAssembler<Jogo> pagedResourcesAssembler;

    @PostMapping
    @Operation(summary = "Cadastrar um novo jogo")
    public ResponseEntity<EntityModel<Jogo>> create(@RequestBody @Valid Jogo jogo){
        Jogo saved = repository.save(jogo);
        return ResponseEntity.status(HttpStatus.CREATED).body(toHateoas(saved));
    }

    @GetMapping
    @Operation(summary = "Listar todos os jogos de forma paginada")
    public ResponseEntity<PagedModel<EntityModel<Jogo>>> findAll(@PageableDefault(size = 10) Pageable pageable){
        Page<Jogo> page = repository.findAll(pageable);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(page, this::toHateoas));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar um jogo pelo ID")
    public ResponseEntity<EntityModel<Jogo>> findById(@PathVariable Integer id){
        return repository.findById(id)
                .map(jogo -> ResponseEntity.ok(toHateoas(jogo)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar os dados de um jogo")
    public ResponseEntity<EntityModel<Jogo>> update(@PathVariable Integer id, @RequestBody @Valid Jogo jogo){
        if(!repository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        jogo.setId(id);
        Jogo update = repository.save(jogo);
        return ResponseEntity.ok(toHateoas(update));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir um jogo pelo ID")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        if(!repository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar jogos pelo título (paginado)")
    public ResponseEntity<PagedModel<EntityModel<Jogo>>> searchByTitulo(@RequestParam String titulo, @PageableDefault(size = 10) Pageable pageable){
        Page<Jogo> page = repository.findByTituloContainingIgnoreCase(titulo, pageable);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(page, this::toHateoas));
    }

    private EntityModel<Jogo> toHateoas(Jogo jogo){
        return EntityModel.of(jogo,
                linkTo(methodOn(JogoController.class).findById(jogo.getId())).withSelfRel(),
                linkTo(methodOn(JogoController.class).findAll(Pageable.unpaged())).withRel("todos_jogos"));
    }
}