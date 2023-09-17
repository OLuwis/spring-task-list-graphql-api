package com.luwis.application.queries;

import static org.junit.jupiter.api.Assertions.assertAll;
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
            String token = path.entity(String.class).get();

            DecodedJWT jwt = JWT.require(Algorithm.HMAC256(secret))
            .build()
            .verify(token);

            Long tokenUserid = jwt.getClaim("id").asLong();
            String tokenUsername = jwt.getClaim("username").asString();

            assertAll(
                "shouldReturnToken",

                () -> assertEquals(1, tokenUserid),
                () -> assertEquals(username, tokenUsername)
            );
        });
    }

}