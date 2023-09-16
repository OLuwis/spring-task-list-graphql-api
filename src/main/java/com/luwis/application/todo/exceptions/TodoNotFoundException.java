package com.luwis.application.todo.exceptions;

import org.springframework.graphql.execution.ErrorType;

import graphql.GraphQLError;

public class TodoNotFoundException extends RuntimeException {
    public String message = "Todo Not Found";

    public String type = ErrorType.NOT_FOUND.toString();
    
    public GraphQLError error() {
        return GraphQLError
        .newError()
        .errorType(ErrorType.NOT_FOUND)
        .message(message)
        .build();
    }
}