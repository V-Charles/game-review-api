package com.gamereview.gamereview_api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "tb_perfis_usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerfilDoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID auto-gerado pelo banco de dados", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Past(message = "A data de nascimento deve estar no passado")
    @Schema(description = "Data de nascimento do usuário", example = "1998-01-31")
    private LocalDate dataNasc;

    @Size(max = 255, message = "A bio deve ter no máximo 255 caracteres")
    @Schema(description = "Biografia curta do perfil", example = "Muito paciente com estratégias e muito rápido para jogos dinâmicos, o que infelizmente me faz gostar de Rainbow Six")
    private String bio;

    @OneToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false)
    private Usuario usuario;
}