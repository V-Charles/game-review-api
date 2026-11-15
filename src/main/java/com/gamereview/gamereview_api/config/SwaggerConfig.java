package com.gamereview.gamereview_api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "ApiKeyAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("Game Review API")
                        .version("2.0.0")
                        .description("""
                                API RESTful desenvolvida com Spring Boot para catalogação de jogos, estúdios, perfis, listas personalizadas e avaliações.
                                
                                ### Regras de Uso e Segurança:
                                - **Autenticação (`X-API-Key`):** Crie um usuário em `POST /usuarios` e gere sua chave em `POST /api-keys/usuario/{usuarioId}`. Em seguida, clique no botão **Authorize** acima e informe a chave gerada para liberar os demais endpoints.
                                - **Idempotência (`X-Idempotency-Key`):** Obrigatório em todas as requisições `POST`. Envie um identificador único (ex: UUID) por operação para evitar cadastros duplicados.
                                - **Rate Limiting:** Limite de 5 requisições por minuto por endereço IP. Ao exceder, a API retorna status `429` com o cabeçalho `Retry-After`.
                                - **Versionamento (`X-API-Version`):** Suporte a múltiplas versões de contrato via cabeçalho customizado (ex: `GET /usuarios/{id}` aceita valores `1` ou `2`).
                                """)
                        .contact(new Contact()
                                .name("Vinicius C. M. Dias")
                                .email("viniciuscharles3101@gmail.com")
                                .url("https://github.com/V-Charles/game-review-api")
                        )
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")
                        )
                )
                .servers(List.of(
                        new Server()
                                .url("/")
                                .description("Servidor Atual (Ambiente Local ou Nuvem Render)")
                ))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name("X-API-Key")
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .description("Chave de API gerada pelo endpoint /api-keys/usuario/{usuarioId}")
                        )
                );
    }

    @Bean
    public OpenApiCustomizer globalHeaderOpenApiCustomizer() {
        return openApi -> {
            if (openApi.getPaths() != null) {
                openApi.getPaths().values().forEach(pathItem -> {
                    Operation postOperation = pathItem.getPost();
                    if (postOperation != null) {
                        boolean alreadyHasHeader = postOperation.getParameters() != null &&
                                postOperation.getParameters().stream()
                                        .anyMatch(p -> "X-Idempotency-Key".equalsIgnoreCase(p.getName()));
                        if (!alreadyHasHeader) {
                            Parameter idempotencyHeader = new Parameter()
                                    .in("header")
                                    .name("X-Idempotency-Key")
                                    .description("Chave única (ex: UUID) para garantir a idempotência da requisição POST e evitar duplicidade.")
                                    .required(true)
                                    .schema(new Schema<String>().type("string").example("550e8400-e29b-41d4-a716-446655440000"));
                            postOperation.addParametersItem(idempotencyHeader);
                        }
                    }
                });
            }
        };
    }
}