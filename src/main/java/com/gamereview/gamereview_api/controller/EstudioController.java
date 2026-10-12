package com.gamereview.gamereview_api.controller;

import com.gamereview.gamereview_api.model.Estudio;
import com.gamereview.gamereview_api.service.EstudioService;
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
@RequestMapping("/estudios")
@Tag(name = "Estúdios", description = "Endpoints para gerenciamento de estúdios de jogos")
public class EstudioController {

    @Autowired
    private EstudioService service;

    @Autowired
    private PagedResourcesAssembler<Estudio> pagedResourcesAssembler;

    @PostMapping
    @Operation(summary = "Cadastrar um novo estúdio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Estúdio criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação (campos vazios ou inválidos)"),
            @ApiResponse(responseCode = "409", description = "Conflito de dados (estúdio já cadastrado)")
    })
    public ResponseEntity<EntityModel<Estudio>> create(@RequestBody @Valid Estudio estudio){
        Estudio saved = service.create(estudio);
        return ResponseEntity.status(HttpStatus.CREATED).body(toHateoas(saved));
    }

    @GetMapping
    @Operation(summary = "Listar todos os estúdios de forma paginada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de estúdios recuperada com sucesso")
    })
    public ResponseEntity<PagedModel<EntityModel<Estudio>>> findAll(@PageableDefault(size = 10) Pageable pageable){
        Page<Estudio> page = service.findAll(pageable);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(page, this::toHateoas));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar um estúdio pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estúdio encontrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "ID fornecido é inválido (erro de tipo, ex: texto em vez de número)"),
            @ApiResponse(responseCode = "404", description = "Estúdio não encontrado")
    })
    public ResponseEntity<EntityModel<Estudio>> findById(@PathVariable Integer id){
        return service.findById(id)
                .map(estudio -> ResponseEntity.ok(toHateoas(estudio)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar os dados de um estúdio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estúdio atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação ou ID inválido"),
            @ApiResponse(responseCode = "404", description = "Estúdio não encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflito de dados")
    })
    public ResponseEntity<EntityModel<Estudio>> update(@PathVariable Integer id, @RequestBody @Valid Estudio estudio){
        return service.update(id, estudio)
                .map(update -> ResponseEntity.ok(toHateoas(update)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir um estúdio pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Estúdio excluído com sucesso"),
            @ApiResponse(responseCode = "400", description = "ID fornecido é inválido"),
            @ApiResponse(responseCode = "404", description = "Estúdio não encontrado")
    })
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        if(!service.delete(id)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar estúdios pelo nome (paginado)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    })
    public ResponseEntity<PagedModel<EntityModel<Estudio>>> searchByName(@RequestParam String nome, @PageableDefault(size = 10) Pageable pageable){
        Page<Estudio> page = service.searchByName(nome, pageable);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(page, this::toHateoas));
    }

    private EntityModel<Estudio> toHateoas(Estudio estudio){
        return EntityModel.of(estudio,
                linkTo(methodOn(EstudioController.class).findById(estudio.getId())).withSelfRel(),
                linkTo(methodOn(EstudioController.class).findAll(Pageable.unpaged())).withRel("todos_estudios"));
    }
}