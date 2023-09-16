package com.luwis.application.mutations;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.luwis.application.user.UserModel;
import com.luwis.application.user.UserRepository;
import com.luwis.application.user.exceptions.UserRegisteredException;
import com.luwis.application.user.exceptions.InvalidEmailException;
import com.luwis.application.user.exceptions.InvalidPasswordException;
import com.luwis.application.user.exceptions.InvalidUsernameException;

@AutoConfigureHttpGraphQlTester
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserSignup {
    
    @Autowired
    private HttpGraphQlTester tester;

    @Autowired
    private UserRepository userRepository;
    
    @Test
    void shouldReturnUserDetails() {
        String username = "Luis";
        String email = "test1@gmail.com";
        String password = "123456Ab!";
        
        tester.documentName("userSignup")
        .variable("username", username)
        .variable("email", email)
        .variable("password", password)
        .execute()
        .path("$['data']['userSignup']", path -> {
            String responseUsername = path.path("['username']").entity(String.class).get();

            String responseEmail = path.path("['email']").entity(String.class).get();

            String responsePassword = path.path("['password']").entity(String.class).get();

            assertAll(
                "shouldReturnUserDetails",

                () -> assertEquals(responseUsername, username),
                () -> assertEquals(responseEmail, email),
                () -> assertTrue(new BCryptPasswordEncoder().matches(password, responsePassword))
            );
        });
    }

    @Test
    void shouldReturnInvalidPassword() {
        String username = "Luis";
        String email = "test2@gmail.com";
        String password = "12345678";

        tester.documentName("userSignup")
        .variable("username", username)
        .variable("email", email)
        .variable("password", password)
        .execute()
        .path("$['errors'][0]", path -> {
            InvalidPasswordException exception = new InvalidPasswordException();

            String errorMessage = path.path("['message']").entity(String.class).get();

            String errorType = path.path("['extensions']['classification']").entity(String.class).get();

            assertAll(
                "shouldReturnInvalidPassword",

                () -> assertEquals(errorMessage, exception.getMessage()),
                () -> assertEquals(errorType, exception.getType())
            );
        });
    }

    @Test
    void shouldReturnInvalidEmail() {
        String username = "Luis";
        String email = "test3@.com";
        String password = "123456Ab!";

        tester.documentName("userSignup")
        .variable("username", username)
        .variable("email", email)
        .variable("password", password)
        .execute()
        .path("$['errors'][0]", path -> {
            InvalidEmailException exception = new InvalidEmailException();

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
    void shouldReturnInvalidUsername() {
        String username = "Lu";
        String email = "test4@gmail.com";
        String password = "123456Ab!";

        tester.documentName("userSignup")
        .variable("username", username)
        .variable("email", email)
        .variable("password", password)
        .execute()
        .path("$['errors'][0]", path -> {
            InvalidUsernameException exception = new InvalidUsernameException();

            String errorMessage = path.path("['message']").entity(String.class).get();

            String errorType = path.path("['extensions']['classification']").entity(String.class).get();

            assertAll(
                "shouldReturnInvalidUsername",

                () -> assertEquals(errorMessage, exception.getMessage()),
                () -> assertEquals(errorType, exception.getType())
            );
        });
    }

    @Test
    void shouldReturnEmailAlreadyUsed() {
        String username = "Luis";
        String email = "test5@gmail.com";
        String password = "Luis260902!";

        UserModel newUser = new UserModel(username, email, new BCryptPasswordEncoder().encode(password));
        userRepository.save(newUser);

        tester.documentName("userSignup")
        .variable("username", username)
        .variable("email", email)
        .variable("password", password)
        .execute()
        .path("$['errors'][0]", path -> {
            UserRegisteredException exception = new UserRegisteredException();

            String errorMessage = path
            .path("['message']").entity(String.class).get();

            String errorType = path.path("['extensions']['classification']").entity(String.class).get();

            assertAll(
                "shouldReturnEmailAlreadyUsed",

                () -> assertEquals(errorMessage, exception.getMessage()),
                () -> assertEquals(errorType, exception.getType())
            );
        });
    }
       
}