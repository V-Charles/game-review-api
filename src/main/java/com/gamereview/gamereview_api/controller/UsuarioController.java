package com.gamereview.gamereview_api.controller;

import com.gamereview.gamereview_api.model.Usuario;
import com.gamereview.gamereview_api.service.UsuarioService;
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
@RequestMapping("/usuarios")
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @Autowired
    private PagedResourcesAssembler<Usuario> pagedResourcesAssembler;

    @PostMapping
    @Operation(summary = "Cadastrar um novo usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação (campos vazios, tamanho de senha ou formato de e-mail)"),
            @ApiResponse(responseCode = "409", description = "Conflito de dados (e-mail ou nome de usuário já cadastrado)")
    })

    public ResponseEntity<EntityModel<Usuario>> create(@RequestBody @Valid Usuario usuario){
        Usuario saved = service.create(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(toHateoas(saved));
    }

    @GetMapping
    @Operation(summary = "Listar todos os usuários de forma paginada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuários recuperada com sucesso")
    })

    public ResponseEntity<PagedModel<EntityModel<Usuario>>> findAll(@PageableDefault(size = 10)Pageable pageable){
        Page<Usuario> page = service.findAll(pageable);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(page, this::toHateoas));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar um usuário pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "ID fornecido é inválido (erro de tipo)"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })

    public ResponseEntity<EntityModel<Usuario>> findById(@PathVariable Integer id){
        return service.findById(id)
                .map(usuario -> ResponseEntity.ok(toHateoas(usuario)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar os dados de um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação ou ID inválido"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflito de dados (e-mail ou nome de usuário em uso)")
    })

    public ResponseEntity<EntityModel<Usuario>> update(@PathVariable Integer id, @RequestBody @Valid Usuario usuario){
        return service.update(id, usuario)
                .map(update -> ResponseEntity.ok(toHateoas(update)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir um usuário pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso"),
            @ApiResponse(responseCode = "400", description = "ID fornecido é inválido"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })

    public ResponseEntity<Void> delete(@PathVariable Integer id){
        if(!service.delete(id)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar usuário por e-mail exato")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhum usuário encontrado com o e-mail fornecido")
    })

    public ResponseEntity<EntityModel<Usuario>> searchByEmail(@RequestParam String email){
        Optional<Usuario> usuario = service.searchByEmail(email);
        return usuario.map(value -> ResponseEntity.ok(toHateoas(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private EntityModel<Usuario> toHateoas(Usuario usuario){
        return EntityModel.of(usuario,
                linkTo(methodOn(UsuarioController.class).findById(usuario.getId())).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).findAll(Pageable.unpaged())).withRel("todos_usuarios"));
    }
}