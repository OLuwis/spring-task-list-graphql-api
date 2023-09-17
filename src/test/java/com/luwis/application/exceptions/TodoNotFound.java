package com.luwis.application.exceptions;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.HttpGraphQlTester;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.luwis.application.todo.exceptions.TodoNotFoundException;

@AutoConfigureHttpGraphQlTester
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TodoNotFound {

    @Value("${JWT_SECRET}")
    private String secret;
    
    @Autowired
    private HttpGraphQlTester tester;

    private TodoNotFoundException exception = new TodoNotFoundException();

    @Test
    void shouldReturnTodoNotFoundForDeleteTodo() {
        String token = JWT.create()
        .withClaim("id", 1)
        .withClaim("username", "Luis")
        .withIssuer("Luwis")
        .sign(Algorithm.HMAC256(secret));

        tester.mutate()
        .header("Authorization", "Bearer " + token)
        .build()
        .documentName("deleteTodo")
        .variable("id", 1)
        .execute()
        .path("$['errors'][0]", path -> {
            String errorMessage = path.path("['message']").entity(String.class).get();
            String errorType = path.path("['extensions']['classification']").entity(String.class).get();

            assertAll(
                "shouldReturnTodoNotFound",
                () -> assertEquals(errorMessage, exception.getMessage()),
                () -> assertEquals(errorType, exception.getType())
            );
        });
    }

    @Test
    void shouldReturnTodoNotFoundForUpdateTodo() {
        String token = JWT.create()
        .withClaim("id", 1)
        .withClaim("username", "Luis")
        .withIssuer("Luwis")
        .sign(Algorithm.HMAC256(secret));

        tester.mutate()
        .header("Authorization", "Bearer " + token)
        .build()
        .documentName("updateTodo")
        .variable("id", 1)
        .variable("status", true)
        .execute()
        .path("$['errors'][0]", path -> {
            String errorMessage = path.path("['message']").entity(String.class).get();
            String errorType = path.path("['extensions']['classification']").entity(String.class).get();

            assertAll(
                "shouldReturnTodoNotFound",
                () -> assertEquals(errorMessage, exception.getMessage()),
                () -> assertEquals(errorType, exception.getType())
            );
        });
    }

    @Test
    void shouldReturnTodoNotFoundForUpdateStatus() {
        String token = JWT.create()
        .withClaim("id", 1)
        .withClaim("username", "Luis")
        .withIssuer("Luwis")
        .sign(Algorithm.HMAC256(secret));

        tester.mutate()
        .header("Authorization", "Bearer " + token)
        .build()
        .documentName("updateStatus")
        .variable("id", 1)
        .execute()
        .path("$['errors'][0]", path -> {
            String errorMessage = path.path("['message']").entity(String.class).get();
            String errorType = path.path("['extensions']['classification']").entity(String.class).get();

            assertAll(
                "shouldReturnTodoNotFound",
                () -> assertEquals(errorMessage, exception.getMessage()),
                () -> assertEquals(errorType, exception.getType())
            );
        });
    }

}