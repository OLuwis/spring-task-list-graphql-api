package com.luwis.application.unit;

import org.mockito.Mockito;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.security.access.AccessDeniedException;

import com.luwis.application.controllers.TodoController;
import com.luwis.application.graphql.inputs.DeleteTodoInput;
import com.luwis.application.graphql.responses.DeleteTodoRes;
import com.luwis.application.graphql.types.Todo;
import com.luwis.application.services.TodoService;

@GraphQlTest(TodoController.class)
@TestMethodOrder(OrderAnnotation.class)
public class DeleteTodo {

    @Autowired
    private GraphQlTester tester;

    @MockBean
    private TodoService todoService;
    
    @Test
    @Order(1)
    void shouldReturnDeletedTodo() {
        long id = 1;
        String title = "API Testing";
        String description = "Just Testing The API";
        Boolean status = false;

        var todo = new Todo(id, title, description, status);
        var input = new DeleteTodoInput(id);
        var response = new DeleteTodoRes(todo);

        Mockito.when(todoService.delete(input)).thenReturn(response);

        tester.documentName("DeleteTodo")
            .variable("id", id)
            .execute()
            .path("$['data']['DeleteTodo']['todo']")
            .entity(Todo.class)
            .get()
            .equals(todo);
    }

    @Test
    @Order(2)
    void shouldThrowAccessDenied() {
        long id = 1;

        var input = new DeleteTodoInput(id);
        var exception = new AccessDeniedException(null);
        
        Mockito.when(todoService.delete(input)).thenThrow(exception);
        
        tester.documentName("DeleteTodo")
            .variable("id", id)
            .execute()
            .errors()
            .expect(error -> error.getMessage().equals("Error: Access Denied"))
            .expect(error -> error.getPath().equals("DeleteTodo"))
            .expect(error -> error.getErrorType().equals(ErrorType.UNAUTHORIZED));
    }
    
    @Test
    @Order(3)
    void shouldThrowResourceNotFound() {
        long id = 1;

        var input = new DeleteTodoInput(id);
        var exception = new NoSuchElementException();
        
        Mockito.when(todoService.delete(input)).thenThrow(exception);
        
        tester.documentName("DeleteTodo")
            .variable("id", id)
            .execute()
            .errors()
            .expect(error -> error.getMessage().equals("Error: Resource Not Found"))
            .expect(error -> error.getPath().equals("DeleteTodo"))
            .expect(error -> error.getErrorType().equals(ErrorType.NOT_FOUND));
    }

}