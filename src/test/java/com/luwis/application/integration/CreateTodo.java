package com.luwis.application.integration;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.luwis.application.entities.UserModel;
import com.luwis.application.graphql.inputs.LoginInput;
import com.luwis.application.graphql.types.Todo;
import com.luwis.application.repositories.UserRepository;
import com.luwis.application.services.AuthService;

@AutoConfigureHttpGraphQlTester
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class CreateTodo {

    @Autowired
    private HttpGraphQlTester tester;
    
    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;
    
    @Test
    @Order(1)
    void shouldReturnNewTodo() {
        String name = "MyName";
        String email = "myemail@gmail.com";
        String password = "12345Ab!";
        
        String title = "API Testing";
        String description = "Just Testing The API";

        var newUser = new UserModel(name, email, encoder.encode(password));
        userRepository.save(newUser);

        var loginInput = new LoginInput(email, password);
        var token = authService.login(loginInput).token();

        tester.mutate()
            .header("Authorization", "Bearer " + token)
            .build()
            .documentName("CreateTodo")
            .variable("title", title)
            .variable("description", description)
            .execute()
            .path("$['data']['CreateTodo']['todo']")
            .entity(Todo.class)
            .satisfies(todo -> {
                todo.title().equals(title);
                todo.description().equals(description);
                todo.status().equals(false);
            });
    }

    @Test
    @Order(1)
    void shouldThrowAccessDenied() {
        String title = "API Testing";
        String description = "Just Testing The API";

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