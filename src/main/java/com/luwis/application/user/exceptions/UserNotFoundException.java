package com.luwis.application.user.exceptions;

import org.springframework.graphql.execution.ErrorType;

import graphql.GraphQLError;

public class UserNotFoundException extends RuntimeException {
    public String message = "User Doesn't Exist";

    public String type = ErrorType.NOT_FOUND.toString();

    public GraphQLError error() {
        return GraphQLError
        .newError()
        .errorType(ErrorType.NOT_FOUND)
        .message(message)
        .build();
    }
}
