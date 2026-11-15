# Game Review API

A Game Review API é um WebService RESTful desenvolvido para gerenciar o ecossistema de avaliação de jogos. A aplicação permite o cadastro de estúdios, jogos e usuários, além de possibilitar que a comunidade crie listas personalizadas e avaliações detalhadas.

Esta API foi construída aplicando conceitos de arquitetura em camadas (MVC), validações, HATEOAS, autenticação via API Key, controle de taxa de requisições (Rate Limiting), idempotência, versionamento por cabeçalho e tratamento global de exceções.

## Tecnologias e Requisitos

Este projeto foi construído utilizando:

- Java 21.0.2
- Spring Boot 4.1.1
- Spring Data JPA
- Spring Security
- Bucket4j (Rate Limiting)
- Springdoc OpenAPI (Swagger)
- H2 Database
- Maven

## Diagrama de Arquitetura
<img width="2496" height="2301" alt="image" src="https://github.com/user-attachments/assets/7f5a7f31-7828-482e-9d5f-8b3fd640df16" />

## Como executar localmente

Siga os passos abaixo para rodar a aplicação em sua máquina:

1. Clone o repositório: `git clone https://github.com/v-charles/game-review-api.git`
2. Acesse a pasta do projeto e instale as dependências via Maven: `mvn clean install`
3. Execute a aplicação Spring Boot: `mvn spring-boot:run`

A API estará disponível em `http://localhost:8080`.

## Documentação e Regras de Uso

A documentação interativa completa dos endpoints, com os esquemas esperados, exemplos de payload e mapeamentos de códigos HTTP, está disponível via Swagger UI:

- `http://localhost:8080/swagger-ui.html`

Para consumir os endpoints protegidos, observe os seguintes cabeçalhos e políticas:

- **Autenticação (`X-API-Key`):** Crie um usuário em `POST /usuarios` e gere uma chave em `POST /api-keys/usuario/{usuarioId}`. Envie essa chave no cabeçalho `X-API-Key` (ou utilize o botão **Authorize** no Swagger UI).
- **Idempotência (`X-Idempotency-Key`):** Obrigatório em todas as requisições `POST`, devendo conter um identificador único (como um UUID) por operação.
- **Versionamento (`X-API-Version`):** Utilizado para selecionar a versão do contrato em endpoints versionados (ex: `1` ou `2` em `GET /usuarios/{id}`).
- **Rate Limiting:** Limite de 5 requisições por minuto por endereço IP.

## Authors

- [Vinicius C. M. Dias](https://www.linkedin.com/in/vinicius-charles)
