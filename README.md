### Sobre o projeto:

Um projeto que fiz de uma API GraphQL com Java, Maven, Spring, Spring GraphQL, Spring Data JPA, Spring JDBC, Lombok, H2 Database, Spring Security, Passay e Apache Commons.

Esta API possui consultas e mutações para cadastro, login e CRUD, tokens JWT para autenticação, criptografia de senhas, também possui testes de integração feitas com o Spring GraphQL Test.

Você pode ler sobre o projeto [aqui](https://medium.com/@luwis/list/criando-uma-api-graphql-com-java-spring-boot-16ce75e1b7ad).

### Feito com:

![Java Badge](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Badge](https://img.shields.io/badge/Spring-6DB33F?logo=spring&logoColor=fff&style=for-the-badge)
![Spring Boot Badge](https://img.shields.io/badge/Spring%20Boot-6DB33F?logo=springboot&logoColor=fff&style=for-the-badge)
![Spring Security Badge](https://img.shields.io/badge/Spring%20Security-6DB33F?logo=springsecurity&logoColor=fff&style=for-the-badge)
![GraphQL Badge](https://img.shields.io/badge/GraphQL-E10098?logo=graphql&logoColor=fff&style=for-the-badge)
![Apache Maven Badge](https://img.shields.io/badge/Apache%20Maven-C71A36?logo=apachemaven&logoColor=fff&style=for-the-badge)
![Apache Tomcat Badge](https://img.shields.io/badge/Apache%20Tomcat-F8DC75?logo=apachetomcat&logoColor=000&style=for-the-badge)
![.ENV Badge](https://img.shields.io/badge/.ENV-ECD53F?logo=dotenv&logoColor=000&style=for-the-badge)

### Requisitos:

* [Java 17+](https://java.com/pt-BR/)
* [Maven](https://maven.apache.org/)

### Instalação:

1. Clonar este repositório:
   ```sh
   git clone https://github.com/OLuwis/TaskListGraphQLAPI.git
   ```

2. Criar um arquivo .env, e definir as variáveis de ambiente (exemplo com uma chave AES256 aleatória):
   ```js
   JWT_KEY=e9F0rkYIwXP2ld1Dn6DxEdtlmsaA2JiL
   ```

### Inicialização:

Para iniciar o projeto, rode o seguinte comando:

   ```sh
   mvn spring-boot:run
   ```
