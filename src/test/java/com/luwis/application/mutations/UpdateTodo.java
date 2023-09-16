package com.luwis.application.mutations;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import com.luwis.application.todo.exceptions.InvalidTitleException;
import com.luwis.application.todo.exceptions.TodoNotFoundException;

@AutoConfigureHttpGraphQlTester
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UpdateTodo {
    
    @Value("${JWT_SECRET}")
    private String secret;
    
    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private HttpGraphQlTester tester;

    @Test
    void shouldReturnUpdatedTodo() {
        long id = 1;

        String token = JWT.create()
        .withClaim("id", id)
        .withClaim("username", "Luis")
        .withIssuer("Luwis")
        .sign(Algorithm.HMAC256(secret));

        TodoModel newTodo = new TodoModel("Title", "Desc", id);
        TodoModel todo = todoRepository.save(newTodo);

        tester.mutate()
        .header("Authorization", "Bearer " + token)
        .build()
        .documentName("updateTodo")
        .variable("id", todo.getId())
        .variable("title", "newTitle")
        .variable("description", "newDesc")
        .variable("status", true)
        .execute()
        .path("$['data']['updateTodo']", path -> {
            String title = path.path("['title']").entity(String.class).get();
            String description = path.path("['description']").entity(String.class).get();
            boolean status = path.path("['status']").entity(Boolean.class).get();

            assertAll(
                "shouldReturnUpdatedTodo",
                () -> assertNotEquals("Title", title),
                () -> assertNotEquals("Desc", description),
                () -> assertTrue(status)
            );
        });
    }

    @Test
    void shouldReturnUpdatedTodoTitle() {
        long id = 1;

        String token = JWT.create()
        .withClaim("id", id)
        .withClaim("username", "Luis")
        .withIssuer("Luwis")
        .sign(Algorithm.HMAC256(secret));

        TodoModel newTodo = new TodoModel("Title", "Desc", id);
        TodoModel todo = todoRepository.save(newTodo);

        tester.mutate()
        .header("Authorization", "Bearer " + token)
        .build()
        .documentName("updateTodo")
        .variable("id", todo.getId())
        .variable("title", "newTitle")
        .execute()
        .path("$['data']['updateTodo']", path -> {
            String title = path.path("['title']").entity(String.class).get();
            String description = path.path("['description']").entity(String.class).get();
            boolean status = path.path("['status']").entity(Boolean.class).get();

            assertAll(
                "shouldReturnUpdatedTodo",
                () -> assertNotEquals("Title", title),
                () -> assertEquals("Desc", description),
                () -> assertFalse(status)
            );
        });
    }

    @Test
    void shouldReturnUpdatedTodoDesc() {
        long id = 1;

        String token = JWT.create()
        .withClaim("id", id)
        .withClaim("username", "Luis")
        .withIssuer("Luwis")
        .sign(Algorithm.HMAC256(secret));

        TodoModel newTodo = new TodoModel("Title", "Desc", id);
        TodoModel todo = todoRepository.save(newTodo);

        tester.mutate()
        .header("Authorization", "Bearer " + token)
        .build()
        .documentName("updateTodo")
        .variable("id", todo.getId())
        .variable("description", "newDesc")
        .execute()
        .path("$['data']['updateTodo']", path -> {
            String title = path.path("['title']").entity(String.class).get();
            String description = path.path("['description']").entity(String.class).get();
            boolean status = path.path("['status']").entity(Boolean.class).get();

            assertAll(
                "shouldReturnUpdatedTodo",
                () -> assertEquals("Title", title),
                () -> assertNotEquals("Desc", description),
                () -> assertFalse(status)
            );
        });
    }
    
    @Test
    void shouldReturnUpdatedTodoStatus() {
        long id = 1;

        String token = JWT.create()
        .withClaim("id", id)
        .withClaim("username", "Luis")
        .withIssuer("Luwis")
        .sign(Algorithm.HMAC256(secret));

        TodoModel newTodo = new TodoModel("Title", "Desc", id);
        TodoModel todo = todoRepository.save(newTodo);

        tester.mutate()
        .header("Authorization", "Bearer " + token)
        .build()
        .documentName("updateTodo")
        .variable("id", todo.getId())
        .variable("status", true)
        .execute()
        .path("$['data']['updateTodo']", path -> {
            String title = path.path("['title']").entity(String.class).get();
            String description = path.path("['description']").entity(String.class).get();
            boolean status = path.path("['status']").entity(Boolean.class).get();

            assertAll(
                "shouldReturnUpdatedTodo",
                () -> assertEquals("Title", title),
                () -> assertEquals("Desc", description),
                () -> assertTrue(status)
            );
        });
    }

    @Test
    void shouldReturnTodoNotFound() {
        long id = 1;

        String token = JWT.create()
        .withClaim("id", id)
        .withClaim("username", "Luis")
        .withIssuer("Luwis")
        .sign(Algorithm.HMAC256(secret));

        tester.mutate()
        .header("Authorization", "Bearer " + token)
        .build()
        .documentName("updateTodo")
        .variable("id", 1)
        .variable("title", "newTitle")
        .variable("description", "newDesc")
        .variable("status", true)
        .execute()
        .path("$['errors'][0]", path -> {
            TodoNotFoundException exception = new TodoNotFoundException();

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
    void shouldReturnInvalidTitle() {
        long id = 1;

        String token = JWT.create()
        .withClaim("id", id)
        .withClaim("username", "Luis")
        .withIssuer("Luwis")
        .sign(Algorithm.HMAC256(secret));

        TodoModel newTodo = new TodoModel("Title", "Desc", id);
        TodoModel todo = todoRepository.save(newTodo);

        tester.mutate()
        .header("Authorization", "Bearer " + token)
        .build()
        .documentName("updateTodo")
        .variable("id", todo.getId())
        .variable("title", "")
        .execute()
        .path("$['errors'][0]", path -> {
            InvalidTitleException exception = new InvalidTitleException();

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
    void shoudlReturnInvalidDesc() {
        long id = 1;

        String token = JWT.create()
        .withClaim("id", id)
        .withClaim("username", "Luis")
        .withIssuer("Luwis")
        .sign(Algorithm.HMAC256(secret));

        TodoModel newTodo = new TodoModel("Title", "Desc", id);
        TodoModel todo = todoRepository.save(newTodo);

        tester.mutate()
        .header("Authorization", "Bearer " + token)
        .build()
        .documentName("updateTodo")
        .variable("id", todo.getId())
        .variable("description", "")
        .execute()
        .path("$['errors'][0]", path -> {
            InvalidDescriptionException exception = new InvalidDescriptionException();

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