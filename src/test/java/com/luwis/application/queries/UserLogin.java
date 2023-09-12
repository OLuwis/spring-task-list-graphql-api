package com.luwis.application.queries;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.luwis.application.user.UserModel;
import com.luwis.application.user.UserRepository;

@AutoConfigureHttpGraphQlTester
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserLogin {

    @Autowired
    private HttpGraphQlTester tester;

    @Autowired
    private UserRepository userRepository;
    
    @Value("${JWT_SECRET}")
    private String secret;

    @Test
    void shouldReturnToken() {
        String username = "test";
        String email = "test1@gmail.com";
        String password = "123456Ab!";

        UserModel user = new UserModel(username, email, new BCryptPasswordEncoder().encode(password));
        userRepository.save(user);

        tester.documentName("userLogin")
        .variable("email", email)
        .variable("password", password)
        .execute()
        .path("$['data']['userLogin']", path -> {
            DecodedJWT jwt = JWT.require(Algorithm.HMAC256(secret))
                                .build()
                                .verify(path.entity(String.class).get());
                                
           assertEquals("Luwis", jwt.getIssuer());
           assertEquals(1, jwt.getClaim("id").asLong());
           assertEquals("test", jwt.getClaim("username"));
        });
    }

    @Test
    void shouldReturnUserNotFound() {
        String email = "test2@gmail.com";
        String password = "123456Ab!";

        tester.documentName("userLogin")
        .variable("email", email)
        .variable("password", password)
        .execute()
        .path("$['errors'][0]", path -> {
            path
            .path("['message']").entity(String.class).isEqualTo("User Doesn't Exist")

            .path("['extensions']['classification']").entity(String.class).isEqualTo("NOT_FOUND");
        });
    }

    @Test
    void shouldReturnWrongPassword() {
        String username = "test3";
        String email = "test3@gmail.com";
        String password = "123456Ab!";
        String wrongPass = "12345";

        UserModel user = new UserModel(username, email, new BCryptPasswordEncoder().encode(password));
        userRepository.save(user);

        tester.documentName("userLogin")
        .variable("email", email)
        .variable("password", wrongPass)
        .execute()
        .path("$['errors'][0]", path -> {
            path
            .path("['message']").entity(String.class).isEqualTo("Wrong Password")

            .path("['extensions']['classification']").entity(String.class).isEqualTo("BAD_REQUEST");
        });
    }

}
