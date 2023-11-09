### Introdução

Uma API GraphQL feita com Java 17 e Spring Boot.

A API usa um sistema de autenticação JWT feita com o Spring Security, também possui uma base de dados em memória com o H2 Database.

### Requisitos

* [Maven](https://maven.apache.org/install.html)
* [Java](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) 17+

### Instruções

1. __Clone este repositório__
    ```
    git clone https://github.com/OLuwis/TaskListGraphQLAPI.git
    ```
2. __Crie um arquivo .env e defina as variáveis de ambiente (exemplo com chave AES256 gerada:)__
    ```
    JWT_KEY=e9F0rkYIwXP2ld1Dn6DxEdtlmsaA2JiL
    ```
3. __Inicie a aplicação__
    ```
    mvn spring-boot:run
    ```
4. __Cheque se a aplicação está rodando (exemplo:)__
    ```
    http://localhost:8080/graphiql
    ```
