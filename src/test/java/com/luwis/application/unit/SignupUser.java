package com.luwis.application.unit;

import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.security.authentication.BadCredentialsException;

import com.luwis.application.controllers.AuthController;
import com.luwis.application.graphql.inputs.SignupInput;
import com.luwis.application.graphql.interfaces.User;
import com.luwis.application.graphql.types.Signup;
import com.luwis.application.services.AuthService;
import com.luwis.application.utils.InputValidator;

@GraphQlTest(AuthController.class)
public class SignupUser {

    @Autowired
    private GraphQlTester tester;

    @MockBean
    private AuthService authService;

    @MockBean
    private InputValidator inputValidator;
    
    @Test
    void shouldReturnNewUser() {
        String name = "MyName";
        String email = "myemail@gmail.com";
        String password = "12345Ab!";
        
        var input = new SignupInput(name, email, password);
        var user = new User((long) 1, name, email);
        var response = new Signup(user);

        Mockito.doReturn(response).when(authService).signup(input);
        
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
    void shouldThrowEmailIsRegistered() {
        String name = "MyName";
        String email = "myemail@gmail.com";
        String password = "12345Ab!";
        
        var input = new SignupInput(name, email, password);
        var exception = new DataIntegrityViolationException(null);

        Mockito.doThrow(exception).when(authService).signup(input);
        
        tester.documentName("SignupUser")
            .variable("name", name)
            .variable("email", email)
            .variable("password", password)
            .execute()
            .errors()
            .expect(error -> error.getMessage().equals("Error: Email Is Registered"));
    }

    @Test
    void shouldThrowInvalidEmail() {
        String name = "MyName";
        String email = "invalid@email";
        String password = "12345Ab!";
        
        var input = new SignupInput(name, email, password);
        var exception = new BadCredentialsException("Error: Invalid Email");

        Mockito.doThrow(exception).when(inputValidator).validate(input);
        
        tester.documentName("SignupUser")
            .variable("name", name)
            .variable("email", email)
            .variable("password", password)
            .execute()
            .errors()
            .expect(error -> error.getMessage().equals("Error: Invalid Email"));
    }

    @Test
    void shouldThrowInvalidPassword() {
        String name = "MyName";
        String email = "myemail@gmail.com";
        String password = "Invalid";
        
        var input = new SignupInput(name, email, password);
        var exception = new BadCredentialsException("Error: Invalid Password");
        
        Mockito.doThrow(exception).when(inputValidator).validate(input);
        
        tester.documentName("SignupUser")
            .variable("name", name)
            .variable("email", email)
            .variable("password", password)
            .execute()
            .errors()
            .expect(error -> error.getMessage().equals("Error: Invalid Password"));
    }

}