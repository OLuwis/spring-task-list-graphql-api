package com.luwis.application.integration;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.HttpGraphQlTester;

import com.luwis.application.entities.User;

@AutoConfigureHttpGraphQlTester
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@TestMethodOrder(OrderAnnotation.class)
public class AuthIntegrationTest {
    
    @Autowired
    private HttpGraphQlTester tester;

    @Test
    @Order(1)
    public void shouldReturnNewUser() {
        String firstName = "Luis";
        String secondName = "Miguel";
        String email = "test@gmail.com";
        String password = "12345Ab!";
        
        tester.documentName("Signup")
            .variable("firstName", firstName)
            .variable("secondName", secondName)
            .variable("email", email)
            .variable("password", password)
            .execute()
            .path("$['data']['Signup']")
            .entity(User.class)
            .satisfies(user -> {
                user.getFirstName().equals(firstName);
                user.getSecondName().equals(secondName);
                user.getEmail().equals(email);
        });
    }
    
    @Test
    @Order(2)
    public void shouldReturnJwtToken() {
        String email = "test@gmail.com";
        String password = "12345Ab!";
        
        tester.documentName("Login")
            .variable("email", email)
            .variable("password", password)
            .execute()
            .path("$['data']['Login']")
            .entity(String.class)
            .get()
            .matches("/^(?:[\\w-]*\\.){2}[\\w-]*$/");
    }

    @Test
    @Order(3)
    public void shouldThrowUserIsRegistered() {
        String firstName = "Luis";
        String secondName = "Miguel";
        String email = "test@gmail.com";
        String password = "12345Ab!";
        
        tester.documentName("Signup")
            .variable("firstName", firstName)
            .variable("secondName", secondName)
            .variable("email", email)
            .variable("password", password)
            .execute()
            .errors()
            .expect(error -> error.getMessage().equals("Email Is Already Registered"))
            .expect(error -> error.getPath().equals("Signup"));
    }

    @Test
    @Order(4)
    public void shouldThrowUserNotFound() {
        String email = "test1@gmail.com";
        String password = "12345Ab!";
        
        tester.documentName("Login")
            .variable("email", email)
            .variable("password", password)
            .execute()
            .errors()
            .expect(error -> error.getMessage().equals("This Email Is Not Registered"))
            .expect(error -> error.getPath().equals("Login"));
    }

    @Test
    @Order(5)
    public void shouldThrowInvalidEmail() {
        String email = "test@gmail";
        String password = "12345Ab!";
        
        tester.documentName("Login")
            .variable("email", email)
            .variable("password", password)
            .execute()
            .errors()
            .expect(error -> error.getMessage().equals("Invalid Email."))
            .expect(error -> error.getPath().equals("Login"));
    }

    @Test
    @Order(6)
    public void shouldThrowInvalidPassword() {
        String email = "test@gmail.com";
        String password = "12345Ab";
        
        tester.documentName("Login")
            .variable("email", email)
            .variable("password", password)
            .execute()
            .errors()
            .expect(error -> error.getMessage().equals("Invalid Password."))
            .expect(error -> error.getPath().equals("Login"));
    }

}