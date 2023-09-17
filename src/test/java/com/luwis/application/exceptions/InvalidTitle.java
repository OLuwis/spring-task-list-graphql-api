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
import com.luwis.application.todo.exceptions.InvalidTitleException;

@AutoConfigureHttpGraphQlTester
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InvalidTitle {

    @Value("${JWT_SECRET}")
    private String secret;
    
    @Autowired
    private TodoRepository todoRepository;
    
    @Autowired
    private HttpGraphQlTester tester;

    private InvalidTitleException exception = new InvalidTitleException();

    @Test
    void shouldReturnInvalidDescriptionForCreateTodo() {
        String title = "";
        String description = "Description";

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
                "shouldReturnInvalidTitle",
                () -> assertEquals(errorMessage, exception.getMessage()),
                () -> assertEquals(errorType, exception.getType())
            );
        });
    }

    @Test
    void shouldReturnInvalidDescriptionForUpdateTodo() {
        String title = "";
        String description = "Description";

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
        .variable("title", title)
        .execute()
        .path("$['errors'][0]", path -> {
            String errorMessage = path.path("['message']").entity(String.class).get();
            String errorType = path.path("['extensions']['classification']").entity(String.class).get();

            assertAll(
                "shouldReturnInvalidTitle",
                () -> assertEquals(errorMessage, exception.getMessage()),
                () -> assertEquals(errorType, exception.getType())
            );
        });
    }

}