package com.luwis.application.integration;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.luwis.application.graphql.inputs.LoginInput;
import com.luwis.application.graphql.types.Todo;
import com.luwis.application.models.TodoModel;
import com.luwis.application.models.UserModel;
import com.luwis.application.repositories.TodoRepository;
import com.luwis.application.repositories.UserRepository;
import com.luwis.application.services.AuthService;

@AutoConfigureHttpGraphQlTester
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class DeleteTodo {
    
    @Autowired
    private HttpGraphQlTester tester;

    @Autowired
    private AuthService authService;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;
    
    @Test
    @Order(1)
    void shouldReturnDeletedTodo() {
        String name = "MyName";
        String email = "myemail@gmail.com";
        String password = "12345Ab!";
        
        String title = "API Testing";
        String description = "Just Testing The API";

        var newUser = new UserModel(name, email, encoder.encode(password));
        var user = userRepository.save(newUser);

        var loginInput = new LoginInput(email, password);
        var token = authService.login(loginInput).token();

        var newTodo = new TodoModel(title, description, false, user);
        var todo = todoRepository.save(newTodo);

        var response = new Todo(todo.getId(), todo.getTitle(), todo.getDescription(), todo.getStatus());

        tester.mutate()
            .header("Authorization", "Bearer " + token)
            .build()
            .documentName("DeleteTodo")
            .variable("id", todo.getId())
            .execute()
            .path("$['data']['DeleteTodo']['todo']")
            .entity(Todo.class)
            .get()
            .equals(response);
    }

    @Test
    @Order(2)
    void shouldThrowAccessDenied() {
        tester.documentName("DeleteTodo")
            .variable("id", 1)
            .execute()
            .errors()
            .expect(error -> error.getMessage().equals("Error: Access Denied"))
            .expect(error -> error.getPath().equals("DeleteTodo"))
            .expect(error -> error.getErrorType().equals(ErrorType.UNAUTHORIZED));
    }

    @Test
    @Order(3)
    void shouldThrowResourceNotFound() {
        String name = "MyName";
        String email = "myother@gmail.com";
        String password = "12345Ab!";
        
        var newUser = new UserModel(name, email, encoder.encode(password));
        userRepository.save(newUser);

        var loginInput = new LoginInput(email, password);
        var token = authService.login(loginInput).token();

        tester.mutate()
            .header("Authorization", "Bearer " + token)
            .build()
            .documentName("DeleteTodo")
            .variable("id", 1)
            .execute()
            .errors()
            .expect(error -> error.getMessage().equals("Error: Resource Not Found"))
            .expect(error -> error.getPath().equals("DeleteTodo"))
            .expect(error -> error.getErrorType().equals(ErrorType.NOT_FOUND));
    }

}