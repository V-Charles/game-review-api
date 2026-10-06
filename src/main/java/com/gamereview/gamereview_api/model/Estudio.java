package com.gamereview.gamereview_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "tb_estudios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Estudio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "O nome do estúdio é obrigatório")
    @Column(nullable = false)
    private String nomeEstudio;

    @NotBlank(message = "O país de fundação é obrigatório")
    private String paisFundacao;

    @OneToMany(mappedBy = "estudio", cascade = CascadeType.ALL)
    private List<Jogo> jogos;
}
