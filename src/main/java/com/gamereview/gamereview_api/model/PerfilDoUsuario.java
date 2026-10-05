package com.gamereview.gamereview_api.model;

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
    private Integer id;

    @Past(message = "A data de nascimento deve estar no passado")
    private LocalDate dataNasc;

    @Size(max = 255, message = "A bio deve ter no máximo 255 caracteres")
    private String bio;

    @OneToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false)
    private Usuario usuario;
}
