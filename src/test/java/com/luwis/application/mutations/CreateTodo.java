package com.luwis.application.mutations;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.HttpGraphQlTester;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@AutoConfigureHttpGraphQlTester
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CreateTodo {

    @Value("${JWT_SECRET}")
    private String secret;
    
    @Autowired
    private HttpGraphQlTester tester;

    @Test
    void shouldReturnNewTodo() {
        String title = "My Todo!";
        String description = "My first todo!";

        String token = JWT.create()
        .withClaim("id", 1)
        .withClaim("username", "Luis")
        .withIssuer("Luwis")
        .sign(Algorithm.HMAC256(secret));

        tester.mutate()
            .header("Authorization", "Bearer " + token)
            .build()
            .documentName("createTodo")
            .variable("title", title)
            .variable("description", description)
            .execute()
            .path("$['data']['createTodo']", path -> {
                Long responseUserid = path.path("['userid']").entity(Long.class).get();
                
                String responseTitle = path.path("['title']").entity(String.class).get();

                String responseDesc = path.path("['description']").entity(String.class).get();
                
                Boolean responseStatus = path.path("['status']").entity(Boolean.class).get();

                assertEquals(responseUserid, 1);
                assertEquals(responseTitle, title);
                assertEquals(responseDesc, description);
                assertEquals(responseStatus, false);
            });

    }
}
