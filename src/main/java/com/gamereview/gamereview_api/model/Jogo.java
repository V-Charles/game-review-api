package com.gamereview.gamereview_api.model;

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
    private Integer id;

    private Long igdbId;

    @NotBlank(message = "O título do jogo é obrigatório")
    @Column(nullable = false)
    private String titulo;

    private String urlCapa;

    @PastOrPresent(message = "A data de lançamento não pode ser no futuro")
    private LocalDate dataLancamento;

    @ManyToOne
    @JoinColumn(name = "estudio_id", nullable = false)
    private Estudio estudio;

    @ManyToMany(mappedBy = "jogos")
    private List<ListasDeJogos> listas;
}
