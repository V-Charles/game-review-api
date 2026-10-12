package com.gamereview.gamereview_api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "tb_listas_jogos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListasDeJogos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID auto-gerado pelo banco de dados", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @NotBlank(message = "O nome da lista é obrigatório")
    @Schema(description = "Nome personalizado da lista", example = "Favoritos")
    @Column(nullable = false)
    private String nome;

    @Schema(description = "Descrição ou propósito da lista", example = "Todos os que mais gostei")
    private String descricao;

    @Schema(description = "Define se a lista é visível para outros usuários", example = "true")
    private Boolean isPublica;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToMany
    @JoinTable(
            name = "tb_lista_jogo_relacao",
            joinColumns = @JoinColumn(name = "lista_id"),
            inverseJoinColumns = @JoinColumn(name = "jogo_id")
    )
    private List<Jogo> jogos;
}