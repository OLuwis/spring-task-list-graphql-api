package com.luwis.application.exceptions;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.HttpGraphQlTester;

import com.luwis.application.user.exceptions.InvalidUsernameException;

@AutoConfigureHttpGraphQlTester
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InvalidUsername {

    @Autowired
    private HttpGraphQlTester tester;

    private InvalidUsernameException exception = new InvalidUsernameException();

    @Test
    void shouldReturnInvalidEmailForUserSignup() {
        String username = "T";
        String email = "test1@gmail.com";
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
                "shouldReturnInvalidUsername",
                () -> assertEquals(errorMessage, exception.getMessage()),
                () -> assertEquals(errorType, exception.getType())
            );
        });
    }

}