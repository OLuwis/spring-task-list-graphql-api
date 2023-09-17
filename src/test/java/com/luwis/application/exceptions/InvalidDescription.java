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
import com.luwis.application.todo.TodoModel;
import com.luwis.application.todo.TodoRepository;
import com.luwis.application.todo.exceptions.InvalidDescriptionException;

@AutoConfigureHttpGraphQlTester
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InvalidDescription {

    @Value("${JWT_SECRET}")
    private String secret;
    
    @Autowired
    private TodoRepository todoRepository;
    
    @Autowired
    private HttpGraphQlTester tester;

    private InvalidDescriptionException exception = new InvalidDescriptionException();

    @Test
    void shouldReturnInvalidDescriptionForCreateTodo() {
        String title = "Title";
        String description = "";

        String token = JWT.create()
        .withClaim("id", 1)
        .withClaim("username", "Luis")
        .withIssuer("Luwis")
        .sign(Algorithm.HMAC256(secret));

        tester.mutate()
        .header("Authorization", "Bearer " + token)
        .build()
        .documentName("createTodo")
        .variable("title", title)
        .variable("description", description)
        .execute()
        .path("$['errors'][0]", path -> {
            String errorMessage = path.path("['message']").entity(String.class).get();
            String errorType = path.path("['extensions']['classification']").entity(String.class).get();

            assertAll(
                "shouldReturnInvalidDescription",
                () -> assertEquals(errorMessage, exception.getMessage()),
                () -> assertEquals(errorType, exception.getType())
            );
        });
    }

    @Test
    void shouldReturnInvalidDescriptionForUpdateTodo() {
        String title = "Title";
        String description = "";

        String token = JWT.create()
        .withClaim("id", 1)
        .withClaim("username", "Luis")
        .withIssuer("Luwis")
        .sign(Algorithm.HMAC256(secret));

        TodoModel newTodo = new TodoModel(title, description, 1);
        TodoModel todo = todoRepository.save(newTodo);
        
        tester.mutate()
        .header("Authorization", "Bearer " + token)
        .build()
        .documentName("updateTodo")
        .variable("id", todo.getId())
        .variable("description", description)
        .execute()
        .path("$['errors'][0]", path -> {
            String errorMessage = path.path("['message']").entity(String.class).get();
            String errorType = path.path("['extensions']['classification']").entity(String.class).get();

            assertAll(
                "shouldReturnInvalidDescription",
                () -> assertEquals(errorMessage, exception.getMessage()),
                () -> assertEquals(errorType, exception.getType())
            );
        });
    }

}