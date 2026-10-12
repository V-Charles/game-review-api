
# Game Review API

A Game Review API é um WebService RESTful desenvolvido para gerenciar o ecossistema de avaliação de jogos. A aplicação permite o cadastro de estúdios, jogos, usuários, e possibilita que a comunidade crie listas de jogos personalizadas e avaliações detalhadas.

Esta API foi construída aplicando rigorosamente conceitos de arquitetura em camadas (MVC), validações, HATEOAS e Tratamento Global de Exceções.

## Tecnologias e Requisitos

Este projeto foi construído utilizando:

- Java 21.0.2
- Spring Boot 4.1.1
- Spring Data JPA
- H2 Database
- Maven

## Diagrama de Arquitetura
<img width="2496" height="2301" alt="image" src="https://github.com/user-attachments/assets/7f5a7f31-7828-482e-9d5f-8b3fd640df16" />


## Como executar localmente

Siga os passos abaixo para rodar a aplicação em sua máquina:

1. Clone o repositório: git clone https://github.com/v-charles/game-review-api.git
2. Acesse a pasta do projeto e instale as dependências via Maven: mvn clean install
3. Execute a aplicação Spring Boot: mvn spring-boot:run

A API estará disponível em http://localhost:8080.

## Documentação

A documentação interativa completa dos endpoints, com os esquemas esperados, exemplos de payload e os mapeamentos de Códigos HTTP, está disponível via Swagger UI.

Com a aplicação rodando, acesse no seu navegador:
http://localhost:8080/swagger-ui.html

## Authors

- [Vinicius C. M. Dias](https://www.linkedin.com/in/vinicius-charles)
