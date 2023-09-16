package com.luwis.application.mutations;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.HttpGraphQlTester;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.luwis.application.todo.exceptions.InvalidDescriptionException;
import com.luwis.application.todo.exceptions.InvalidTitleException;
import com.luwis.application.todo.exceptions.UnauthorizedException;

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
            Long userid = path.path("['userid']").entity(Long.class).get();
            
            String responseTitle = path.path("['title']").entity(String.class).get();

            String responseDesc = path.path("['description']").entity(String.class).get();
            
            Boolean status = path.path("['status']").entity(Boolean.class).get();

            assertAll(
                "shouldReturnNewTodo",
                () -> assertEquals(userid, 1),
                () -> assertEquals(responseTitle, title),
                () -> assertEquals(responseDesc, description),
                () -> assertFalse(status)
            );
        });
    }

    @Test
    void shouldReturnActionNotAllowed() {
        String title = "My Todo!";
        String description = "My first todo!";

        tester.documentName("createTodo")
        .variable("title", title)
        .variable("description", description)
        .execute()
        .path("$['errors'][0]", path -> {
            UnauthorizedException exception = new UnauthorizedException();

            String errorMessage = path.path("['message']").entity(String.class).get();

            String errorType = path.path("['extensions']['classification']").entity(String.class).get();

            assertAll(
                "shouldReturnInvalidTitle",
                
                () -> assertEquals(errorMessage, exception.getMessage()),
                () -> assertEquals(errorType, exception.getType())
            );
        });
    }

    @Test
    void shouldReturnInvalidTitle() {
        String title = "";
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
        .path("$['errors'][0]", path -> {
            InvalidTitleException exception = new InvalidTitleException();

            String errorMessage = path.path("['message']").entity(String.class).get();

            String errorType = path.path("['extensions']['classification']").entity(String.class).get();

            assertAll(
                "shouldReturnInvalidTitle",
                
                () -> assertEquals(errorMessage, exception.getMessage()),
                () -> assertEquals(errorType, exception.getType())
            );
        });
    }


    @Test
    void shouldReturnInvalidDescription() {
        String title = "My todo!";
        String description = "";

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
        .path("$['errors'][0]", path -> {
            InvalidDescriptionException exception = new InvalidDescriptionException();

            String errorMessage = path.path("['message']").entity(String.class).get();

            String errorType = path.path("['extensions']['classification']").entity(String.class).get();

            assertAll(
                "shouldReturnInvalidTitle",
                
                () -> assertEquals(errorMessage, exception.getMessage()),
                () -> assertEquals(errorType, exception.getType())
            );
        });       
    }

}