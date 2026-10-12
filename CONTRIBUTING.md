# Como Contribuir

Obrigado por investir seu tempo em contribuir com a **Game Review API**! Toda contribuição é bem-vinda, seja ela correção de bugs, melhorias na documentação ou novas funcionalidades.

## Processo de Contribuição

1. Faça um **Fork** deste repositório.
2. Crie uma branch para a sua feature ou correção:
   `git checkout -b feature/minha-nova-feature` ou `git checkout -b fix/correcao-bug`
3. Faça as alterações necessárias e garanta que o código compila corretamente.
4. Faça o commit das suas alterações seguindo o nosso padrão de commits (veja abaixo).
5. Faça o push para a sua branch: `git push origin feature/minha-nova-feature`
6. Abra um **Pull Request** detalhando o que foi feito.

## Padrão de Commits (Conventional Commits)

Nós utilizamos o padrão [Conventional Commits](https://www.conventionalcommits.org/pt-br/v1.0.0/). Por favor, garanta que suas mensagens de commit sigam as regras abaixo:

* `feat:` Uma nova funcionalidade (ex: `feat: criacao do endpoint de busca por nome`)
* `fix:` Uma correção de bug (ex: `fix: erro de validacao no cadastro de usuario`)
* `docs:` Alterações na documentação (ex: `docs: atualizacao do readme e diagrama C4`)
* `refactor:` Uma alteração de código que não corrige um bug nem adiciona uma feature (ex: `refactor: criacao da camada de Service`)

## Ambiente de Desenvolvimento

Para rodar este projeto localmente, certifique-se de ter instalado:
* Java 21 ou superior
* Maven

Não é necessário configurar um banco de dados externo, pois o projeto utiliza o **H2 Database** em memória para facilitar os testes.
