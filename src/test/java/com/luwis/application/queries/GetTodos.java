package com.luwis.application.queries;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.HttpGraphQlTester;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.luwis.application.todo.TodoModel;
import com.luwis.application.todo.TodoRepository;
import com.luwis.application.todo.exceptions.UnauthorizedException;

@AutoConfigureHttpGraphQlTester
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetTodos {
    
    @Value("${JWT_SECRET}")
    private String secret;

    @Autowired
    private HttpGraphQlTester tester;

    @Autowired
    private TodoRepository todoRepository;

    @Test
    void shouldReturnAnArrayOfTodos() {
        long id = 1;
        
        String token = JWT.create()
        .withClaim("id", id)
        .withClaim("username", "Luis")
        .withIssuer("Luwis")
        .sign(Algorithm.HMAC256(secret));

        TodoModel todo = new TodoModel("Title", "Desc", id);
        todoRepository.save(todo);

        tester.mutate()
        .header("Authorization", "Bearer " + token)
        .build()
        .documentName("getTodos")
        .execute()
        .path("$['data']['getTodos']", path -> {
            String title = path.path("[0]['title']").entity(String.class).get();
            String desc = path.path("[0]['description']").entity(String.class).get();
            
            Long userid = path.path("[0]['userid']").entity(Long.class).get();

            assertAll(
                "shouldReturnAnArrayOfTodos",
                () -> assertEquals(title, todo.getTitle()),
                () -> assertEquals(desc, todo.getDescription()),
                () -> assertEquals(userid, id)
            );
        });
    }

    @Test
    void shouldReturnActionNotAllowed() {
        tester.documentName("getTodos")
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
    void shouldReturnAnEmptyArray() {
        String token = JWT.create()
        .withClaim("id", 1)
        .withClaim("username", "Luis")
        .withIssuer("Luwis")
        .sign(Algorithm.HMAC256(secret));

        tester.mutate()
        .header("Authorization", "Bearer " + token)
        .build()
        .documentName("getTodos")
        .execute()
        .path("$['data']['getTodos']", path -> {
            path.path("[0]").pathDoesNotExist();
        });
    }

}
