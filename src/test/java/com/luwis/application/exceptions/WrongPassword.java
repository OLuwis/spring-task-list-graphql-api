package com.luwis.application.exceptions;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.luwis.application.user.UserModel;
import com.luwis.application.user.UserRepository;
import com.luwis.application.user.exceptions.WrongPasswordException;

@AutoConfigureHttpGraphQlTester
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WrongPassword {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private HttpGraphQlTester tester;

    private WrongPasswordException exception = new WrongPasswordException();

    @Test
    void shouldReturnWrongPasswordForUserLogin() {
        String username = "Test";
        String email = "test1@gmail.com";
        String password = "123456Ab!";

        UserModel newUser = new UserModel(username, email, new BCryptPasswordEncoder().encode(password));
        userRepository.save(newUser);

        tester.documentName("userLogin")
        .variable("email", email)
        .variable("password", "1234")
        .execute()
        .path("$['errors'][0]", path -> {
            String errorMessage = path.path("['message']").entity(String.class).get();

            String errorType = path.path("['extensions']['classification']").entity(String.class).get();

            assertAll(
                "shouldReturnWrongPassword",
                () -> assertEquals(errorMessage, exception.getMessage()),
                () -> assertEquals(errorType, exception.getType())
            );
        });
    }

}