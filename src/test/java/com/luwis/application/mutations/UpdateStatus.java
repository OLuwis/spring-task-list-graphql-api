package com.luwis.application.mutations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import com.luwis.application.todo.TodoService;

@AutoConfigureHttpGraphQlTester
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UpdateStatus {
    
    @Value("${JWT_SECRET}")
    private String secret;
    
    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private TodoService todoService;

    @Autowired
    private HttpGraphQlTester tester;

    @Test
    void shouldReturnTodoStatusTrue() {
        long id = 1;

        String token = JWT.create()
        .withClaim("id", id)
        .withClaim("username", "Luis")
        .withIssuer("Luwis")
        .sign(Algorithm.HMAC256(secret));

        TodoModel newTodo = new TodoModel("Title", "Desc", id);
        TodoModel todo = todoRepository.save(newTodo);

        tester.mutate()
        .header("Authorization", "Bearer " + token)
        .build()
        .documentName("updateStatus")
        .variable("id", todo.getId())
        .execute()
        .path("$['data']['updateStatus']", path -> {
            boolean status = path.path("['status']").entity(Boolean.class).get();

            assertTrue(status);
        });
    }

    @Test
    void shouldReturnTodoStatusFalse() {
        long id = 1;

        String token = JWT.create()
        .withClaim("id", id)
        .withClaim("username", "Luis")
        .withIssuer("Luwis")
        .sign(Algorithm.HMAC256(secret));

        TodoModel newTodo = new TodoModel("Title", "Desc", id);
        TodoModel todo = todoRepository.save(newTodo);

        todoService.updateStatus(todo.getId(), "Bearer " + token);
        
        tester.mutate()
        .header("Authorization", "Bearer " + token)
        .build()
        .documentName("updateStatus")
        .variable("id", todo.getId())
        .execute()
        .path("$['data']['updateStatus']", path -> {
            boolean status = path.path("['status']").entity(Boolean.class).get();

            assertFalse(status);
        });
    }

}