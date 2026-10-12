package com.gamereview.gamereview_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "ID auto-gerado pelo banco de dados", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @NotBlank(message = "O nome do estúdio é obrigatório")
    @Schema(description = "Nome oficial do estúdio", example = "Ubisoft Montreal")
    @Column(nullable = false)
    private String nomeEstudio;

    @NotBlank(message = "O país de fundação é obrigatório")
    @Schema(description = "País sede do estúdio", example = "Canadá")
    private String paisFundacao;

    @OneToMany(mappedBy = "estudio", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Jogo> jogos;
}