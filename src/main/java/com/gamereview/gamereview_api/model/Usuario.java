package com.gamereview.gamereview_api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID auto-gerado pelo banco de dados", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @NotBlank(message = "O nome de usuário é obrigatório")
    @Size(min = 3, max = 50, message = "O nome de usuário deve ter entre 3 e 50 caracteres")
    @Schema(description = "Nome de usuário exclusivo", example = "zReaperz")
    @Column(nullable = false, unique = true)
    private String userName;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    @Schema(description = "Endereço de e-mail do usuário", example = "reaper.r6siege@email.com")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    @Schema(description = "Senha de acesso (mínimo 6 caracteres)", example = "mainbuck123")
    @Column(nullable = false)
    private String senha;

    @Column(nullable = false, updatable = false)
    @Schema(description = "Data de registro automático do usuário", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime dataCadastro;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    @Schema(description = "Perfil estendido do usuário", accessMode = Schema.AccessMode.READ_ONLY)
    private PerfilDoUsuario perfil;

    @PrePersist
    protected void onCreate(){
        this.dataCadastro = LocalDateTime.now();
    }
}