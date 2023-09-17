package com.luwis.application.exceptions;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.HttpGraphQlTester;

import com.luwis.application.todo.exceptions.UnauthorizedException;

@AutoConfigureHttpGraphQlTester
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class Unauthorized {
    
    @Autowired
    private HttpGraphQlTester tester;

    private UnauthorizedException exception = new UnauthorizedException();

    @Test
    void shouldReturnUnauthorizedForCreateTodo() {
        String title = "Title";
        String description = "Description";

        tester.documentName("createTodo")
        .variable("title", title)
        .variable("description", description)
        .execute()
        .path("$['errors'][0]", path -> {
            String errorMessage = path.path("['message']").entity(String.class).get();
            String errorType = path.path("['extensions']['classification']").entity(String.class).get();

            assertAll(
                "shouldReturnUnauthorized",
                () -> assertEquals(errorMessage, exception.getMessage()),
                () -> assertEquals(errorType, exception.getType())
            );
        });
    }

    @Test
    void shouldReturnUnauthorizedForDeleteTodo() {
        tester.documentName("deleteTodo")
        .variable("id", 1)
        .execute()
        .path("$['errors'][0]", path -> {
            String errorMessage = path.path("['message']").entity(String.class).get();
            String errorType = path.path("['extensions']['classification']").entity(String.class).get();

            assertAll(
                "shouldReturnUnauthorized",
                () -> assertEquals(errorMessage, exception.getMessage()),
                () -> assertEquals(errorType, exception.getType())
            );
        });
    }

    @Test
    void shouldReturnUnauthorizedForUpdateTodo() {
        tester.documentName("updateTodo")
        .variable("id", 1)
        .variable("status", true)
        .execute()
        .path("$['errors'][0]", path -> {
            String errorMessage = path.path("['message']").entity(String.class).get();
            String errorType = path.path("['extensions']['classification']").entity(String.class).get();

            assertAll(
                "shouldReturnUnauthorized",
                () -> assertEquals(errorMessage, exception.getMessage()),
                () -> assertEquals(errorType, exception.getType())
            );
        });
    }

    @Test
    void shouldReturnUnauthorizedForUpdateStatus() {
        tester.documentName("updateStatus")
        .variable("id", 1)
        .execute()
        .path("$['errors'][0]", path -> {
            String errorMessage = path.path("['message']").entity(String.class).get();
            String errorType = path.path("['extensions']['classification']").entity(String.class).get();

            assertAll(
                "shouldReturnUnauthorized",
                () -> assertEquals(errorMessage, exception.getMessage()),
                () -> assertEquals(errorType, exception.getType())
            );
        });
    }

    @Test
    void shouldReturnUnauthorizedForGetTodos() {
        tester.documentName("getTodos")
        .execute()
        .path("$['errors'][0]", path -> {
            String errorMessage = path.path("['message']").entity(String.class).get();
            String errorType = path.path("['extensions']['classification']").entity(String.class).get();

            assertAll(
                "shouldReturnUnauthorized",
                () -> assertEquals(errorMessage, exception.getMessage()),
                () -> assertEquals(errorType, exception.getType())
            );
        });
    }

}