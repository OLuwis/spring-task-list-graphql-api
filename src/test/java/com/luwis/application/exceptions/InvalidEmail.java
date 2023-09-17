package com.luwis.application.exceptions;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.HttpGraphQlTester;

import com.luwis.application.user.exceptions.InvalidEmailException;

@AutoConfigureHttpGraphQlTester
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InvalidEmail {

    @Autowired
    private HttpGraphQlTester tester;

    private InvalidEmailException exception = new InvalidEmailException();

    @Test
    void shouldReturnInvalidEmailForUserSignup() {
        String username = "Test";
        String email = "test1@.com";
        String password = "123456Ab!";

        tester.documentName("userSignup")
        .variable("username", username)
        .variable("email", email)
        .variable("password", password)
        .execute()
        .path("$['errors'][0]", path -> {
            String errorMessage = path.path("['message']").entity(String.class).get();
            String errorType = path.path("['extensions']['classification']").entity(String.class).get();

            assertAll(
                "shouldReturnInvalidEmail",
                () -> assertEquals(errorMessage, exception.getMessage()),
                () -> assertEquals(errorType, exception.getType())
            );
        });
    }

    @Test
    void shouldReturnInvalidEmailForUserLogin() {
        String email = "test1@.com";
        String password = "123456Ab!";

        tester.documentName("userLogin")
        .variable("email", email)
        .variable("password", password)
        .execute()
        .path("$['errors'][0]", path -> {
            String errorMessage = path.path("['message']").entity(String.class).get();
            String errorType = path.path("['extensions']['classification']").entity(String.class).get();

            assertAll(
                "shouldReturnInvalidEmail",
                () -> assertEquals(errorMessage, exception.getMessage()),
                () -> assertEquals(errorType, exception.getType())
            );
        });
    }

}