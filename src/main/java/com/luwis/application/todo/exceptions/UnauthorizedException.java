package com.luwis.application.todo.exceptions;

import org.springframework.graphql.execution.ErrorType;

import graphql.GraphQLError;

public class UnauthorizedException extends RuntimeException {
    public GraphQLError error() {
        return GraphQLError
                .newError()
                .errorType(ErrorType.UNAUTHORIZED)
                .message("Unauthorized: Action Not Allowed")
                .build();
    }
}
