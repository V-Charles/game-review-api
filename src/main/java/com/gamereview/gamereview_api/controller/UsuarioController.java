package com.gamereview.gamereview_api.controller;

import com.gamereview.gamereview_api.model.Usuario;
import com.gamereview.gamereview_api.repository.UsuarioRepository;
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

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PagedResourcesAssembler<Usuario> pagedResourcesAssembler;

    @PostMapping
    @Operation(summary = "Cadastrar um novo usuário")
    public ResponseEntity<EntityModel<Usuario>> create(@RequestBody @Valid Usuario usuario){
        Usuario saved = repository.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(toHateoas(saved));
    }

    @GetMapping
    @Operation(summary = "Listar todos os usuários de forma paginada")
    public ResponseEntity<PagedModel<EntityModel<Usuario>>> findAll(@PageableDefault(size = 10)Pageable pageable){
        Page<Usuario> page = repository.findAll(pageable);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(page, this::toHateoas));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar um usuário pelo ID")
    public ResponseEntity<EntityModel<Usuario>> findById(@PathVariable Integer id){
        return repository.findById(id)
                .map(usuario -> ResponseEntity.ok(toHateoas(usuario)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar os dados de um usuário")
    public ResponseEntity<EntityModel<Usuario>> update(@PathVariable Integer id, @RequestBody @Valid Usuario usuario){
        if(!repository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        usuario.setId(id);
        Usuario update = repository.save(usuario);
        return ResponseEntity.ok(toHateoas(update));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir um usuário pelo ID")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        if(!repository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar usuário por e-mail exato")
    public ResponseEntity<EntityModel<Usuario>> searchByEmail(@RequestParam String email){
        Optional<Usuario> usuario = repository.findByEmail(email);
        return usuario.map(value -> ResponseEntity.ok(toHateoas(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private EntityModel<Usuario> toHateoas(Usuario usuario){
        return EntityModel.of(usuario,
                linkTo(methodOn(UsuarioController.class).findById(usuario.getId())).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).findAll(Pageable.unpaged())).withRel("todos_usuarios"));
    }
}
