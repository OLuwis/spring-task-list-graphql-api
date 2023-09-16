package com.luwis.application.todo.exceptions;

import org.springframework.graphql.execution.ErrorType;

import graphql.GraphQLError;

public class UnauthorizedException extends RuntimeException {
    public String message = "Action Not Allowed";

    public String type = ErrorType.UNAUTHORIZED.toString();
    
    public GraphQLError error() {
        return GraphQLError
        .newError()
        .errorType(ErrorType.UNAUTHORIZED)
        .message(message)
        .build();
    }
}
