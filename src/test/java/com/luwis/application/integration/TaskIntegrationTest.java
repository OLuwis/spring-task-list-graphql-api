package com.luwis.application.integration;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.HttpGraphQlTester;

import com.luwis.application.entities.Task;

@AutoConfigureHttpGraphQlTester
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class TaskIntegrationTest {
    
    @Autowired
    private HttpGraphQlTester tester;

    private String token;
    
    @BeforeAll
    public void setup() {
        String firstName = "Luis";
        String secondName = "Miguel";
        String email = "test@gmail.com";
        String password = "12345Ab!";
        
        tester.documentName("Signup")
            .variable("firstName", firstName)
            .variable("secondName", secondName)
            .variable("email", email)
            .variable("password", password)
            .execute();

        tester.documentName("Login")
            .variable("email", email)
            .variable("password", password)
            .execute()
            .path("$['data']['Login']", path -> {
                token = path.entity(String.class)
                    .get();
            });
    }

    @Test
    @Order(1)
    public void shouldReturnNewTask() {
        String title = "Title";

        tester.mutate()
            .header("Authorization", "Bearer " + token)
            .build()
            .documentName("CreateTask")
            .variable("title", title)
            .execute()
            .path("$['data']['CreateTask']")
            .entity(Task.class)
            .satisfies(task -> {
                task.getTitle().equals(title);
                task.getPending().equals(true);
                task.getCreatedAt().equals(LocalDate.now());
        });
    }

    @Test
    @Order(2)
    public void shouldReturnUpdatedTask() {
        String title = "Update";
        String description = "Description";
        Boolean pending = false;

        tester.mutate()
            .header("Authorization", "Bearer " + token)
            .build()
            .documentName("UpdateTask")
            .variable("id", 1)
            .variable("title", title)
            .variable("description", description)
            .variable("pending", pending)
            .execute()
            .path("$['data']['UpdateTask']")
            .entity(Task.class)
            .satisfies(task -> {
                task.getTitle().equals(title);
                task.getDescription().equals(description);
                task.getPending().equals(pending);
                task.getCreatedAt().equals(LocalDate.now());
        });
    }

    @Test
    @Order(3)
    public void shouldReturnTasks() {
        tester.mutate()
            .header("Authorization", "Bearer " + token)
            .build()
            .documentName("GetTasks")
            .execute()
            .path("$['data']['GetTasks'][0]")
            .entity(Task.class)
            .satisfies(task -> {
                task.getTitle().equals("Update");
                task.getDescription().equals("Description");
                task.getPending().equals(false);
                task.getCreatedAt().equals(LocalDate.now());
        });
    }

    @Test
    @Order(4)
    public void shouldReturnDeletedTask() {
        tester.mutate()
            .header("Authorization", "Bearer " + token)
            .build()
            .documentName("DeleteTask")
            .variable("id", 1)
            .execute()
            .path("$['data']['DeleteTask']")
            .entity(Task.class)
            .satisfies(task -> {
                task.getTitle().equals("Update");
                task.getDescription().equals("Description");
                task.getPending().equals(false);
                task.getCreatedAt().equals(LocalDate.now());
        });
    }

    @Test
    @Order(5)
    public void shouldThrowTaskNotFound() {
        tester.mutate()
            .header("Authorization", "Bearer " + token)
            .build()
            .documentName("DeleteTask")
            .variable("id", 2)
            .execute()
            .errors()
            .expect(error -> error.getMessage().equals("Task Not Found."))
            .expect(error -> error.getPath().equals("DeleteTask"));
    }

}