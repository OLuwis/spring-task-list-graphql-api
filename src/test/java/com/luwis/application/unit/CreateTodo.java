package com.luwis.application.unit;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.security.access.AccessDeniedException;

import com.luwis.application.controllers.TodoController;
import com.luwis.application.graphql.inputs.CreateTodoInput;
import com.luwis.application.graphql.responses.CreateTodoRes;
import com.luwis.application.graphql.types.Todo;
import com.luwis.application.services.TodoService;

@GraphQlTest(TodoController.class)
@TestMethodOrder(OrderAnnotation.class)
public class CreateTodo {
    
    @Autowired
    private GraphQlTester tester;

    @MockBean
    private TodoService todoService;

    @Test
    @Order(1)
    void shouldReturnNewTodo() {
        String title = "API Testing";
        String description = "Just Testing The API";
        
        var input = new CreateTodoInput(title, description);
        var todo = new Todo((long) 1, title, description, false);
        var response = new CreateTodoRes(todo);

        Mockito.doReturn(response).when(todoService).create(input);
        
        tester.documentName("CreateTodo")
            .variable("title", title)
            .variable("description", description)
            .execute()
            .path("$['data']['CreateTodo']['todo']")
            .entity(Todo.class)
            .get()
            .equals(todo);
    }

    @Test
    @Order(2)
    void shouldThrowAccessDenied() {
        String title = "API Testing";
        String description = "Just Testing The API";
        
        var input = new CreateTodoInput(title, description);
        var exception = new AccessDeniedException(null);

        Mockito.doThrow(exception).when(todoService).create(input);

        tester.documentName("CreateTodo")
            .variable("title", title)
            .variable("description", description)
            .execute()
            .errors()
            .expect(error -> error.getMessage().equals("Error: Access Denied"))
            .expect(error -> error.getErrorType().equals(ErrorType.UNAUTHORIZED))
            .expect(error -> error.getPath().equals("CreateTodo"));
    }

}