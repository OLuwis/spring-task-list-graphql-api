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

import com.luwis.application.entities.TodoModel;
import com.luwis.application.entities.UserModel;
import com.luwis.application.graphql.inputs.LoginInput;
import com.luwis.application.graphql.types.Todo;
import com.luwis.application.repositories.TodoRepository;
import com.luwis.application.repositories.UserRepository;
import com.luwis.application.services.AuthService;

@AutoConfigureHttpGraphQlTester
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class UpdateTodo {
    
    @Autowired
    private HttpGraphQlTester tester;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private AuthService authService;
    
    @Test
    @Order(1)
    void shouldReturnUpdatedTodo() {
        String name = "MyName";
        String email = "myemail@gmail.com";
        String password = "12345Ab!";
        
        String oldTitle = "API Testing";
        String oldDescription = "Just Testing The API";
        Boolean oldStatus = false;

        String newTitle = "Updating";
        String newDescription = "It is what it is";
        Boolean newStatus = true;

        var newUser = new UserModel(name, email, encoder.encode(password));
        var user = userRepository.save(newUser);

        var loginInput = new LoginInput(email, password);
        var token = authService.login(loginInput).token();

        var newTodo = new TodoModel(oldTitle, oldDescription, oldStatus, user);
        var todo = todoRepository.save(newTodo);

        var before = new Todo(todo.getId(), todo.getTitle(), todo.getDescription(), todo.getStatus());
        var after = new Todo(todo.getId(), todo.getTitle(), todo.getDescription(), todo.getStatus());

        tester.mutate()
            .header("Authorization", "Bearer " + token)
            .build()
            .documentName("UpdateTodo")
            .variable("id", todo.getId())
            .variable("title", newTitle)
            .variable("description", newDescription)
            .variable("status", newStatus)
            .execute()
            .path("$['data']['UpdateTodo']", path -> {
                path.path("['before']").entity(Todo.class).get().equals(before);

                path.path("['after']").entity(Todo.class).get().equals(after);
            });
    }

    @Test
    @Order(2)
    void shouldThrowAccessDenied() {
        tester.documentName("UpdateTodo")
            .variable("id", 1)
            .execute()
            .errors()
            .expect(error -> error.getMessage().equals("Error: Access Denied"))
            .expect(error -> error.getPath().equals("UpdateTodo"))
            .expect(error -> error.getErrorType().equals(ErrorType.UNAUTHORIZED));
    }

    @Test
    @Order(3)
    void shouldThrowResourceNotFound() {
        String name = "MyName";
        String email = "myemail@gmail.com";
        String password = "12345Ab!";

        String newTitle = "Updating";
        String newDescription = "It is what it is";
        Boolean newStatus = true;

        var newUser = new UserModel(name, email, encoder.encode(password));
        userRepository.save(newUser);

        var loginInput = new LoginInput(email, password);
        var token = authService.login(loginInput).token();

        tester.mutate()
            .header("Authorization", "Bearer " + token)
            .build()
            .documentName("UpdateTodo")
            .variable("id", 1)
            .variable("title", newTitle)
            .variable("description", newDescription)
            .variable("status", newStatus)
            .execute()
            .errors()
            .expect(error -> error.getMessage().equals("Error: Resource Not Found"))
            .expect(error -> error.getPath().equals("UpdateTodo"))
            .expect(error -> error.getErrorType().equals(ErrorType.NOT_FOUND));
    }

}