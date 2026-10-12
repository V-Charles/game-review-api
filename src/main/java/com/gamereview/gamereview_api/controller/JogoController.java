package com.gamereview.gamereview_api.controller;

import com.gamereview.gamereview_api.model.Jogo;
import com.gamereview.gamereview_api.service.JogoService;
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
@RequestMapping("/jogos")
@Tag(name = "Jogos", description = "Endpoints para gerenciamento de jogos e relacionamento com estúdios")
public class JogoController {

    @Autowired
    private JogoService service;

    @Autowired
    private PagedResourcesAssembler<Jogo> pagedResourcesAssembler;

    @PostMapping
    @Operation(summary = "Cadastrar um novo jogo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Jogo criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação (campos vazios ou inválidos)"),
            @ApiResponse(responseCode = "409", description = "Conflito de dados (ex: violação de integridade ou chave estrangeira inexistente)")
    })

    public ResponseEntity<EntityModel<Jogo>> create(@RequestBody @Valid Jogo jogo){
        Jogo saved = service.create(jogo);
        return ResponseEntity.status(HttpStatus.CREATED).body(toHateoas(saved));
    }

    @GetMapping
    @Operation(summary = "Listar todos os jogos de forma paginada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de jogos recuperada com sucesso")
    })

    public ResponseEntity<PagedModel<EntityModel<Jogo>>> findAll(@PageableDefault(size = 10) Pageable pageable){
        Page<Jogo> page = service.findAll(pageable);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(page, this::toHateoas));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar um jogo pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Jogo encontrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "ID fornecido é inválido (erro de tipo)"),
            @ApiResponse(responseCode = "404", description = "Jogo não encontrado")
    })

    public ResponseEntity<EntityModel<Jogo>> findById(@PathVariable Integer id){
        return service.findById(id)
                .map(jogo -> ResponseEntity.ok(toHateoas(jogo)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar os dados de um jogo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Jogo atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação ou ID inválido"),
            @ApiResponse(responseCode = "404", description = "Jogo não encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflito de dados")
    })

    public ResponseEntity<EntityModel<Jogo>> update(@PathVariable Integer id, @RequestBody @Valid Jogo jogo){
        return service.update(id, jogo)
                .map(update -> ResponseEntity.ok(toHateoas(update)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir um jogo pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Jogo excluído com sucesso"),
            @ApiResponse(responseCode = "400", description = "ID fornecido é inválido"),
            @ApiResponse(responseCode = "404", description = "Jogo não encontrado")
    })

    public ResponseEntity<Void> delete(@PathVariable Integer id){
        if(!service.delete(id)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar jogos pelo título (paginado)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    })

    public ResponseEntity<PagedModel<EntityModel<Jogo>>> searchByTitulo(@RequestParam String titulo, @PageableDefault(size = 10) Pageable pageable){
        Page<Jogo> page = service.searchByTitulo(titulo, pageable);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(page, this::toHateoas));
    }

    private EntityModel<Jogo> toHateoas(Jogo jogo){
        return EntityModel.of(jogo,
                linkTo(methodOn(JogoController.class).findById(jogo.getId())).withSelfRel(),
                linkTo(methodOn(JogoController.class).findAll(Pageable.unpaged())).withRel("todos_jogos"));
    }
}