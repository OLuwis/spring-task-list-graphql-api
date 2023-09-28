package com.luwis.application.unit;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.security.access.AccessDeniedException;

import com.luwis.application.controllers.TodoController;
import com.luwis.application.graphql.inputs.UpdateTodoInput;
import com.luwis.application.graphql.responses.UpdateTodoRes;
import com.luwis.application.graphql.types.Todo;
import com.luwis.application.services.TodoService;

@GraphQlTest(TodoController.class)
@TestMethodOrder(OrderAnnotation.class)
public class UpdateTodo {
    
    @Autowired
    private GraphQlTester tester;

    @MockBean
    private TodoService todoService;

    @Test
    @Order(1)
    void shouldReturnUpdatedTodo() {
        long id = 1;
        String oldTitle = "API Testing";
        String oldDescription = "Just Testing The API";
        Boolean oldStatus = false;

        Optional<String> newTitle = Optional.of("Updating");
        Optional<String> newDescription = Optional.of("It is what it is");
        Optional<Boolean> newStatus = Optional.of(true);

        var oldTodo = new Todo((long) 1, oldTitle, oldDescription, oldStatus);
        var newTodo = new Todo((long) 1, newTitle.get(), newDescription.get(), newStatus.get());
        var input = new UpdateTodoInput(id, newTitle, newDescription, newStatus);
        var response = new UpdateTodoRes(oldTodo, newTodo);

        Mockito.when(todoService.update(input)).thenReturn(response);

        tester.documentName("UpdateTodo")
            .variable("id", id)
            .variable("title", newTitle.get())
            .variable("description", newDescription.get())
            .variable("status", newStatus.get())
            .execute()
            .path("$['data']['UpdateTodo']", path -> {
                path.path("['before']").entity(Todo.class).get().equals(oldTodo);

                path.path("['after']").entity(Todo.class).get().equals(newTodo);
            });
    }

    @Test
    @Order(2)
    void shouldThrowAccessDenied() {
        long id = 1;
        
        Optional<String> newTitle = Optional.of("Updating");
        Optional<String> newDescription = Optional.of("It is what it is");
        Optional<Boolean> newStatus = Optional.of(true);

        var input = new UpdateTodoInput(id, newTitle, newDescription, newStatus);
        var exception = new AccessDeniedException(null);

        Mockito.when(todoService.update(input)).thenThrow(exception);

        tester.documentName("UpdateTodo")
            .variable("id", id)
            .variable("title", newTitle.get())
            .variable("description", newDescription.get())
            .variable("status", newStatus.get())
            .execute()
            .errors()
            .expect(error -> error.getMessage().equals("Error: Access Denied"))
            .expect(error -> error.getPath().equals("UpdateTodo"))
            .expect(error -> error.getErrorType().equals(ErrorType.UNAUTHORIZED));
    }

    @Test
    @Order(3)
    void shouldThrowResourceNotFound() {
        long id = 1;
        
        Optional<String> newTitle = Optional.of("Updating");
        Optional<String> newDescription = Optional.of("It is what it is");
        Optional<Boolean> newStatus = Optional.of(true);

        var input = new UpdateTodoInput(id, newTitle, newDescription, newStatus);
        var exception = new NoSuchElementException();

        Mockito.when(todoService.update(input)).thenThrow(exception);

        tester.documentName("UpdateTodo")
            .variable("id", id)
            .variable("title", newTitle.get())
            .variable("description", newDescription.get())
            .variable("status", newStatus.get())
            .execute()
            .errors()
            .expect(error -> error.getMessage().equals("Error: Resource Not Found"))
            .expect(error -> error.getPath().equals("UpdateTodo"))
            .expect(error -> error.getErrorType().equals(ErrorType.NOT_FOUND));
    }
    
}