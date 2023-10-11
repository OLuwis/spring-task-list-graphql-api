package com.luwis.application.controllers;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.luwis.application.entities.Task;
import com.luwis.application.inputs.CreateTaskInput;
import com.luwis.application.inputs.UpdateTaskInput;
import com.luwis.application.services.TaskService;
import com.luwis.application.services.TokenService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final TokenService tokenService;
    
    @MutationMapping
    public Task CreateTask(@Argument CreateTaskInput input) {
        return taskService.CreateTask(input.title(), input.description(), Long.valueOf(tokenService.extractSubject()));
    }

    @MutationMapping
    public Task UpdateTask(@Argument UpdateTaskInput input) {
        return taskService.UpdateTask(input.id(), input.title(), input.description(), input.pending(), Long.valueOf(tokenService.extractSubject()));
    }
    
    @MutationMapping
    public Task DeleteTask(@Argument Long id) {
        return taskService.DeleteTask(id, Long.valueOf(tokenService.extractSubject()));
    }
    
    @QueryMapping
    public List<Task> GetTasks() {
        return taskService.GetTasks(Long.valueOf(tokenService.extractSubject()));
    }
    
}