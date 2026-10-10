package com.gamereview.gamereview_api.controller;

import com.gamereview.gamereview_api.model.Estudio;
import com.gamereview.gamereview_api.repository.EstudioRepository;
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
@RequestMapping("/estudios")
@Tag(name = "Estúdios", description = "Endpoints para gerenciamento de estúdios de jogos")
public class EstudioController {

    @Autowired
    private EstudioRepository repository;

    @Autowired
    private PagedResourcesAssembler<Estudio> pagedResourcesAssembler;

    @PostMapping
    @Operation(summary = "Cadastrar um novo estúdio")
    public ResponseEntity<EntityModel<Estudio>> create(@RequestBody @Valid Estudio estudio){
        Estudio saved = repository.save(estudio);
        return ResponseEntity.status(HttpStatus.CREATED).body(toHateoas(saved));
    }

    @GetMapping
    @Operation(summary = "Listar todos os estúdios de forma paginada")
    public ResponseEntity<PagedModel<EntityModel<Estudio>>> findAll(@PageableDefault(size = 10) Pageable pageable){
        Page<Estudio> page = repository.findAll(pageable);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(page, this::toHateoas));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar um estúdio pelo ID")
    public ResponseEntity<EntityModel<Estudio>> findById(@PathVariable Integer id){
        return repository.findById(id)
                .map(estudio -> ResponseEntity.ok(toHateoas(estudio)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar os dados de um estúdio")
    public ResponseEntity<EntityModel<Estudio>> update(@PathVariable Integer id, @RequestBody @Valid Estudio estudio){
        if(!repository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        estudio.setId(id);
        Estudio update = repository.save(estudio);
        return ResponseEntity.ok(toHateoas(update));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir um estúdio pelo ID")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        if(!repository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar estúdios pelo nome (paginado)")
    public ResponseEntity<PagedModel<EntityModel<Estudio>>> searchByName(@RequestParam String nome, @PageableDefault(size = 10) Pageable pageable){
        Page<Estudio> page = repository.findByNomeEstudioContainingIgnoreCase(nome, pageable);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(page, this::toHateoas));
    }

    private EntityModel<Estudio> toHateoas(Estudio estudio){
        return EntityModel.of(estudio,
                linkTo(methodOn(EstudioController.class).findById(estudio.getId())).withSelfRel(),
                linkTo(methodOn(EstudioController.class).findAll(Pageable.unpaged())).withRel("todos_estudios"));
    }
}
