package com.gamereview.gamereview_api.model;

import com.gamereview.gamereview_api.enums.StatusProgresso;
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
    private Integer id;

    @NotNull(message = "A nota é obrigatória")
    @Min(value = 0, message = "A nota mínima é 0")
    @Max(value = 10, message = "A nota máxima é 10")
    @Column(nullable = false)
    private Double nota;

    private String comentario;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataReview;

    @NotNull(message = "O status de progresso é obrigatório")
    @Enumerated(EnumType.STRING)
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
