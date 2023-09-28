package com.luwis.application.unit;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.security.authentication.BadCredentialsException;

import com.luwis.application.controllers.AuthController;
import com.luwis.application.graphql.inputs.SignupInput;
import com.luwis.application.graphql.responses.SignupRes;
import com.luwis.application.graphql.types.User;
import com.luwis.application.services.AuthService;
import com.luwis.application.utils.InputValidator;

@GraphQlTest(AuthController.class)
@TestMethodOrder(OrderAnnotation.class)
public class Signup {

    @Autowired
    private GraphQlTester tester;

    @MockBean
    private AuthService authService;

    @MockBean
    private InputValidator inputValidator;
    
    @Test
    @Order(1)
    void shouldReturnNewUser() {
        String name = "MyName";
        String email = "myemail@gmail.com";
        String password = "12345Ab!";
        
        var input = new SignupInput(name, email, password);
        var user = new User((long) 1, name, email);
        var response = new SignupRes(user);

        Mockito.when(authService.signup(input)).thenReturn(response);
        
        tester.documentName("SignupUser")
            .variable("name", name)
            .variable("email", email)
            .variable("password", password)
            .execute()
            .path("$['data']['SignupUser']['user']")
            .entity(User.class)
            .isEqualTo(user);
    }

    @Test
    @Order(2)
    void shouldThrowEmailIsRegistered() {
        String name = "MyName";
        String email = "myemail@gmail.com";
        String password = "12345Ab!";
        
        var input = new SignupInput(name, email, password);
        var exception = new DataIntegrityViolationException(null);

        Mockito.when(authService.signup(input)).thenThrow(exception);
        
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
        
        var input = new SignupInput(name, email, password);
        var exception = new BadCredentialsException("Error: Invalid Email");

        Mockito.when(authService.signup(input)).thenThrow(exception);
        
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
        
        var input = new SignupInput(name, email, password);
        var exception = new BadCredentialsException("Error: Invalid Password");
        
        Mockito.when(authService.signup(input)).thenThrow(exception);
        
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