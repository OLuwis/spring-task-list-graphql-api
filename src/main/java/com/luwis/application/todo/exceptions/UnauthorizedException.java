package com.luwis.application.todo.exceptions;

import org.springframework.graphql.execution.ErrorType;

import graphql.GraphQLError;

public class UnauthorizedException extends RuntimeException {
    private String message = "Action Not Allowed";

    private String type = ErrorType.UNAUTHORIZED.toString();
    
    public GraphQLError error() {
        return GraphQLError
        .newError()
        .errorType(ErrorType.UNAUTHORIZED)
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
