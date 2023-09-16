package com.luwis.application.user.exceptions;

import org.springframework.graphql.execution.ErrorType;

import graphql.GraphQLError;

public class UserNotFoundException extends RuntimeException {
    private String message = "User Doesn't Exist";

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
