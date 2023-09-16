package com.luwis.application.todo.exceptions;

import org.springframework.graphql.execution.ErrorType;

import graphql.GraphQLError;

public class TodoNotFoundException extends RuntimeException {
    private String message = "Todo Not Found";

    private String type = ErrorType.NOT_FOUND.toString();
    
    public GraphQLError error() {
        return GraphQLError
        .newError()
        .errorType(ErrorType.NOT_FOUND)
        .message(message)
        .build();
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }
}