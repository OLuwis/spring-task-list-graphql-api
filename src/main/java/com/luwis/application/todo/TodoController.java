package com.luwis.application.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import com.luwis.application.RequestHeaderInterceptor;

@Controller
public class TodoController extends RequestHeaderInterceptor {
    
    @Autowired
    private TodoService todoService;

    @MutationMapping
    public TodoModel createTodo(@Argument String title, @Argument String description, @ContextValue String authHeader) {
        return todoService.createTodo(title, description, authHeader);
    }
    
}
