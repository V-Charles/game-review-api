package com.gamereview.gamereview_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "tb_jogos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Jogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID auto-gerado pelo banco de dados", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Schema(description = "ID do jogo na base de dados externa (ex: IGDB)", example = "7360")
    private Long igdbId;

    @NotBlank(message = "O título do jogo é obrigatório")
    @Schema(description = "Título completo do jogo", example = "Tom Clancy's Rainbow Six Siege")
    @Column(nullable = false)
    private String titulo;

    @Schema(description = "URL da imagem de capa do jogo", example = "https://images.igdb.com/igdb/image/upload/t_cover_big/co1x7j.jpg")
    private String urlCapa;

    @PastOrPresent(message = "A data de lançamento não pode ser no futuro")
    @Schema(description = "Data de lançamento oficial do jogo", example = "2015-12-01")
    private LocalDate dataLancamento;

    @ManyToOne
    @JoinColumn(name = "estudio_id", nullable = false)
    private Estudio estudio;

    @ManyToMany(mappedBy = "jogos")
    @JsonIgnore
    private List<ListasDeJogos> listas;
}