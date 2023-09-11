package com.luwis.application.user.exceptions;

import org.springframework.graphql.execution.ErrorType;

import graphql.GraphQLError;

public class UserNotFoundException extends RuntimeException {
    public GraphQLError error() {
        return GraphQLError
            .newError()
            .errorType(ErrorType.NOT_FOUND)
            .message("User Doesn't Exist")
            .build();
    }
}
