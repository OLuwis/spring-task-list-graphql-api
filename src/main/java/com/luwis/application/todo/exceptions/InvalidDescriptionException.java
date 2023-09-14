package com.luwis.application.todo.exceptions;

import org.springframework.graphql.execution.ErrorType;

import graphql.GraphQLError;

public class InvalidDescriptionException extends RuntimeException {
    public GraphQLError error() {
        return GraphQLError.newError()
                            .errorType(ErrorType.BAD_REQUEST)
                            .message("Invalid Description: Please Insert A Description")
                            .build();
    }
}
