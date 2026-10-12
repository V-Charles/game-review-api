package com.gamereview.gamereview_api.model;

import com.gamereview.gamereview_api.enums.StatusProgresso;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_avaliacoes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID auto-gerado pelo banco de dados", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @NotNull(message = "A nota é obrigatória")
    @Min(value = 0, message = "A nota mínima é 0")
    @Max(value = 10, message = "A nota máxima é 10")
    @Schema(description = "Nota atribuída ao jogo (de 0 a 10)", example = "9.5")
    @Column(nullable = false)
    private Double nota;

    @Schema(description = "Comentário ou análise do jogo", example = "É legal, mas não sei se devo recomendar pra alguém. A chance de você ficar com muita raiva é enorme")
    private String comentario;

    @Column(nullable = false, updatable = false)
    @Schema(description = "Data de registro automático da avaliação", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime dataReview;

    @NotNull(message = "O status de progresso é obrigatório")
    @Enumerated(EnumType.STRING)
    @Schema(description = "Status de progressão do usuário no jogo", example = "PLATINADO")
    @Column(nullable = false)
    private StatusProgresso status;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "jogo_id", nullable = false)
    private Jogo jogo;

    @PrePersist
    protected void onCreate(){
        this.dataReview = LocalDateTime.now();
    }
}