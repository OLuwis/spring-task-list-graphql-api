package com.luwis.application.todo;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import com.luwis.application.RequestHeaderInterceptor;

@Controller
public class TodoController extends RequestHeaderInterceptor {
    
    @MutationMapping
    public String createTodo(@Argument String title, @Argument String description, @ContextValue String authHeader) {
        String token = authHeader.split(" ")[1];
        return token;
    }
    
}
