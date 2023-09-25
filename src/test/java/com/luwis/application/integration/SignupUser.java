package com.luwis.application.integration;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.graphql.test.tester.GraphQlTester;

import com.luwis.application.graphql.interfaces.User;

@AutoConfigureGraphQlTester
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class SignupUser {

    @Autowired
    private GraphQlTester tester;
    
    @Test
    @Order(1)
    void shouldReturnNewUser() {
        String name = "MyName";
        String email = "myemail@gmail.com";
        String password = "12345Ab!";
   
        tester.documentName("SignupUser")
            .variable("name", name)
            .variable("email", email)
            .variable("password", password)
            .execute()
            .path("$['data']['SignupUser']['user']")
            .entity(User.class)
            .satisfies(user -> {
                user.name().equals(name);
                user.email().equals(email);
            });
    }

    @Test
    @Order(2)
    void shouldThrowEmailIsRegistered() {
        String name = "MyName";
        String email = "myemail@gmail.com";
        String password = "12345Ab!";
        
        tester.documentName("SignupUser")
            .variable("name", name)
            .variable("email", email)
            .variable("password", password)
            .execute()
            .errors()
            .expect(error -> error.getMessage().equals("Error: Email Is Registered"))
            .expect(error -> error.getErrorType().equals(ErrorType.BAD_REQUEST))
            .expect(error -> error.getPath().equals("SignupUser"));
    }

    @Test
    @Order(3)
    void shouldThrowInvalidEmail() {
        String name = "MyName";
        String email = "invalid@email";
        String password = "12345Ab!";
    
        tester.documentName("SignupUser")
            .variable("name", name)
            .variable("email", email)
            .variable("password", password)
            .execute()
            .errors()
            .expect(error -> error.getMessage().equals("Error: Invalid Email"))
            .expect(error -> error.getErrorType().equals(ErrorType.BAD_REQUEST))
            .expect(error -> error.getPath().equals("SignupUser"));
    }

    @Test
    @Order(4)
    void shouldThrowInvalidPassword() {
        String name = "MyName";
        String email = "myemail@gmail.com";
        String password = "Invalid";

        tester.documentName("SignupUser")
            .variable("name", name)
            .variable("email", email)
            .variable("password", password)
            .execute()
            .errors()
            .expect(error -> error.getMessage().equals("Error: Invalid Password"))
            .expect(error -> error.getErrorType().equals(ErrorType.BAD_REQUEST))
            .expect(error -> error.getPath().equals("SignupUser"));
    }

}