package com.luwis.application.queries;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

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

        TodoModel firstTodo = new TodoModel("My Todo!", "My First Todo!", id);
        TodoModel secondTodo = new TodoModel("My Todo!", "My Second Todo!", id);

        List<TodoModel> todos = new ArrayList<>() {{
            add(firstTodo);
            add(secondTodo);
        }};

        todoRepository.saveAll(todos);

        tester.mutate()
        .header("Authorization", "Bearer " + token)
        .build()
        .documentName("getTodos")
        .execute()
        .path("$['data']['getTodos']", path -> {
            String firstTitle = path.path("[0]['title']").entity(String.class).get();
            String secondTitle = path.path("[1]['title']").entity(String.class).get();
            String firstDesc = path.path("[0]['description']").entity(String.class).get();
            String secondDesc = path.path("[1]['description']").entity(String.class).get();
            
            Long userid = path.path("[0]['userid']").entity(Long.class).get();

            assertAll(
                "shouldReturnAnArrayOfTodos",
                () -> assertEquals(firstTitle, firstTodo.getTitle()),
                () -> assertEquals(secondTitle, secondTodo.getTitle()),
                () -> assertEquals(firstDesc, firstTodo.getDescription()),
                () -> assertEquals(secondDesc, secondTodo.getDescription()),
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
                
                () -> assertEquals(errorMessage, exception.message),
                () -> assertEquals(errorType, exception.type)
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
