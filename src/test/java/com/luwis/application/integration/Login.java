package com.luwis.application.integration;

import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import com.luwis.application.graphql.types.User;
import com.luwis.application.models.UserModel;
import com.luwis.application.repositories.UserRepository;

@AutoConfigureGraphQlTester
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class Login {

    @Autowired
    private GraphQlTester tester;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtDecoder jwtDecoder;
    
    @Test
    @Order(1)
    void shouldReturnUserAndToken() {
        String name = "MyName";
        String email = "myemail@gmail.com";
        String password = "12345Ab!";

        var newUser = new UserModel(name, email, passwordEncoder.encode(password));
        userRepository.save(newUser);

        tester.documentName("LoginUser")
            .variable("name", name)
            .variable("email", email)
            .variable("password", password)
            .execute()
            .path("$['data']['LoginUser']", path -> {

                path.path("['user']").entity(User.class).satisfies(user -> {
                    user.name().equals(name);
                    user.email().equals(email);
                });

                jwtDecoder.decode(
                    path.path("['token']").entity(String.class).get()
                ).getClaimAsString("scope").equals("READ WRITE UPDATE DELETE");

            });
    }

    @Test
    @Order(2)
    void shouldThrowInvalidEmail() {
        String email = "invalid@email";
        String password = "12345Ab!";
        
        tester.documentName("LoginUser")
            .variable("email", email)
            .variable("password", password)
            .execute()
            .errors()
            .expect(error -> error.getMessage().equals("Error: Invalid Email"))
            .expect(error -> error.getErrorType().equals(ErrorType.BAD_REQUEST))
            .expect(error -> error.getPath().equals("LoginUser"));
    }

    @Test
    @Order(3)
    void shouldThrowInvalidPassword() {
        String email = "myemail@gmail.com";
        String password = "Invalid";
        
        tester.documentName("LoginUser")
            .variable("email", email)
            .variable("password", password)
            .execute()
            .errors()
            .expect(error -> error.getMessage().equals("Error: Invalid Password"))
            .expect(error -> error.getErrorType().equals(ErrorType.BAD_REQUEST))
            .expect(error -> error.getPath().equals("LoginUser"));
    }

    @Test
    @Order(4)
    void shouldThrowUserIsNotRegistered() {
        String email = "unknown@gmail.com";
        String password = "12345Ab!";
        
        tester.documentName("LoginUser")
            .variable("email", email)
            .variable("password", password)
            .execute()
            .errors()
            .expect(error -> error.getMessage().equals("Error: User Is Not Registered"))
            .expect(error -> error.getErrorType().equals(ErrorType.NOT_FOUND))
            .expect(error -> error.getPath().equals("LoginUser"));
    }

}