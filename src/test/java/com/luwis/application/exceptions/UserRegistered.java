package com.luwis.application.exceptions;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import com.luwis.application.user.UserModel;
import com.luwis.application.user.UserRepository;
import com.luwis.application.user.exceptions.UserRegisteredException;

@AutoConfigureHttpGraphQlTester
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserRegistered {

    @Autowired
    private HttpGraphQlTester tester;

    @Autowired
    private UserRepository userRepository;

    private UserRegisteredException exception = new UserRegisteredException();

    @Test
    void shouldReturnUserRegisteredForUserSignup() {
        String username = "Test";
        String email = "test1@gmail.com";
        String password = "123456Ab!";

        UserModel newUser = new UserModel(username, email, password);
        userRepository.save(newUser);

        tester.documentName("userSignup")
        .variable("username", username)
        .variable("email", email)
        .variable("password", password)
        .execute()
        .path("$['errors'][0]", path -> {
            String errorMessage = path.path("['message']").entity(String.class).get();
            String errorType = path.path("['extensions']['classification']").entity(String.class).get();

            assertAll(
                "shouldReturnUserRegistered",
                () -> assertEquals(errorMessage, exception.getMessage()),
                () -> assertEquals(errorType, exception.getType())
            );
        });
    }

}