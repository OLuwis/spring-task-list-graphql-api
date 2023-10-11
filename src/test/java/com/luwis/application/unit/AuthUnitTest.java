package com.luwis.application.unit;

import java.security.InvalidParameterException;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;

import com.luwis.application.configs.Beans;
import com.luwis.application.controllers.AuthController;
import com.luwis.application.entities.User;
import com.luwis.application.middlewares.Validator;
import com.luwis.application.services.AuthService;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

@GraphQlTest(AuthController.class)
@Import(Beans.class)
@TestMethodOrder(OrderAnnotation.class)
public class AuthUnitTest {
    
    @Autowired
    private GraphQlTester graphQlTester;
    
    @MockBean
    private Validator validator;

    @MockBean
    private AuthService service;
    
    @Test
    @Order(1)
    public void ShouldReturnNewUser() {
        String firstName = "Luis";
        String secondName = "Miguel";
        String email = "test@gmail.com";
        String password = "12345Ab!";

        User newUser = new User();
        newUser.setId((long) 1);
        newUser.setFirstName(firstName);
        newUser.setSecondName(secondName);
        newUser.setEmail(email);
        newUser.setPassword(password);

        Mockito.when(service.Signup(firstName, secondName, email, password)).thenReturn(newUser);
        
        graphQlTester.documentName("Signup")
            .variable("firstName", firstName)
            .variable("secondName", secondName)
            .variable("email", email)
            .variable("password", password)
            .execute()
            .path("$['data']['Signup']")
            .entity(User.class)
            .get()
            .equals(newUser);
    }

    @Test
    @Order(2)
    public void ShouldThrowUserRegistered() {
        String firstName = "Luis";
        String secondName = "Miguel";
        String email = "test@gmail.com";
        String password = "12345Ab!";

        var exception = new EntityExistsException("Email Is Already Registered");

        Mockito.when(service.Signup(firstName, secondName, email, password)).thenThrow(exception);
        
        graphQlTester.documentName("Signup")
            .variable("firstName", firstName)
            .variable("secondName", secondName)
            .variable("email", email)
            .variable("password", password)
            .execute()
            .errors()
            .expect(error -> error.getMessage().equals(exception.getMessage()))
            .expect(error -> error.getPath().equals("Signup"));
    }

    @Test
    @Order(3)
    public void ShouldReturnAToken() {
        String email = "test@gmail.com";
        String password = "12345Ab!";

        Mockito.when(service.Login(email, password)).thenReturn("Header.Payload.Signature");
        
        graphQlTester.documentName("Login")
            .variable("email", email)
            .variable("password", password)
            .execute()
            .path("$['data']['Login']")
            .entity(String.class)
            .get()
            .matches("/^(?:[\\w-]*\\.){2}[\\w-]*$/");
    }

    @Test
    @Order(4)
    public void ShouldThrowUserNotFound() {
        String email = "test@gmail.com";
        String password = "12345Ab!";

        var exception = new EntityNotFoundException("This Email Is Not Registered");
        
        Mockito.when(service.Login(email, password)).thenThrow(exception);
        
        graphQlTester.documentName("Login")
            .variable("email", email)
            .variable("password", password)
            .execute()
            .errors()
            .expect(error -> error.getMessage().equals(exception.getMessage()))
            .expect(error -> error.getPath().equals("Login"));
    }

    @Test
    @Order(5)
    public void ShouldThrowInvalidEmail() {
        String email = "test@";
        String password = "12345Ab!";

        var exception = new InvalidParameterException("Email Needs To Be Valid.");
        
        Mockito.doThrow(exception).when(validator).validate(email, password);
        
        graphQlTester.documentName("Login")
            .variable("email", email)
            .variable("password", password)
            .execute()
            .errors()
            .expect(error -> error.getMessage().equals(exception.getMessage()))
            .expect(error -> error.getPath().equals("Login"));
    }

    @Test
    @Order(6)
    public void ShouldThrowInvalidPassword() {
        String email = "test@gmail.com";
        String password = "12345Ab";

        var exception = new InvalidParameterException("Passwords Needs To Have 8-20 Chars, 1 Uppercase, 1 Lowercase, 1 Number And 1 Symbol.");
        
        Mockito.doThrow(exception).when(validator).validate(email, password);
        
        graphQlTester.documentName("Login")
            .variable("email", email)
            .variable("password", password)
            .execute()
            .errors()
            .expect(error -> error.getMessage().equals(exception.getMessage()))
            .expect(error -> error.getPath().equals("Login"));
    }

}