package com.luwis.application.mutations;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.luwis.application.user.UserModel;
import com.luwis.application.user.UserRepository;

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
            path
            .path("['username']").entity(String.class).isEqualTo(username)

            .path("['email']").entity(String.class).isEqualTo(email)

            .path("['password']").entity(String.class).isNotEqualTo(password);
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
            path
            .path("['message']").entity(String.class).isEqualTo("Invalid Password: Passwords Must Be Between 8-20 Characters Long And Contain A Number, An Upper And Lowercase Letter, And A Special Symbol")

            .path("['extensions']['classification']").entity(String.class).isEqualTo("BAD_REQUEST");
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
            path
            .path("['message']").entity(String.class).isEqualTo("Invalid Email: Please Put A Valid Email")

            .path("['extensions']['classification']").entity(String.class).isEqualTo("BAD_REQUEST");
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
            path
            .path("['message']").entity(String.class).isEqualTo("Invalid Username: Usernames Must Be Between 3-20 Characters Long")

            .path("['extensions']['classification']").entity(String.class).isEqualTo("BAD_REQUEST");
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
            path
            .path("['message']").entity(String.class).isEqualTo("Invalid Email: Email Is Already In Use")

            .path("['extensions']['classification']").entity(String.class).isEqualTo("BAD_REQUEST");
        });
    }

}
